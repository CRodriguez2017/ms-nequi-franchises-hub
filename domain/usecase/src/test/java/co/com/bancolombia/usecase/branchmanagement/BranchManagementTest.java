package co.com.bancolombia.usecase.branchmanagement;

import co.com.bancolombia.exceptions.BadRequestException;
import co.com.bancolombia.exceptions.NotFoundException;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchisemanagement.FranchiseManagementUseCase;
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
public class BranchManagementTest {

    @Mock
    BranchRepository branchRepository;

    @Mock
    FranchiseManagementUseCase franchiseManagementUseCase;

    @InjectMocks
    BranchManagementUseCase branchManagementUseCase;

    @Test
    void test1() {

        Franchise franchise = DataMock.createFakeFranchise(1, "Franchise 1");
        Branch branch = DataMock.createFakeBranch(1, "Branch 1", franchise);


        given(franchiseManagementUseCase.getFranchiseById(1)).willReturn(Mono.just(franchise));
        given(branchRepository.createBranch(any(Branch.class))).willReturn(Mono.just(branch));

        StepVerifier.create(branchManagementUseCase.createBranch("Branch 2", 1))
                .expectNext(branch)
                .verifyComplete();
    }

    @Test
    void test2() {

        Franchise franchise = DataMock.createFakeFranchise(1, "Franchise 1");
        Branch branch = DataMock.createFakeBranch(2, "OldBranch", franchise);
        Branch updatedBranch = branch.toBuilder().name("UpdatedBranch").build();


        given(branchRepository.getBranchById(2)).willReturn(Mono.just(branch));
        given(branchRepository.updateBranch(any(Branch.class))).willReturn(Mono.just(updatedBranch));

        StepVerifier.create(branchManagementUseCase.updateBranch(1, 2, "UpdatedBranch"))
                .expectNext(updatedBranch)
                .verifyComplete();
    }

    @Test
    void test3() {
        Franchise franchise = DataMock.createFakeFranchise(2, "Franchise 2");
        Branch branch = DataMock.createFakeBranch(3, "Branch", franchise);

        given(branchRepository.getBranchById(3)).willReturn(Mono.just(branch));

        StepVerifier.create(branchManagementUseCase.updateBranch(1, 3, "NewBranch"))
                .expectErrorMatches(e -> e instanceof BadRequestException &&
                        e.getMessage().equals(e.getMessage()))
                .verify();
    }

    @Test
    void test4() {
        Franchise franchise = DataMock.createFakeFranchise(1, "Franchise 1");
        List<Branch> branches = DataMock.getFakeBranches(franchise);

        given(franchiseManagementUseCase.getFranchiseById(1)).willReturn(Mono.just(franchise));
        given(branchRepository.getBranchByFranchiseId(1)).willReturn(Flux.fromIterable(branches));

        StepVerifier.create(branchManagementUseCase.getBranchesByFranchiseId(1))
                .expectNextSequence(branches)
                .verifyComplete();
    }

    @Test
    void test5() {
        Franchise franchise = DataMock.createFakeFranchise(1, "Franchise");

        given(franchiseManagementUseCase.getFranchiseById(1)).willReturn(Mono.just(franchise));
        given(branchRepository.getBranchByFranchiseId(1)).willReturn(Flux.empty());

        StepVerifier.create(branchManagementUseCase.getBranchesByFranchiseId(1))
                .expectErrorMatches(e -> e instanceof NotFoundException &&
                        e.getMessage().equals(e.getMessage()))
                .verify();
    }
}
