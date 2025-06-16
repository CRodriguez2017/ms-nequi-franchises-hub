package co.com.bancolombia.api.dto.Franchise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseRequest {

    @NotBlank(message = "El nombre de la franquicia es requerido")
    private String name;
}
