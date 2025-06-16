package co.com.bancolombia.api.dto.Branch;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBranchRequest {
    @NotBlank(message = "El nombre de la sucursal es obligatoria")
    private String name;
}
