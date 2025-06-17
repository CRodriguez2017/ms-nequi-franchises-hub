package co.com.bancolombia.usecase.productmanagement;

import co.com.bancolombia.exceptions.BadRequestException;
import co.com.bancolombia.exceptions.NotFoundException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.dto.UpdateProduct;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.branchmanagement.BranchManagementUseCase;
import co.com.bancolombia.usecase.franchisemanagement.FranchiseManagementUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class ProductManagementUseCase {

    private final BranchManagementUseCase branchManagementUseCase;
    private final ProductRepository productRepository;
    private final Logger logger = Logger.getLogger(ProductManagementUseCase.class.getName());

    public Mono<Product> createProduct(Integer franchiseId, Integer branchId, String productName, Integer stock) {
        return branchManagementUseCase.getBranchByIdAndFranchiseId(franchiseId, branchId)
                .map(branch -> Product.builder()
                        .name(productName)
                        .stock(stock)
                        .branch(branch)
                        .build())
                .flatMap(productRepository::createProduct)
                .doOnSuccess(branch -> logger.info("Se termina exitosamente el metodo createProduct"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo createProduct, pero termina en error:" + throwable.getMessage()));
    }

    public Mono<Product> updateProduct(UpdateProduct updateProduct) {
        return getProductById(updateProduct.getProductId())
                .flatMap(product -> validateProductBelongsToBranch(product, updateProduct.getFranchiseId(), updateProduct.getBranchId())
                        .thenReturn(product)) // ← importante, retornamos el mismo producto si pasa la validación
                .map(product -> product.toBuilder()
                        .name(updateProduct.getName())
                        .stock(updateProduct.getStock())
                        .build())
                .flatMap(productRepository::updateProduct)
                .doOnRequest(value -> logger.info("Se ingresa al metodo updateProduct"))
                .doOnSuccess(branch -> logger.info("Se termina exitosamente el metodo updateProduct"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo updateProduct, pero termina en error:" + throwable.getMessage()));
    }

    public Mono<Void> deleteProduct(Integer franchiseId, Integer branchId, Integer productId) {
        return getProductById(productId)
                .flatMap(product -> validateProductBelongsToBranch(product, franchiseId, branchId)
                        .then(productRepository.deleteProduct(productId)))
                .doOnRequest(value -> logger.info("Se ingresa al metodo deleteProduct"))
                .doOnSuccess(branch -> logger.info("Se termina exitosamente el metodo deleteProduct"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo deleteProduct, pero termina en error:" + throwable.getMessage()));
    }

    public Flux<Product> getProductsByFranchiseIdAndBranchId(Integer franchiseId, Integer branchId) {
        return productRepository.getProductsByBranchId(branchId)
                .filter(product -> franchiseId.equals(product.getBranch().getFranchise().getId()))
                .doOnRequest(value -> logger.info("Se ingresa al metodo getProductsByFranchiseIdAndBranchId"))
                .doOnComplete(() -> logger.info("Se termina exitosamente el metodo getProductsByFranchiseIdAndBranchId"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo getProductsByFranchiseIdAndBranchId, pero termina en error: " + throwable.getMessage()));
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
                })
                .doOnRequest(value -> logger.info("Se ingresa al metodo getTopProductsByFranchiseId"))
                .doOnComplete(() -> logger.info("Se termina exitosamente el metodo getTopProductsByFranchiseId"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo getTopProductsByFranchiseId, pero termina en error: " + throwable.getMessage()));
    }

    private Mono<Product> getProductById(Integer productId) {
        return productRepository.getProductById(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontro el producto")))
                .doOnRequest(value -> logger.info("Se ingresa al metodo getProductById"))
                .doOnSuccess(branch -> logger.info("Se termina exitosamente el metodo getProductById"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo getProductById, pero termina en error:" + throwable.getMessage()));
    }


    private Mono<Product> getTopProductByBranchId(Integer franchiseId, Integer branchId) {
        return getProductsByFranchiseIdAndBranchId(franchiseId, branchId)
                .reduce((p1, p2) -> p1.getStock() >= p2.getStock() ? p1 : p2)
                .doOnRequest(value -> logger.info("Se ingresa al metodo getTopProductByBranchId"))
                .doOnSuccess(branch -> logger.info("Se termina exitosamente el metodo getTopProductByBranchId"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo getTopProductByBranchId, pero termina en error:" + throwable.getMessage()));
    }

    private Mono<Void> validateProductBelongsToBranch(Product product, Integer franchiseId, Integer branchId) {
        boolean isValid = branchId.equals(product.getBranch().getId()) &&
                franchiseId.equals(product.getBranch().getFranchise().getId());
        return isValid ? Mono.empty() : Mono.error(new BadRequestException("No existe en la sucursal"));
    }

}
