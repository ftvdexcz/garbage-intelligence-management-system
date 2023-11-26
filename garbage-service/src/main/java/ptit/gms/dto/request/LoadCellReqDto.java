package ptit.gms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoadCellReqDto {
    @JsonProperty("bin_code")
    @NotEmpty(message = "bin_code (mã bin) không được để trống")
    private String binCode;

    @JsonProperty("weight")
    @Min(value = 0, message = "weight (khối lượng) phải lớn hơn 0")
    private int weight;
}
