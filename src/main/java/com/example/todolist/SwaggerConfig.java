package com.example.todolist;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("내일배움캠프 TodoList API").version("v0.1.0"));
    }

    @Bean
    public OpenApiCustomizer customizeOpenApi() {
        return openApi -> {
            // /authors/{id} 공통 경로 Parameter Swagger에서 이쁘게 보이게 수정
            openApi.getPaths().forEach((path, pathItem) -> {
                if ("/authors/{id}".equals(path)) {
                    pathItem.readOperations().forEach(operation -> {
                       operation.getParameters().stream()
                               .filter(p -> "id".equals(p.getName()) && "path".equals(p.getIn()))
                               .forEach(p -> {
                                   p.setDescription("작성자 ID (숫자 입력, 예: 1)");
                                   p.setSchema(new NumberSchema());
                               });
                    });
                }

                if ("/boards/{id}".equals(path)) {
                    pathItem.readOperations().forEach(operation -> {
                        operation.getParameters().stream()
                                .filter(p -> "id".equals(p.getName()) && "path".equals(p.getIn()))
                                .forEach(p -> {
                                    p.setDescription("게시글 ID (숫자 입력, 예: 1)");
                                    p.setSchema(new NumberSchema());
                                });
                    });
                }

                // 오히려 너무 지저분해 보여서 그냥 뺐음
//                pathItem.readOperations().forEach(operation -> {
//                    operation.getResponses().addApiResponse("400",
//                            new ApiResponse().$ref("#/components/responses/AllBadResponse")
//                    );
//                    operation.getResponses().addApiResponse("404",
//                            new ApiResponse().$ref("#/components/responses/AllBadResponse")
//                    );
//                    operation.getResponses().addApiResponse("409",
//                            new ApiResponse().$ref("#/components/responses/AllBadResponse")
//                    );
//                    operation.getResponses().addApiResponse("500",
//                            new ApiResponse().$ref("#/components/responses/AllBadResponse")
//                    );
//                });
            });
        };
    }
}
