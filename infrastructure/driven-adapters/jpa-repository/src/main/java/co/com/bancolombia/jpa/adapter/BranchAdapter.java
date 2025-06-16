package co.com.bancolombia.jpa.adapter;

import co.com.bancolombia.jpa.entity.BranchEntity;
import co.com.bancolombia.jpa.helper.AdapterOperations;
import co.com.bancolombia.jpa.repository.BranchesRepository;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class BranchAdapter extends AdapterOperations<Branch, BranchEntity, Integer, BranchesRepository>
        implements BranchRepository {

    public BranchAdapter(BranchesRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Branch.class));
    }

    @Override
    public Mono<Branch> createBranch(Branch site) {
        return Mono.defer(() -> Mono.just(save(site)));
    }

    @Override
    public Mono<Branch> updateBranch(Branch branch) {
        return Mono.defer(() -> Mono.just(save(branch)));
    }

    @Override
    public Mono<Branch> getBranchById(Integer branchId) {
        return Mono.justOrEmpty(findById(branchId));
    }

    @Override
    public Flux<Branch> getBranchByFranchiseId(Integer franchiseId) {
        return Flux.fromIterable(repository.findBranchesByFranchiseId(franchiseId))
                .map(this::toEntity);
    }

}
