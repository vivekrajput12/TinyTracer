package com.url.shortner.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
//@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uid;
    private String email;

    @NotNull(message = "Username cannot be null")
    @Column(nullable = false,unique = true)
    private String username;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roleid" , referencedColumnName = "id")
    private Role role;
}
