package co.com.bancolombia.jpa.repository;

import co.com.bancolombia.jpa.entity.BranchEntity;
import co.com.bancolombia.jpa.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface ProductsRepository extends CrudRepository<ProductEntity, Integer>, QueryByExampleExecutor<ProductEntity> {
    List<ProductEntity> findProductsByBranchId(Integer branchId);
}
