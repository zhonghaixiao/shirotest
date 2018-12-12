package com.example.demo.login.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private long id;
    private String description;
    private List<Permission> permissions;
}
