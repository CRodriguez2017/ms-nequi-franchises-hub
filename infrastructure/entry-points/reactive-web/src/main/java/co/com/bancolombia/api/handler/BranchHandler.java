package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.Branch.BranchRequest;
import co.com.bancolombia.api.dto.Franchise.FranchiseRequest;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.usecase.branchmanagement.BranchManagementUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.api.enums.Param.BRANCH_ID;
import static co.com.bancolombia.api.enums.Param.FRANCHISE_ID;
import static co.com.bancolombia.api.utils.PathParamValidator.validateRequiredParam;
import static co.com.bancolombia.api.utils.RequestBodyValidator.validateBody;


@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final Validator validator;
    private final BranchManagementUseCase branchManagementUseCase;

    public Mono<ServerResponse> getBranchesByFranchiseId(ServerRequest serverRequest) {
        Integer franchiseId = validateRequiredParam(FRANCHISE_ID.getName(), serverRequest.pathVariable(FRANCHISE_ID.getName()));
        return ServerResponse.ok()
                .body(branchManagementUseCase.getBranchesByFranchiseId(franchiseId), Branch.class);
    }

    public Mono<ServerResponse> createBranch(ServerRequest serverRequest) {
        Integer franchiseId = validateRequiredParam(FRANCHISE_ID.getName(), serverRequest.pathVariable(FRANCHISE_ID.getName()));
        return validateBody(serverRequest.bodyToMono(BranchRequest.class), validator)
                .map(BranchRequest::getName)
                .flatMap(branchName -> branchManagementUseCase.createBranch(branchName, franchiseId))
                .flatMap(branch -> ServerResponse.ok().bodyValue(branch));
    }

    public Mono<ServerResponse> updateBranch(ServerRequest serverRequest) {
        Integer franchiseId = validateRequiredParam(FRANCHISE_ID.getName(), serverRequest.pathVariable(FRANCHISE_ID.getName()));
        Integer branchId = validateRequiredParam(BRANCH_ID.getName(), serverRequest.pathVariable(BRANCH_ID.getName()));
        return validateBody(serverRequest.bodyToMono(FranchiseRequest.class), validator)
                .map(FranchiseRequest::getName)
                .flatMap(branchName -> branchManagementUseCase.updateBranch(franchiseId, branchId, branchName))
                .flatMap(branch -> ServerResponse.ok().bodyValue(branch));
    }
}
