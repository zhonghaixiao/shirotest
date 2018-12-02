package com.example.demo.login.domain;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements Serializable {
    private long id;
    private String name;
    private String password;
    private String enable;
    List<Role> roles;
}
