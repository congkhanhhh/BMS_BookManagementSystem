package com.bookstore.project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Getter
@Setter
public class AppConfig implements WebMvcConfigurer {
    @Value("${app.jwt.publicKey}")
    private String publicKey;
    @Value("${app.jwt.privateKey}")
    private String privateKey;
    @Value("${app.jwt.expirationTime}")
    private Long expirationTime;
    @Value("${app.uploadPath:/uploads}")
    private String uploadPath;
    @Value("${app.uploadDir:/opt/tomcat}")
    private String uploadDir;
    @Value("${domain.seller.mail.activated.link:dummyData}")
    private String verifyLink;
    @Value("${domain.seller.mail.activated.redirect:dummyData}")
    private String verifySuccessLink;
}
