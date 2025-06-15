package com.example.demo.myrestfulservice.bean;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HelloWorldBean {
    private final String message;

//    public HelloWolrdBean(String message) {
//        this.message = message;
//    }
}
