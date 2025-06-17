package co.com.bancolombia.usecase.franchisemanagement;

import co.com.bancolombia.exceptions.NotFoundException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.usecase.branchmanagement.BranchManagementUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class FranchiseManagementUseCase {

    private final FranchiseRepository franchiseRepository;
    private final Logger logger = Logger.getLogger(FranchiseManagementUseCase.class.getName());

    public Mono<Franchise> createFranchise(String name) {

        return franchiseRepository.createFranchise(name)
                .doOnSuccess(branch -> logger.info("Se termina exitosamente el metodo createFranchise"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo createFranchise, pero termina en error:" + throwable.getMessage()));
    }

    public Mono<Franchise> updateFranchise(Integer franchiseId, String name) {
        return getFranchiseById(franchiseId)
                .flatMap(brand ->  franchiseRepository.updateFranchise(franchiseId, name))
                .doOnRequest(value -> logger.info("Se ingresa al metodo updateFranchise"))
                .doOnSuccess(branch -> logger.info("Se termina exitosamente el metodo updateFranchise"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo updateFranchise, pero termina en error:" + throwable.getMessage()));
    }

    public Flux<Franchise> getAllFranchises(){
        return franchiseRepository.getAllFranchises()
                .switchIfEmpty(Mono.error(new NotFoundException("Franquicia no encontrada")))
                .doOnRequest(value -> logger.info("Se ingresa al metodo getAllFranchises"))
                .doOnComplete(() -> logger.info("Se termina exitosamente el metodo getAllFranchises"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo getAllFranchises, pero termina en error: " + throwable.getMessage()));
    }

    public Mono<Franchise> getFranchiseById(Integer franchiseId) {
        return franchiseRepository.getFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franquicia no encontrada")))
                .doOnRequest(value -> logger.info("Se ingresa al metodo getFranchiseById"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo getFranchiseById, pero termina en error:" + throwable.getMessage()));
    }


}
