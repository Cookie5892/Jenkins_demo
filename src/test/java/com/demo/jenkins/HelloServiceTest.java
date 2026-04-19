package com.demo.jenkins;

import com.demo.jenkins.service.HelloService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelloServiceTest {

    private final HelloService service = new HelloService();

    @Test
    void testGetMessage() {
        assertEquals("Jenkins + Spring Boot Demo", service.getMessage());
    }

    @Test
    void testCalculate() {
        assertEquals(5, service.calculate(2, 3));
    }
}
