package co.com.bancolombia.api.dto.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;
    @NotNull(message = "El stock del producto es obligatorio")
    @Min(value = 0, message = "El valor debe ser igual o mayor a cero (0)")
    private Integer stock;
}
