package co.com.bancolombia.usecase.productmanagement;

import co.com.bancolombia.exceptions.BadRequestException;
import co.com.bancolombia.exceptions.NotFoundException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.dto.UpdateProduct;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.branchmanagement.BranchManagementUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductManagementUseCase {

    private final BranchManagementUseCase branchManagementUseCase;
    private final ProductRepository productRepository;

    public Mono<Product> createProduct(Integer franchiseId, Integer branchId, String productName, Integer stock) {
        return branchManagementUseCase.getBranchByIdAndFranchiseId(franchiseId, branchId)
                .map(branch -> Product.builder()
                        .name(productName)
                        .stock(stock)
                        .branch(branch)
                        .build())
                .flatMap(productRepository::createProduct);
    }

    public Mono<Product> updateProduct(UpdateProduct updateProduct) {
        return getProductById(updateProduct.getProductId())
                .flatMap(product -> validateProductBelongsToBranch(product, updateProduct.getFranchiseId(), updateProduct.getBranchId())
                        .thenReturn(product)) // ← importante, retornamos el mismo producto si pasa la validación
                .map(product -> product.toBuilder()
                        .name(updateProduct.getName())
                        .stock(updateProduct.getStock())
                        .build())
                .flatMap(productRepository::updateProduct);
    }

    public Mono<Void> deleteProduct(Integer franchiseId, Integer branchId, Integer productId) {
        return getProductById(productId)
                .flatMap(product -> validateProductBelongsToBranch(product, franchiseId, branchId)
                        .then(productRepository.deleteProduct(productId)));
    }

    public Flux<Product> getProductsByFranchiseIdAndBranchId(Integer franchiseId, Integer branchId) {
        return productRepository.getProductsByBranchId(branchId)
                .filter(product -> franchiseId.equals(product.getBranch().getFranchise().getId()));
    }

    public Flux<Product> getTopProductsByFranchiseId(Integer franchiseId) {
        return branchManagementUseCase.getBranchesByFranchiseId(franchiseId)
                .flatMap(branch -> getTopProductByBranchId(franchiseId, branch.getId()))
                .collectList()
                .flatMapMany(products -> {
                    if (products.isEmpty()) {
                        return Flux.error(new NotFoundException("No se encontraron productos"));
                    }
                    return Flux.fromIterable(products);
                });
    }

    private Mono<Product> getProductById(Integer productId) {
        return productRepository.getProductById(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontro el producto")));
    }


    private Mono<Product> getTopProductByBranchId(Integer franchiseId, Integer branchId) {
        return getProductsByFranchiseIdAndBranchId(franchiseId, branchId)
                .reduce((p1, p2) -> p1.getStock() >= p2.getStock() ? p1 : p2);
    }

    private Mono<Void> validateProductBelongsToBranch(Product product, Integer franchiseId, Integer branchId) {
        boolean isValid = branchId.equals(product.getBranch().getId()) &&
                franchiseId.equals(product.getBranch().getFranchise().getId());
        return isValid ? Mono.empty() : Mono.error(new BadRequestException("No existe en la sucursal"));
    }

}
