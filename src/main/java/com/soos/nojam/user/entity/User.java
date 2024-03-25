package com.soos.nojam.user.entity;

import com.soos.nojam.user.security.PasswordEncoder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @NotBlank
    @Column(length = 100, nullable = false, unique = true)
    private String nickname;
    @NotBlank
    @Column(length = 256, nullable = false)
    private String password;
    @Column
    private String introduction;

    @Builder
    public User(String email, String nickname, String password) {
        Assert.hasText(email, "이메일은 필수입니다.");
        Assert.hasText(nickname, "닉네임은 필수입니다.");
        Assert.hasText(password, "비밀번호는 필수입니다.");

        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public void updateIntroduction(final String introduction) {
        this.introduction = introduction;
    }

    public boolean doesMatchPassword(final String password, final PasswordEncoder encoder) {
        return encoder.verifyPassword(password, this.password);
    }
}
