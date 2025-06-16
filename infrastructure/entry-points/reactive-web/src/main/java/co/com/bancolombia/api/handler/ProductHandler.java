package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.Branch.CreateBranchRequest;
import co.com.bancolombia.api.dto.Franchise.UpdateFranchiseRequest;
import co.com.bancolombia.api.dto.Product.ProductRequest;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.dto.UpdateProduct;
import co.com.bancolombia.usecase.branchmanagement.BranchManagementUseCase;
import co.com.bancolombia.usecase.productmanagement.ProductManagementUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.api.enums.Param.*;
import static co.com.bancolombia.api.utils.PathParamValidator.validateRequiredParam;
import static co.com.bancolombia.api.utils.RequestBodyValidator.validateBody;


@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final Validator validator;
    private final ProductManagementUseCase productManagementUseCase;

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        Integer franchiseId = validateRequiredParam(FRANCHISE_ID.getName(), serverRequest.pathVariable(FRANCHISE_ID.getName()));
        Integer branchId = validateRequiredParam(BRANCH_ID.getName(), serverRequest.pathVariable(BRANCH_ID.getName()));
        return validateBody(serverRequest.bodyToMono(ProductRequest.class), validator)
                .flatMap(createProductRequest -> productManagementUseCase.createProduct(
                        franchiseId, branchId, createProductRequest.getName(), createProductRequest.getStock()))
                .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }

    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
        Integer franchiseId = validateRequiredParam(FRANCHISE_ID.getName(), serverRequest.pathVariable(FRANCHISE_ID.getName()));
        Integer branchId = validateRequiredParam(BRANCH_ID.getName(), serverRequest.pathVariable(BRANCH_ID.getName()));
        Integer productId = validateRequiredParam(PRODUCT_ID.getName(), serverRequest.pathVariable(PRODUCT_ID.getName()));
        return validateBody(serverRequest.bodyToMono(ProductRequest.class), validator)
                .flatMap(updateProductRequest -> productManagementUseCase.updateProduct(
                        UpdateProduct.builder()
                                .franchiseId(franchiseId)
                                .branchId(branchId)
                                .productId(productId)
                                .name(updateProductRequest.getName())
                                .stock(updateProductRequest.getStock())
                                .build()))
                .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        Integer franchiseId = validateRequiredParam(FRANCHISE_ID.getName(), serverRequest.pathVariable(FRANCHISE_ID.getName()));
        Integer branchId = validateRequiredParam(BRANCH_ID.getName(), serverRequest.pathVariable(BRANCH_ID.getName()));
        Integer productId = validateRequiredParam(PRODUCT_ID.getName(), serverRequest.pathVariable(PRODUCT_ID.getName()));
        return productManagementUseCase.deleteProduct(franchiseId, branchId, productId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getTopProductsByFranchiseId(ServerRequest serverRequest) {
        Integer franchiseId = validateRequiredParam(FRANCHISE_ID.getName(), serverRequest.pathVariable(FRANCHISE_ID.getName()));
        return ServerResponse.ok().body(productManagementUseCase
                .getTopProductsByFranchiseId(franchiseId), Product.class);
    }

    public Mono<ServerResponse> getProductsByBranchId(ServerRequest serverRequest) {
        Integer franchiseId = validateRequiredParam(FRANCHISE_ID.getName(), serverRequest.pathVariable(FRANCHISE_ID.getName()));
        Integer branchId = validateRequiredParam(BRANCH_ID.getName(), serverRequest.pathVariable(BRANCH_ID.getName()));
        return ServerResponse.ok()
                .body(productManagementUseCase.getProductsByFranchiseIdAndBranchId(franchiseId, branchId), Product.class);
    }
}
