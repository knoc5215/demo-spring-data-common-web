package me.jumen.demospringdatacommonweb.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Test
    public void getPost() throws Exception {
        Post post = new Post();
        post.setTitle("jpa");
        postRepository.save(post);

        mockMvc.perform(get("/posts/" + post.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("jpa"))
        ;
    }

    @Test
    public void getPostWithPageable() throws Exception {
        this.createPosts();

        mockMvc.perform(get("/posts")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,desc")
                        .param("sort", "title")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", is("post 99")))
        ;
    }

    @Test
    public void getPostWithHATEOAS() throws Exception {
        this.createPosts();

        mockMvc.perform(get("/posts")
                .param("page", "2")
                .param("size", "10")
                .param("sort", "id,desc")
                .param("sort", "title")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.postList[0].title", is("post 79")))
        ;
    }

    public void createPosts() {
        for (int i = 0; i < 100; i++) {
            Post post = new Post();
            post.setTitle("post " + i);
            postRepository.save(post);
        }
    }

}