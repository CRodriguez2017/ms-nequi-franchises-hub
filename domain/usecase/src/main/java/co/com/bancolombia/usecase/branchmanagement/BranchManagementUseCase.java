package co.com.bancolombia.usecase.branchmanagement;

import co.com.bancolombia.exceptions.BadRequestException;
import co.com.bancolombia.exceptions.NotFoundException;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchisemanagement.FranchiseManagementUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchManagementUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseManagementUseCase franchiseManagementUseCase;

    public Mono<Branch> createBranch(String name, Integer franchiseId) {
        return franchiseManagementUseCase.getFranchiseById(franchiseId)
                .flatMap(franchise -> branchRepository.createBranch(Branch.builder()
                        .name(name)
                        .franchise(franchise)
                        .build()));
    }

    public Mono<Branch> updateBranch(Integer franchiseId, Integer branchId, String name) {
        return getBranchByIdAndFranchiseId(franchiseId, branchId)
                .flatMap(branch -> branchRepository.updateBranch(branch.toBuilder().name(name).build()));
    }

    public Mono<Branch> getBranchByIdAndFranchiseId(Integer franchiseId, Integer branchId) {
        return branchRepository.getBranchById(branchId)
                .filter(branch -> franchiseId.equals(branch.getFranchise().getId()))
                .switchIfEmpty(Mono.error(new BadRequestException("No existe la sucursal")));
    }

    public Flux<Branch> getBranchesByFranchiseId(Integer franchiseId){
        return franchiseManagementUseCase.getFranchiseById(franchiseId)
                .map(Franchise::getId)
                .flatMapMany(branchRepository::getBranchByFranchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("No existe la sucursal")));
    }
}
