package co.com.bancolombia.model.product;
import co.com.bancolombia.model.branch.Branch;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {

    private Integer id;
    private String name;
    private Integer stock;
    private Branch branch;
}
