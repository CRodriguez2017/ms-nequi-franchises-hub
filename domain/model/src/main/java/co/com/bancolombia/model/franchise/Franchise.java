package co.com.bancolombia.model.franchise;
import lombok.*;
//import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Franchise {

    private Integer id;
    private String name;
    //private LocalDateTime created_at;
    //private LocalDateTime updated_at;
}
