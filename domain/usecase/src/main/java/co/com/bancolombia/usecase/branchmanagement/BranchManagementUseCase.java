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

import java.util.logging.Logger;

@RequiredArgsConstructor
public class BranchManagementUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseManagementUseCase franchiseManagementUseCase;
    private final Logger logger = Logger.getLogger(BranchManagementUseCase.class.getName());

    public Mono<Branch> createBranch(String name, Integer franchiseId) {
        return franchiseManagementUseCase.getFranchiseById(franchiseId)
                .flatMap(franchise -> branchRepository.createBranch(Branch.builder()
                        .name(name)
                        .franchise(franchise)
                        .build())
                ).doOnRequest(value -> logger.info("Se ingresa al metodo createBranch"))
                .doOnSuccess(branch -> logger.info("Se termina exitosamente el metodo createBranch"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo createBranch, pero termina en error:" + throwable.getMessage()));
    }

    public Mono<Branch> updateBranch(Integer franchiseId, Integer branchId, String name) {
        return getBranchByIdAndFranchiseId(franchiseId, branchId)
                .flatMap(branch -> branchRepository.updateBranch(branch.toBuilder().name(name).build()))
                .doOnRequest(value -> logger.info("Se ingresa al metodo updateBranch"))
                .doOnSuccess(branch -> logger.info("Se termina exitosamente el metodo updateBranch"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo updateBranch, pero termina en error:" + throwable.getMessage()));
    }

    public Mono<Branch> getBranchByIdAndFranchiseId(Integer franchiseId, Integer branchId) {
        return branchRepository.getBranchById(branchId)
                .filter(branch -> franchiseId.equals(branch.getFranchise().getId()))
                .switchIfEmpty(Mono.error(new BadRequestException("No existe la sucursal")))
                .doOnRequest(value -> logger.info("Se ingresa al metodo getBranchByIdAndFranchiseId"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo getBranchByIdAndFranchiseId, pero termina en error:" + throwable.getMessage()));
    }

    public Flux<Branch> getBranchesByFranchiseId(Integer franchiseId){
        return franchiseManagementUseCase.getFranchiseById(franchiseId)
                .map(Franchise::getId)
                .flatMapMany(branchRepository::getBranchByFranchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("No existe la sucursal")))
                .doOnRequest(value -> logger.info("Se ingresa al metodo getBranchesByFranchiseId"))
                .doOnComplete(() -> logger.info("Se termina exitosamente el metodo getBranchesByFranchiseId"))
                .doOnError(throwable -> logger.warning("Se ejecuta el metodo getBranchesByFranchiseId, pero termina en error: " + throwable.getMessage()));
    }
}
