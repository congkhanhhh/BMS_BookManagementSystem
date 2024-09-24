package com.bookstore.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "address", length = 45)
    private String address;

    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @Column(name = "picture")
    private String picture;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @Column(name = "role_id")
    private Integer roleId; // Sử dụng Integer thay vì int


    @Column(name = "create_at", nullable = false)
    private LocalDate createAt;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "token_expiration")
    private Date tokenExpiration;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();

    public User() {
        this.createAt = LocalDate.now();
    }
}
