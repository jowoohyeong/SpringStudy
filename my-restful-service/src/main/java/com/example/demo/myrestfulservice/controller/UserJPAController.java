package com.example.demo.myrestfulservice.controller;

import com.example.demo.myrestfulservice.bean.Post;
import com.example.demo.myrestfulservice.bean.ResponseData;
import com.example.demo.myrestfulservice.bean.User;
import com.example.demo.myrestfulservice.exception.UserNotFoundException;
import com.example.demo.myrestfulservice.repository.PostRepository;
import com.example.demo.myrestfulservice.repository.UserRepository;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
public class UserJPAController {

    private UserRepository userRepository;
    private PostRepository postRepository;

    public UserJPAController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/users")
//    public List<User> retrieveAllusers() {
//        return userRepository.findAll();
//    }
    public ResponseEntity retrieveAllusers() {
        List<User> users = userRepository.findAll();

        ResponseData responseData = ResponseData.builder()
                .count(users == null || users.isEmpty() ? 0 : users.size())
                .user(users)
                .build();

        EntityModel entityModel = EntityModel.of(responseData);
        WebMvcLinkBuilder linTo = linkTo(methodOn(this.getClass()).retrieveAllusers());
        entityModel.add(linTo.withSelfRel());

        return ResponseEntity.ok(entityModel);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();


        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity retrieveUsersById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        }

        EntityModel entityModel = EntityModel.of(user.get());

        WebMvcLinkBuilder linTo = linkTo(methodOn(this.getClass()).retrieveAllusers());
        entityModel.add(linTo.withRel("all-users"));    // all-users -> http://localhost:8088/users

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        }

        return user.get().getPosts();

    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        }

        post.setUser(user.get());

        postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }
}
