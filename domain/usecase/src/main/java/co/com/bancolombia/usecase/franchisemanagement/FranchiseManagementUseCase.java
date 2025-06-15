package co.com.bancolombia.usecase.franchisemanagement;

import co.com.bancolombia.exceptions.NotFoundException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseManagementUseCase {

    private final FranchiseRepository franchiseRepository;

    public Flux<Franchise> getAllFranchises(){
        return franchiseRepository.getAllFranchises()
                .switchIfEmpty(Mono.error(new NotFoundException("Franquicia no encontrada")));
    }

    public Mono<Franchise> getFranchiseById(Integer franchiseId) {
        return franchiseRepository.getFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franquicia no encontrada")));
    }

    public Mono<Franchise> createFranchise(String name) {
        return franchiseRepository.createFranchise(name);
    }

    public Mono<Franchise> updateFranchise(Integer franchiseId, String name) {
        return getFranchiseById(franchiseId)
                .flatMap(brand ->  franchiseRepository.updateFranchise(franchiseId, name));
    }
}
