package com.example.demo.myrestfulservice.bean;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseData {
    private int count;
    private List<User> user;
}
