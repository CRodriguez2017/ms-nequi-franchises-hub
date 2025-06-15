package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/usecase/path"), handler::listenGETUseCase)
                .andRoute(POST("/api/v1/usecase/otherpath"), handler::listenPOSTUseCase)
                .and(route(GET("/api/v1/otherusercase/path"), handler::listenGETOtherUseCase));
    }
}
