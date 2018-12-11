package com.example.demo.login.domain;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private long id;
    private String name;
}
