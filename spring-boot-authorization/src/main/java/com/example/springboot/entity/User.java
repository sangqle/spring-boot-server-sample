package com.example.springboot.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
@Where(clause="is_deleted=0")
public class User {
    private static final int serialVersionUID = -Integer.MIN_VALUE;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int userId;
    private long updatedAt;
    private String email;
    private String password;
    private String phoneNumber;
    private Integer isDeleted;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}