package me.geon.artice.apiCaller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.geon.artice.ApiResponse;
import me.geon.artice.MockApiCaller;
import me.geon.artice.service.request.ArticleCreateRequest;
import me.geon.artice.service.response.ArticleResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleApiCaller extends MockApiCaller {

    public ArticleApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<ArticleResponse> createArticle(ArticleCreateRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/v1/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8");

        return objectMapper.readValue(
                mockMvc.perform(builder)
                        .andExpect(status().is(expectedStatus))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
                }
        );
    }
}
