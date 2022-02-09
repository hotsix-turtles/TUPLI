package hotsixturtles.tupli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
    // http://localhost:8080/swagger-ui/index.html
    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
//                .apis(RequestHandlerSelectors.basePackage("hotsixturtles.turtletube.controller")) // 이렇게 하면 controller 아래의 것들만 표시해준다.
                .apis(RequestHandlerSelectors.basePackage("hotsixturtles.tupli.api"))
//                .apis(RequestHandlerSelectors.any()) // 지금은 컨트롤러가 여러곳에 있으니 any() 로 주었습니다.
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("TUPLI API")
                .description("TUPLI API Reference for Developers")
                .termsOfServiceUrl("https://edu.ssafy.com") // 임시로 에듀싸피로 함
                .version("1.0.0")
                .build();
//                .license("SSAFY License")
//                .licenseUrl("ssafy@ssafy.com").version("1.0").build();
    }

}
