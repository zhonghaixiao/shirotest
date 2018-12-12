package com.example.demo.redistest.vote.service;

import com.example.demo.redistest.vote.domain.Article;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VoteService {

    private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    private static final int VOTE_SCORE = 432;
    private static final int ARTICLES_PER_PAGE = 25;

    @Autowired
    Jedis jedis;

    public String postArticle(Article article){
        String articleId = String.valueOf(jedis.incr("articles:"));
        article.setId(articleId);
        String voted = "voted:" + articleId;
        jedis.sadd(voted, article.getUser());
        jedis.expire(voted, ONE_WEEK_IN_SECONDS);

        long now = System.currentTimeMillis()/1000;
        article.setTime(String.valueOf(now));
        String articleKey = "article:" + articleId;
        Map<String,String> maps = toStringMap(article);
        jedis.hmset(articleKey, maps);
        jedis.zadd("score:", now + VOTE_SCORE, articleKey);
        jedis.zadd("time:", now, articleKey);
        return articleId;
    }

    public void articleVote(String user, String articleId){
        jedis.pipelined();
        long cutoff = (System.currentTimeMillis() /1000) - ONE_WEEK_IN_SECONDS;
        if (jedis.zscore("time:", "article:" + articleId) < cutoff){
            return;
        }
        if (jedis.sadd("voted:" + articleId, user) == 1){
            jedis.zincrby("score:", VOTE_SCORE, "article:" + articleId);
            jedis.hincrBy("article:" + articleId, "votes", 1);
        }
    }

    public List<Article> getArticles(int page, String order){
        int start = (page - 1) * ARTICLES_PER_PAGE;
        int end = start + ARTICLES_PER_PAGE - 1;
        Set<String> ids = jedis.zrevrange(order, start, end);
        return ids.stream().map(id->getArticleById(id.split(":")[1])).collect(Collectors.toList());
    }

    public Article getArticleById(String articleId){
        Map<String, String> res = jedis.hgetAll("article:" + articleId);
        return mapToArticle(res);
    }

    public void addGroups(String articleId, String[] toAdd){
        String article = "article:" + articleId;
        for (String group : toAdd){
            jedis.sadd("group:" + group, article);
        }
    }

    public List<Article> getGroupArticles(String group, int page){
        return getGroupArticles(group, page, "score:");
    }

    public List<Article> getGroupArticles(String group, int page, String order){
        String key = order + group;
        if (!jedis.exists(key)){
            ZParams params = new ZParams().aggregate(ZParams.Aggregate.MAX);
            jedis.zinterstore(key, params, "group:" + group, order);
            jedis.expire(key, 60);
        }
        return getArticles(page, key);
    }



    private Article mapToArticle(Map<String, String> res) {
        Article article = new Article();
        for (Map.Entry<String,String> e: res.entrySet()){
            try {
                BeanUtils.setProperty(article, e.getKey(), e.getValue());
            } catch (IllegalAccessException|InvocationTargetException e1) {
                throw new RuntimeException(e1);
            }
        }
        return article;
    }

    private HashMap<String,String> toStringMap(Article article) {
        HashMap<String,String> fieldMap = new HashMap<>();
        for (Field field : article.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                fieldMap.put(field.getName(), BeanUtils.getProperty(article, field.getName()));
            } catch (IllegalAccessException |InvocationTargetException|NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return fieldMap;
    }
}
