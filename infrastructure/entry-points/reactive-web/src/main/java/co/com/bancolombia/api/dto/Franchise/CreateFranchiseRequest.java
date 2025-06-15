package co.com.bancolombia.api.dto.Franchise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFranchiseRequest {

  //  @NotBlank(message = "Brand name is required")
    private String name;
}
