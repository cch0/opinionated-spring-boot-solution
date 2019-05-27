package com.demo.cloudsleuth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;

import static org.zalando.logbook.Conditions.contentType;

@Configuration
public class Config {
//    @Bean
//    public Logbook logbook() {
//        return Logbook.builder()
//                .condition(
//                        exclude(
//                        requestTo("/health"),
//                        requestTo("/admin/**"),
//                        contentType("application/octet-stream"),
//                        header("X-Secret", newHashSet("1", "true")::contains)))
//                .build();
//    }
}
