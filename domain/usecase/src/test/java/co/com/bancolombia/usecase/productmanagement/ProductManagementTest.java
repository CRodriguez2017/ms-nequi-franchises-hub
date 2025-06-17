package co.com.bancolombia.usecase.productmanagement;

import co.com.bancolombia.exceptions.BadRequestException;
import co.com.bancolombia.exceptions.NotFoundException;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.dto.UpdateProduct;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.branchmanagement.BranchManagementUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import utils.DataMock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductManagementTest {

    @Mock
    BranchManagementUseCase branchManagementUseCase;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductManagementUseCase useCase;

    @Test
    void test1() {
        Franchise franchise = DataMock.createFakeFranchise(1, "Franchise");
        Branch branch = DataMock.createFakeBranch(1,"Branch", franchise);
        Product expectedProduct = DataMock.createFakeProduct(1, "Product", 10, branch);
        
        given(branchManagementUseCase.getBranchByIdAndFranchiseId(2, 1)).willReturn(Mono.just(branch));
        given(productRepository.createProduct(any())).willReturn(Mono.just(expectedProduct));
        
        StepVerifier.create(useCase.createProduct(2, 1, "Product", 10))
                .expectNext(expectedProduct)
                .verifyComplete();
    }


    @Test
    void test2() {
        Franchise franchise = DataMock.createFakeFranchise(1, "Franchise");
        Branch branch = DataMock.createFakeBranch(99, "Branch", franchise);
        Product product = DataMock.createFakeProduct(1, "Product", 10, branch);
        UpdateProduct updateProduct = new UpdateProduct(2, 1, 1, "Producto", 91);

        given(productRepository.getProductById(1)).willReturn(Mono.just(product));


        StepVerifier.create(useCase.updateProduct(updateProduct))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void test3() {
        Franchise franchise = DataMock.createFakeFranchise(2, "Franchise");
        Branch branch = DataMock.createFakeBranch(1,"Branch", franchise);
        Product product = DataMock.createFakeProduct(1, "Product", 10, branch);
        
        given(productRepository.getProductById(1)).willReturn(Mono.just(product));
        given(productRepository.deleteProduct(1)).willReturn(Mono.empty());
        
        StepVerifier.create(useCase.deleteProduct(2, 1, 1))
                .verifyComplete();
    }

    @Test
    void test4() {
        Franchise franchise = DataMock.createFakeFranchise(1, "Franchise");
        Branch branch = DataMock.createFakeBranch(1,"Branch", franchise);

        Product topProduct = DataMock.createFakeProduct(1, "Product", 50, branch);
        Product anyProduct = DataMock.createFakeProduct(2, "Product", 20, branch);

        List<Product> products = List.of(topProduct, anyProduct);
        
        given(branchManagementUseCase.getBranchesByFranchiseId(1)).willReturn(Flux.just(branch));

        given(productRepository.getProductsByBranchId(branch.getId()))
                .willReturn(Flux.fromIterable(products));
        
        StepVerifier.create(useCase.getTopProductsByFranchiseId(1))
                .expectNext(topProduct)
                .verifyComplete();
    }

    @Test
    void test5() {
        Franchise franchise = DataMock.createFakeFranchise(1, "Franchise");
        Branch branch = DataMock.createFakeBranch(1,"Branch", franchise);
        
        given(branchManagementUseCase.getBranchesByFranchiseId(2)).willReturn(Flux.just(branch));
        given(productRepository.getProductsByBranchId(1)).willReturn(Flux.empty());
        
        StepVerifier.create(useCase.getTopProductsByFranchiseId(2))
                .expectError(NotFoundException.class)
                .verify();
    }
}
