package com.rentzy.controller;

import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.model.dto.FilterParams;
import com.rentzy.model.dto.PostDTO;
import com.rentzy.model.dto.PostSearchDTO;
import com.rentzy.service.PostService;
import com.rentzy.utils.PageableUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(
            @Valid @ModelAttribute FilterParams filterParams) {
        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);
        Page<PostDTO> page = postService.getAllPosts(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostDTO>> searchPosts(
            @Valid @ModelAttribute PostSearchDTO criteria,
            @Valid @ModelAttribute FilterParams filterParams) {
        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);
        Page<PostDTO> page = postService.searchPosts(criteria, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable String id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) {
        PostDTO created = postService.createPost(postDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable String id,
            @Valid @RequestBody PostDTO postDTO) {
        try {
            PostDTO updated = postService.updatePost(id, postDTO);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
