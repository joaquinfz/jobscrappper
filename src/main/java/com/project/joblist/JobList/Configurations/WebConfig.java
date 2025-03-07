package com.project.joblist.JobList.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:4200");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{spring:\\w+}").setViewName("forward:/index.html");
        registry.addViewController("/**/{spring:\\w+}").setViewName("forward:/index.html");
        registry.addViewController("/{spring:\\w+}/**{spring:?!(\\.js|\\.css|\\.png|\\.jpg|\\.jpeg|\\.gif|\\.svg|\\.ico|\\.ttf|\\.woff|\\.woff2|\\.eot|\\.otf|\\.json)}")
                .setViewName("forward:/index.html");
    }
}