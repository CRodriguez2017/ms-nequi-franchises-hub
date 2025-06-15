package co.com.bancolombia.api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Param {

        FRANCHISE_ID("franchiseId"),
        BRANCH_ID("branchId"),
        PRODUCT_ID("productId");

        private final String name;

}
