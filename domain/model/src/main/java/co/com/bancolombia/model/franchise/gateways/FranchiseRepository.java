package co.com.bancolombia.model.franchise.gateways;

import co.com.bancolombia.model.franchise.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Flux<Franchise> getAllFranchises();
    Mono<Franchise> getFranchiseById(Integer franchiseId);
    Mono<Franchise> createFranchise(String name);
    Mono<Franchise> updateFranchise(Integer franchiseId, String name);
}
