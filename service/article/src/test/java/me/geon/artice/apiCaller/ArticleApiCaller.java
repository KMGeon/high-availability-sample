package me.geon.artice.apiCaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.geon.artice.config.WebTestConfigure;
import me.geon.artice.service.request.ArticleCreateRequest;
import me.geon.artice.service.response.ArticleResponse;
import me.geon.snowflake.ApiResponse;
import me.geon.snowflake.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ArticleApiCaller extends WebTestConfigure {

    public ArticleApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<ErrorResponse, ArticleResponse> createArticle_validData(ArticleCreateRequest request) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        if (response.getStatus() != HttpStatus.OK.value()) {
            ErrorResponse error = objectMapper.readValue(
                    response.getContentAsString(),
                    ErrorResponse.class
            );
            return ApiResponse.error(response.getStatus(), error);
        }

        ArticleResponse data = objectMapper.readValue(
                response.getContentAsString(),
                ArticleResponse.class
        );
        return ApiResponse.success(response.getStatus(), data);
    }
}
