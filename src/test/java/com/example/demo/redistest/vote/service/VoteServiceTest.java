package com.example.demo.redistest.vote.service;

import com.example.demo.redistest.vote.domain.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VoteServiceTest {

    @Autowired
    Jedis jedis;

    @Autowired
    VoteService service;

    @Test
    public void testRedis() {
        jedis.set("name", "zhonghaixiao");
    }

    @Test
    public void postArticle() {
        Article article1 = Article.builder()
                .user("user:1231")
                .title("a title1")
                .link("a link1 by user:1231")
                .votes("0")
                .build();
        String articleId = service.postArticle(article1);
        System.out.println(articleId);
    }

    @Test
    public void articleVote() {
        service.articleVote("user:111", "1");
    }

    @Test
    public void getArticles() {
        System.out.println(service.getArticles(1, "score:"));
    }

    @Test
    public void getArticleById() {
        System.out.println(service.getArticleById("1"));
    }

    @Test
    public void addGroups() {
        service.addGroups("1", new String[]{"history"});
    }

    @Test
    public void getGroupArticles() {
        System.out.println(service.getGroupArticles("history", 1, "score:"));
    }

}