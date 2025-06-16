package co.com.bancolombia.jpa.repository;

import co.com.bancolombia.jpa.entity.BranchEntity;
import co.com.bancolombia.jpa.entity.FranchiseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface BranchesRepository extends CrudRepository<BranchEntity, Integer>, QueryByExampleExecutor<BranchEntity> {
    List<BranchEntity> findBranchesByFranchiseId(Integer franchiseId);
}