package com.example.demo.redistest.vote.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    private String id;
    private String title;
    private String link;
    private String user;
    private String time;
    private String votes;

}
