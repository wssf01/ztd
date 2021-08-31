package com.bike.ztd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConf {

    @Value("${swagger.show}")
    private boolean swaggerShow;

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization")
                .description("Authorization Token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();

        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerShow)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bike.ztd"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(token())
                .securityContexts(contexts());
//                .globalOperationParameters(pars);
    }

    private List<ApiKey> token() {
        List<ApiKey> keys = new ArrayList<>();
        keys.add(new ApiKey("Authorization", "Authorization", "header"));
        return keys;
    }

    private List<SecurityContext> contexts() {
        List<SecurityContext> contexts = new ArrayList<>();
        contexts.add(SecurityContext.builder()
                .securityReferences(auth())
                .forPaths(PathSelectors.any())
                .build());
        return contexts;
    }


    private List<SecurityReference> auth() {
        List<SecurityReference> auths = new ArrayList<>();
        auths.add(new SecurityReference("Authorization", new AuthorizationScope[0]));
        return auths;
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Ztd RESTful APIs")
                .description("")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .build();
    }
}
