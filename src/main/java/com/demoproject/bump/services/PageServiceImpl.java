package com.demoproject.bump.services;

import com.demoproject.bump.dtos.PageRequest;
import com.demoproject.bump.dtos.PageResponse;
import com.demoproject.bump.dtos.PostRequest;
import com.demoproject.bump.dtos.PostResponse;
import com.demoproject.bump.entities.PageEntity;
import com.demoproject.bump.entities.PostEntity;
import com.demoproject.bump.exceptions.TitleNotValidException;
import com.demoproject.bump.repositories.PageRepository;
import com.demoproject.bump.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final UserRepository userRepository;

    @Override
    public PageResponse create(PageRequest page) {
        this.validTitle(page.getTitle());
        final var entity = new PageEntity();
        BeanUtils.copyProperties(page, entity);


        final var user = this.userRepository.findById(page.getUserId()).
                orElseThrow();

        entity.setUser(user);
        entity.setDateCreation(LocalDateTime.now());
        entity.setPosts(new ArrayList<>());

        var pageCreated = this.pageRepository.save(entity);
        final var response = new PageResponse();

        BeanUtils.copyProperties(pageCreated, response);

        return response;
    }

    @Override
    public PageResponse readByTitle(String title) {
        final var entityResponse = this.pageRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Title not found"));

        final var response = new PageResponse();
        BeanUtils.copyProperties(entityResponse, response);

        final List<PostResponse> posts = entityResponse.getPosts()
                .stream()
                .map(postEntity ->
                        PostResponse
                                .builder()
                                .img(postEntity.getImg())
                                .content(postEntity.getContent())
                                .dateCreation(postEntity.getDateCreation())
                                .build()
                ).toList();
        response.setPosts(posts);


        return response;
    }

    @Override
    public PageResponse update(PageRequest page, String title) {
        this.validTitle(page.getTitle());
        final var entityFromDB = this.pageRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Title not found"));

        entityFromDB.setTitle(page.getTitle());

        var pageUpdated = this.pageRepository.save(entityFromDB);
        final var response = new PageResponse();

        BeanUtils.copyProperties(pageUpdated, response);
        return response;
    }

    @Override
    public void delete(String title) {
        if (this.pageRepository.existsBytitle(title)) {
            log.info("Deleting page");
            this.pageRepository.deleteByTitle(title);
        } else {
            log.info("Error to delete");
            throw new IllegalArgumentException("Cant delete because title not exist");
        }


    }

    @Override
    public PageResponse createPost(PostRequest post, String title) {
        final var pageToUpdate = this.pageRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException(("Title not found")));

        final var postEntity = new PostEntity();
        BeanUtils.copyProperties(post, postEntity);
        pageToUpdate.addPost(postEntity);

        postEntity.setDateCreation(LocalDateTime.now());

        final var responseEntity = this.pageRepository.save(pageToUpdate);

        final  var response = new PageResponse();
        BeanUtils.copyProperties(responseEntity, response);

        List<PostResponse> postResponses = responseEntity.getPosts()
                .stream()
                .map(postE -> PostResponse
                        .builder()
                        .img(postE.getImg())
                        .content(postE.getContent())
                        .dateCreation(postE.getDateCreation())
                        .build()).toList();
        response.setPosts(postResponses);


        return response;
    }

    @Override
    public void deletePost(Long idPost, String title) {
        final var pageToUpdate = this.pageRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException(("Title not found")));

       final var postToDelete = pageToUpdate.getPosts()
                .stream()
                .filter(post -> post.getId().equals(idPost))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("post id not found"));

       pageToUpdate.removePost(postToDelete);

       this.pageRepository.save(pageToUpdate);
    }

    private void validTitle(String title) {
        if(title.contains("*****")) {
            throw new TitleNotValidException("Title cant contain bad words");
        }
    }
}
