package com.demo.api.commons.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghw on 20180814.
 */
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Configuration
@Profile({"development","test"})
public class Swagger2Config {

    @Bean
    public Docket createAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("API")
                .produces(Sets.newHashSet("application/json"))
                .consumes(Sets.newHashSet("application/json"))
                .protocols(Sets.newHashSet("http", "https"))
                .forCodeGeneration(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.demo.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemesAPI())
                .securityContexts(securityContextsAPI());

    }

    @Bean
    public Docket createBMS() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("BMS")
                .produces(Sets.newHashSet("application/json"))
                .consumes(Sets.newHashSet("application/json"))
                .protocols(Sets.newHashSet("http", "https"))
                .forCodeGeneration(true)
                .apiInfo(bmsInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.demo.bms.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemesBMS())
                .securityContexts(securityContextsBMS());

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API接口")
                .version("1.0")
                .build();
    }

    private ApiInfo bmsInfo() {
        return new ApiInfoBuilder()
                .title("BMS接口")
                .version("1.0")
                .build();
    }

    private List<ApiKey> securitySchemesAPI() {
        List<ApiKey> list =  new ArrayList<>();
        list.add(new ApiKey("用户Token", "x-authorization", "header"));
        return list;
    }

    private List<SecurityContext> securityContextsAPI() {
        List<SecurityContext> list =  new ArrayList<>();
        SecurityContext securityContext = SecurityContext.builder().securityReferences(defaultAuthAPI()).forPaths(PathSelectors.regex("/.*")).build();
        list.add(securityContext);
        return list;
    }

    private List<SecurityReference> defaultAuthAPI() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> list = new ArrayList<>();
        list.add(new SecurityReference("Authorization", authorizationScopes));
        return list;
    }

    private List<ApiKey> securitySchemesBMS() {
        List<ApiKey> list =  new ArrayList<>();
        list.add(new ApiKey("用户Token", "x-authorization-bms", "header"));
        return list;
    }

    private List<SecurityContext> securityContextsBMS() {
        List<SecurityContext> list =  new ArrayList<>();
        SecurityContext securityContext = SecurityContext.builder().securityReferences(defaultAuthBMS()).forPaths(PathSelectors.regex("/.*")).build();
        list.add(securityContext);
        return list;
    }

    private List<SecurityReference> defaultAuthBMS() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> list = new ArrayList<>();
        list.add(new SecurityReference("Authorization", authorizationScopes));
        return list;
    }
}
