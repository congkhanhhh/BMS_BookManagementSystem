package com.bookstore.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "role_id")
    private int roleId;

    @Column(nullable = false)
    private boolean enable;

    @Column(name = "create_at", nullable = false)
    private LocalDate createAt;

    @ManyToMany(fetch = FetchType.LAZY) // Đảm bảo thêm @ManyToMany
    @JoinTable(
            name = "user_role", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User() {
    }
}

