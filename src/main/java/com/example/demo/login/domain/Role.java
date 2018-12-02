package com.example.demo.login.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Role {
    private long id;
    private String description;
    private List<Permission> permissions;
}
