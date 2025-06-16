package co.com.bancolombia.model.branch.gateways;

import co.com.bancolombia.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Branch> createBranch(Branch branch);
    Mono<Branch> updateBranch(Branch branch);
    Mono<Branch> getBranchById(Integer branchId);
    Flux<Branch> getBranchByFranchiseId(Integer franchiseId);
}
