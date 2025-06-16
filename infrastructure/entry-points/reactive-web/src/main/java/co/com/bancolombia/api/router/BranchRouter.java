package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.BranchHandler;
import co.com.bancolombia.api.handler.FranchiseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouter {
    @Bean
    public RouterFunction<ServerResponse> routerBranch(BranchHandler handler) {
        return nest(path("/api/v1"),
                route(GET("/franchises/{franchiseId}/branches"), handler::getBranchesByFranchiseId)
                        .andRoute(POST("/franchises/{franchiseId}/branches"), handler::createBranch)
                        .andRoute(PUT("/franchises/{franchiseId}/branches/{branchId}"), handler::updateBranch));
    }
}
