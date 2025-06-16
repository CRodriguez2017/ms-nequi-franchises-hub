package co.com.bancolombia.model.franchise;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Franchise {

    private Integer id;
    private String name;
}
