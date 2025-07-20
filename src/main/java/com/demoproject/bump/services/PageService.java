package com.demoproject.bump.services;

import com.demoproject.bump.dtos.PageRequest;
import com.demoproject.bump.dtos.PageResponse;
import com.demoproject.bump.dtos.PostRequest;
import com.demoproject.bump.dtos.PostResponse;

public interface PageService {
    PageResponse create(PageRequest page);
    PageResponse readByTitle(String title);
    PageResponse update(PageRequest page, String title);
    void delete(String title);

    PostResponse createPost(PostRequest post);
    PageResponse deletePost(Long idPost);

}
