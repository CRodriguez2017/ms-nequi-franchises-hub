package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.FranchiseHandler;
import co.com.bancolombia.api.handler.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranchiseRouter {
    @Bean
    public RouterFunction<ServerResponse> routerBrand(FranchiseHandler handler) {
        return nest(path("/api/v1"),
                route(GET("/franchises"), handler::getAllFranchises)
                        .andRoute(POST("/franchises"), handler::createFranchise)
                        .andRoute(PUT("/franchises/{franchiseId}"), handler::updateFranchise));
    }
}
