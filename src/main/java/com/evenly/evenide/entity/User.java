package com.evenly.evenide.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String provider = "local";

    private String providerId;


    public static User create(String email, String encodedPassword, String nickname) {
        User user = new User();
        user.email = email;
        user.password = encodedPassword;
        user.nickname = nickname;
        user.provider = "local";
        return user;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public static User createSocialUser(String email, String nickname, String provider, String providerId) {
        User user = new User();
        user.email = email;
        user.password = null;
        user.nickname = nickname;
        user.provider = provider;
        user.providerId = providerId;
        return user;
    }
}
