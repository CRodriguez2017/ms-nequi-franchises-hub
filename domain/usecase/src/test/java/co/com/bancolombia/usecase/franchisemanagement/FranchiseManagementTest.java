package co.com.bancolombia.usecase.franchisemanagement;

import co.com.bancolombia.exceptions.NotFoundException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
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

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FranchiseManagementTest {

    @Mock
    FranchiseRepository franchiseRepository;

    @InjectMocks
    FranchiseManagementUseCase franchiseManagementUseCase;

    @Test
    void test1() {

        List<Franchise> fakeFranchises = DataMock.getFakeFranchise();

        given(franchiseRepository.getAllFranchises())
                .willReturn(Flux.fromIterable(fakeFranchises));

        Flux<Franchise> result = franchiseManagementUseCase.getAllFranchises();

        StepVerifier.create(result)
                .expectNextSequence(fakeFranchises)
                .verifyComplete();
    }

    @Test
    void test2() {

        Franchise franchise = DataMock.createFakeFranchise(1, "Franchise 2");
        given(franchiseRepository.getFranchiseById(1))
                .willReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseManagementUseCase.getFranchiseById(1);

        StepVerifier.create(result)
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void test3() {
        given(franchiseRepository.getFranchiseById(2221)).willReturn(Mono.empty());


        StepVerifier.create(franchiseManagementUseCase.getFranchiseById(2221))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals(throwable.getMessage()))
                .verify();
    }

    @Test
    void test4() {
        String name = "Test";
        Franchise franchise = DataMock.createFakeFranchise(1, name);
        given(franchiseRepository.createFranchise(name)).willReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseManagementUseCase.createFranchise(name);

        StepVerifier.create(result)
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void test5() {
        Integer brandId = 1;
        String newName = "Updated Franchise";
        Franchise existingBrand = DataMock.createFakeFranchise(brandId, "Before Franchise");
        Franchise updatedBrand = DataMock.createFakeFranchise(brandId, newName);

        given(franchiseRepository.getFranchiseById(brandId)).willReturn(Mono.just(existingBrand));
        given(franchiseRepository.updateFranchise(brandId, newName)).willReturn(Mono.just(updatedBrand));

        Mono<Franchise> result = franchiseManagementUseCase.updateFranchise(brandId, newName);

        StepVerifier.create(result)
                .expectNext(updatedBrand)
                .verifyComplete();
    }

    @Test
    void test6() {
        Integer brandId = 231;
        String name = "Test 1";
        given(franchiseRepository.getFranchiseById(brandId)).willReturn(Mono.empty());

        StepVerifier.create(franchiseManagementUseCase.updateFranchise(brandId, name))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals(throwable.getMessage()))
                .verify();
    }
}
