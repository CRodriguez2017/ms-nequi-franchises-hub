package co.com.bancolombia.jpa.adapter;

import co.com.bancolombia.jpa.entity.FranchiseEntity;
import co.com.bancolombia.jpa.helper.AdapterOperations;
import co.com.bancolombia.jpa.repository.FranchiesRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class FranchiseAdapter extends AdapterOperations<Franchise, FranchiseEntity, Integer, FranchiesRepository>
        implements FranchiseRepository {

    public FranchiseAdapter(FranchiesRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
    }


    @Override
    public Flux<Franchise> getAllFranchises() {
        return Flux.fromIterable(findAll());
    }

    @Override
    public Mono<Franchise> getFranchiseById(Integer brandId) {
        return Mono.justOrEmpty(findById(brandId));
    }

    @Override
    public Mono<Franchise> createFranchise(String name) {
        return Mono.defer(() -> Mono.just(
                save(Franchise.builder()
                        .name(name)
                        .build())
        ));
    }

    @Override
    public Mono<Franchise> updateFranchise(Integer franchiseId, String name) {
        return Mono.defer(() -> Mono.just(
                save(Franchise.builder()
                        .id(franchiseId)
                        .name(name)
                        .build())
        ));
    }

}
