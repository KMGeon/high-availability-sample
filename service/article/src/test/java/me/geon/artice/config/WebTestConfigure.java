package me.geon.artice.config;

import me.geon.artice.apiCaller.ArticleApiCaller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import me.geon.artice.ArticleApplication;
import me.geon.artice.data.DataInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest(
        classes = {AppConfig.class, TestConfig.class, ArticleApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import({DataInitializer.class})
public abstract class WebTestConfigure {

    protected static final String URL = "http://localhost:9000/v1/articles";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected FixtureMonkey fixtureMonkey;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    protected static DataInitializer dataInitializer;

    protected ArticleApiCaller articleApiCaller;


    protected void setup() throws Exception {
        articleApiCaller = new ArticleApiCaller(mockMvc, objectMapper);
    }

}
