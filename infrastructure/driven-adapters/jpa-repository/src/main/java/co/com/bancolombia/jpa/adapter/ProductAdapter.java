package co.com.bancolombia.jpa.adapter;

import co.com.bancolombia.jpa.entity.ProductEntity;
import co.com.bancolombia.jpa.helper.AdapterOperations;
import co.com.bancolombia.jpa.repository.ProductsRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class ProductAdapter extends AdapterOperations<Product, ProductEntity, Integer, ProductsRepository>
        implements ProductRepository {

    public ProductAdapter(ProductsRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Product.class));
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        return Mono.defer(() -> Mono.just(save(product)));
    }

    public Mono<Product> updateProduct(Product product) {
        return Mono.defer(() -> Mono.just(save(product)));
    }

    @Override
    public Mono<Void> deleteProduct(Integer productId) {
        return Mono.fromRunnable(() -> repository.deleteById(productId));
    }

    @Override
    public Mono<Product> getProductById(Integer productId) {
        return Mono.justOrEmpty(findById(productId));
    }

    @Override
    public Flux<Product> getProductsByBranchId(Integer siteId) {
        return Flux.fromIterable(repository.findProductsByBranchId(siteId))
                .map(this::toEntity);
    }

}
