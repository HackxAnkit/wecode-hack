// java
package com.wecode.bookit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApp {
    public static void main(String[] args) {
        // Ensure no blocking console reads here
        SpringApplication.run(MainApp.class, args);
    }
}
