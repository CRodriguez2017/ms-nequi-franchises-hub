package co.com.bancolombia.jpa.repository;

import co.com.bancolombia.jpa.entity.FranchiseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface FranchiesRepository extends CrudRepository<FranchiseEntity, Integer>, QueryByExampleExecutor<FranchiseEntity> {
}
