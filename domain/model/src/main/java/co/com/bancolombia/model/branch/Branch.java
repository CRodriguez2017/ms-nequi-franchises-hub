package co.com.bancolombia.model.branch;
import co.com.bancolombia.model.franchise.Franchise;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Branch {

    private Integer id;
    private String name;
    private Franchise franchise;
}
