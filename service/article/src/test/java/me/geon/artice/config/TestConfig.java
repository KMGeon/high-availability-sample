package me.geon.artice.config;


import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BeanArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.jqwik.JavaTypeArbitraryGenerator;
import com.navercorp.fixturemonkey.api.jqwik.JqwikPlugin;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.arbitraries.StringArbitrary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class TestConfig {


    @Bean
    public FixtureMonkey getFullAutoFixtureMonkey(){
        return FixtureMonkey.builder()
                .objectIntrospector(new FailoverIntrospector(Arrays.asList(
                        FieldReflectionArbitraryIntrospector.INSTANCE,
                        BeanArbitraryIntrospector.INSTANCE
                )))
                .defaultNotNull(true)
                .plugin(
                        new JqwikPlugin()
                                .javaTypeArbitraryGenerator(new JavaTypeArbitraryGenerator() {
                                    @Override
                                    public StringArbitrary strings() {
                                        return Arbitraries.strings()
                                                .ofMinLength(3)
                                                .ofMaxLength(10);
                                    }
                                })
                )
                .build();
    }
}
