package com.demoproject.bump.controllers;


import com.demoproject.bump.dtos.PageRequest;
import com.demoproject.bump.dtos.PageResponse;
import com.demoproject.bump.dtos.PostRequest;
import com.demoproject.bump.dtos.PostResponse;
import com.demoproject.bump.services.PageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path="page")
@AllArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping(path = "{title}")
    public ResponseEntity<PageResponse> getPage(@PathVariable String title) {

        return ResponseEntity.ok(this.pageService.readByTitle(title));
    }

    @PostMapping //use to create data
    public ResponseEntity<?> postPage(@RequestBody PageRequest request) {
        request.setTitle(this.normalizeTitle(request.getTitle()));
        final var uri = this.pageService.create(request).getTitle();
        return ResponseEntity.created(URI.create(uri)).build();
    }

    @PutMapping(path = "{title}")
    public ResponseEntity<PageResponse> updatePage(@PathVariable String title, @RequestBody PageRequest req) {

        return ResponseEntity.ok(this.pageService.update(req, title));
    }

    @DeleteMapping(path = "{title}")
    public ResponseEntity<Void> deletePage(@PathVariable String title) {
        this.pageService.delete(title);
        return ResponseEntity.noContent().build();
    }

    private String normalizeTitle(String title) {
        if (title.contains(" ")) {
            return title.replaceAll(" ", "-");
        } else {
            return title;
        }
    }


    @PostMapping(path = "{title}/post")
    public ResponseEntity<PageResponse> postPost(@RequestBody PostRequest request, @PathVariable String title) {
        return ResponseEntity.ok(this.pageService.createPost(request, title));
    }


    @DeleteMapping(path = "{title}/post/{idPost}")
    public ResponseEntity<Void> deletePost( @PathVariable  Long idPost, @PathVariable String title) {
        this.pageService.deletePost(idPost, title);
        return ResponseEntity.noContent().build();
    }


}
