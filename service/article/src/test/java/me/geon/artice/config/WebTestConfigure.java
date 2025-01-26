package me.geon.artice.config;

import me.geon.artice.apiCaller.ArticleApiCaller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import me.geon.artice.ArticleApplication;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
//@ActiveProfiles("dev")
@Transactional
@SpringBootTest(
        classes = {AppConfig.class, TestConfig.class, ArticleApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class WebTestConfigure {

    protected static final String URL = "http://localhost:9000/v1/articles";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected FixtureMonkey fixtureMonkey;

    protected ArticleApiCaller articleApiCaller;

    @Autowired
    private WebApplicationContext context;



    @BeforeEach
    public void setup() throws Exception {
        articleApiCaller = new ArticleApiCaller(mockMvc, objectMapper);
    }

}
