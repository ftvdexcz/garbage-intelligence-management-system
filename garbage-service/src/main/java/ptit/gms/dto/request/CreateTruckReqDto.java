package ptit.gms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTruckReqDto {
    @JsonProperty("plate")
    @NotBlank(message = "plate (bển số) không được để trống")
    private String plate;

    @JsonProperty("company")
    @NotBlank(message = "company (đơn vị thu gom) không được để trống")
    private String company;

    @JsonProperty("capacity")
    @Min(value = 0, message = "capacity (sức chứa) phải lớn hơn 0")
    private int capacity;
}
