package co.com.bancolombia.model.product.gateways;

import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> createProduct(Product product);
    Mono<Product> updateProduct(Product product);
    Mono<Void> deleteProduct(Integer productId);
    Mono<Product> getProductById(Integer productId);
    Flux<Product> getProductsByBranchId(Integer branchId);
}
