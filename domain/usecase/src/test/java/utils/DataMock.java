package utils;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.product.Product;

import java.util.List;

public class DataMock {

    public static Franchise createFakeFranchise(Integer id, String name) {
        return Franchise.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static List<Franchise> getFakeFranchise() {
        return List.of(
                createFakeFranchise(1, "Franchise 1"),
                createFakeFranchise(2, "Franchise 2"),
                createFakeFranchise(3, "Franchise 3"));
    }

    public static Branch createFakeBranch(Integer id, String name, Franchise franchise) {
        return Branch.builder()
                .id(id)
                .name(name)
                .franchise(franchise)
                .build();
    }

    public static List<Branch> getFakeBranches(Franchise franchise) {
        return List.of(
                createFakeBranch(1, "Branch 1", franchise),
                createFakeBranch(2, "Branch 2", franchise),
                createFakeBranch(3, "Branch 3", franchise));
    }

    public static Product createFakeProduct(Integer id, String name, Integer stock, Branch branch) {
        return Product.builder()
                .id(id)
                .name(name)
                .branch(branch)
                .stock(stock)
                .build();
    }
}
