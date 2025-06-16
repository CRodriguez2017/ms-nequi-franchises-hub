package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.BranchHandler;
import co.com.bancolombia.api.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouter {
    @Bean
    public RouterFunction<ServerResponse> routerProduct(ProductHandler handler) {
        return nest(path("/api/v1"),
                route(GET("/franchises/{franchiseId}/branches/{branchId}/products"), handler::getProductsByBranchId)
                        .andRoute(POST("/franchises/{franchiseId}/branches/{branchId}/products"), handler::createProduct)
                        .andRoute(PUT("/franchises/{franchiseId}/branches/{branchId}/products/{productId}"), handler::updateProduct)
                        .andRoute(DELETE("/franchises/{franchiseId}/branches/{branchId}/products/{productId}"), handler::deleteProduct)
                        .andRoute(GET("/franchises/{franchiseId}/top-products"), handler::getTopProductsByFranchiseId));
    }
}
