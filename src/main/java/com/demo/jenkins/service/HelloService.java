package com.demo.jenkins.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getMessage() {
        return "Jenkins + Spring Boot Demo";
    }

    public int calculate(int a, int b) {
        return a + b;
    }
}
