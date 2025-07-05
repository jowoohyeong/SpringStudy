package com.example.demo.myrestfulservice.controller;

import com.example.demo.myrestfulservice.bean.AdminUser;
import com.example.demo.myrestfulservice.bean.AdminUserV2;
import com.example.demo.myrestfulservice.bean.User;
import com.example.demo.myrestfulservice.dao.UserDaoService;
import com.example.demo.myrestfulservice.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsersAdmin() {
        List<User> users = service.findAll();

        List<AdminUser> adminUsers = new ArrayList<>();
        AdminUser adminUser = null;
        for (User user : users) {
            adminUser = new AdminUser();

            BeanUtils.copyProperties(user, adminUser);
            adminUsers.add(adminUser);

        }
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
        mapping.setFilters(filters);

        return mapping;
    }

//    @GetMapping(value ="/v1/users/{id}")
//    @GetMapping(value ="/users/{id}", params = "version=1")
//    @GetMapping(value ="/users/{id}", headers="X-API-VERSION=1")
    @GetMapping(value ="/users/{id}", produces="application/vnd.company.appv1+json")
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) {
        User user = service.findOne(id);

        AdminUser adminUser = new AdminUser();
        if(user == null){
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        } else {
            BeanUtils.copyProperties(user, adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
        mapping.setFilters(filters);

        return mapping;
    }

//    @GetMapping("/v2/users/{id}")
//    @GetMapping(value = "/users/{id}", params = "version=2")
//    @GetMapping(value ="/users/{id}", headers="X-API-VERSION=2")
@GetMapping(value ="/users/{id}", produces="application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
        User user = service.findOne(id);

        AdminUserV2 adminUserV2 = new AdminUserV2();

        if(user == null){
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        } else {
            BeanUtils.copyProperties(user, adminUserV2);
            adminUserV2.setGrade("VIP");
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(adminUserV2);
        mapping.setFilters(filters);

        return mapping;
    }

}
