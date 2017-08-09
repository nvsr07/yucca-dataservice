package org.csi.yucca.adminapi.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
@Configuration
@ComponentScan
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {
 
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
//                .build().apiInfo(apiInfo());
        .build();
    }
     
//    private ApiInfo apiInfo() {
//        ApiInfo apiInfo = new ApiInfo("Rest API",
//                                        "Rest API Example",
//                                        "1",
//                                        "",
//                                        new Contact("Edwin", "http://edwin.baculsoft.con", "edwin@baculsoft.com"),
//                                        "Apache License",
//                                        "");
//        return apiInfo;
//    }
    
    
//    private ApiInfo apiInfo() {
//        ApiInfo apiInfo = new ApiInfo(
//          "My REST API",
//          "Some custom description of API.",
//          "API TOS",
//          "Terms of service",
//          "myeaddress@company.com",
//          "License of API",
//          "API license URL");
//        return apiInfo;
//    }
}
