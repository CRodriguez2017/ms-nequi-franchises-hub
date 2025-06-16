package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.Franchise.FranchiseRequest;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchisemanagement.FranchiseManagementUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.api.enums.Param.FRANCHISE_ID;
import static co.com.bancolombia.api.utils.PathParamValidator.validateRequiredParam;
import static co.com.bancolombia.api.utils.RequestBodyValidator.validateBody;



    @Component
    @RequiredArgsConstructor
    public class FranchiseHandler {

        private final Validator validator;
        private final FranchiseManagementUseCase franchiseManagementUseCase;

        public Mono<ServerResponse> getAllFranchises(ServerRequest serverRequest) {
            return ServerResponse.ok()
                    .body(franchiseManagementUseCase.getAllFranchises(), Franchise.class);
        }

        public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {
            return validateBody(serverRequest.bodyToMono(FranchiseRequest.class), validator)
                    .map(FranchiseRequest::getName)
                    .flatMap(franchiseManagementUseCase::createFranchise)
                    .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise));
        }

        public Mono<ServerResponse> updateFranchise(ServerRequest serverRequest) {
            Integer brandId = validateRequiredParam(FRANCHISE_ID.getName(), serverRequest.pathVariable(FRANCHISE_ID.getName()));
            return validateBody(serverRequest.bodyToMono(FranchiseRequest.class), validator)
                    .map(FranchiseRequest::getName)
                    .flatMap(name -> franchiseManagementUseCase.updateFranchise(brandId, name))
                    .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise));
        }
    }
