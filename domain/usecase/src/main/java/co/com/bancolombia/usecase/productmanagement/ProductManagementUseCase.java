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

import java.util.Comparator;

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

    public Mono<Product> updateProduct(UpdateProduct command) {
        return getProductById(command.getProductId())
                .flatMap(product ->  checkIfProductBelongToSite(
                        product, command.getBrandId(), command.getSiteId()))
                .map(product -> product.toBuilder()
                        .name(command.getName())
                        .stock(command.getStock())
                        .build())
                .flatMap(productRepository::updateProduct);
    }

    public Mono<Void> deleteProduct(Integer brandId, Integer siteId, Integer productId) {
        return getProductById(productId)
                .flatMap(product -> this.checkIfProductBelongToSite(product, brandId, siteId))
                .flatMap(product -> productRepository.deleteProduct(productId));
    }

    public Flux<Product> getTopProductsByFranchiseId(Integer franchiseId) {
        return branchManagementUseCase.getBranchesByFranchiseId(franchiseId)
                .flatMap(branch -> getTopProductByBranchId(franchiseId, branch.getId()))
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontraron productos")));
    }

    public Flux<Product> getProductsByFranchiseIdAndBranchId(Integer franchiseId, Integer branchId) {
        return productRepository.getProductsByBranchId(branchId)
                .filter(product -> franchiseId.equals(product.getBranch().getFranchise().getId()));
    }

    private Mono<Product> getProductById(Integer productId) {
        return productRepository.getProductById(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontro el producto")));
    }

    private Mono<Product> checkIfProductBelongToSite(Product product, Integer franchiseId, Integer branchId) {
        return Mono.just(product)
                .filter(p -> branchId.equals(p.getBranch().getId()))
                .filter(p -> franchiseId.equals(p.getBranch().getFranchise().getId()))
                .switchIfEmpty(Mono.error(new BadRequestException("No existe en la sucursal")));
    }

    private Mono<Product> getTopProductByBranchId(Integer franchiseId, Integer siteId) {
        return getProductsByFranchiseIdAndBranchId(franchiseId, siteId)
                .collectSortedList(Comparator.comparingInt(Product::getStock).reversed())
                .filter(products -> !products.isEmpty())
                .map(products -> products.get(0));
    }
}
