package com.demoproject.bump.services;

import com.demoproject.bump.dtos.PageResponse;
import com.demoproject.bump.entities.PageEntity;
import com.demoproject.bump.entities.PostEntity;
import com.demoproject.bump.repositories.PageRepository;
import com.demoproject.bump.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PageServiceTest {

    @MockitoBean
    private PageRepository pageRepository;
    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private PageServiceImpl target;


    @Test
    void readByTitle_ShouldReturnPageResponse_WhenTitleExist() {
        String title = "Example Title";

        PostEntity postEntity = new PostEntity();
        postEntity.setImg("http://img");
        postEntity.setContent("Some content");
        postEntity.setDateCreation(LocalDateTime.MIN);

        PageEntity pageEntity = new PageEntity();

        pageEntity.setTitle(title);
        pageEntity.setDateCreation(LocalDateTime.MIN);
        pageEntity.setPosts(List.of(postEntity));

        given(pageRepository.findByTitle(title))
                .willReturn(Optional.of(pageEntity));

        PageResponse result =  target.readByTitle(title);

        System.out.println(result);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Example Title");
        assertThat(result.getPosts()).hasSize(1);

    }

    @Test
    void readByTitle_ShouldReturnPageResponse_WhenTitleNotExist() {
        String title = "Invalid";
        given(pageRepository.findByTitle(title))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> target.readByTitle(title))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title not found");


        verify(pageRepository).findByTitle(title);


    }
}
