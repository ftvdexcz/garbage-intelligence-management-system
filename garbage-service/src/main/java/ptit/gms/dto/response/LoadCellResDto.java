package ptit.gms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoadCellResDto {
    @JsonProperty("bin_code")
    private String binCode;

    @JsonProperty("weight")
    private int weight;

    @JsonProperty("total_weight")
    private int totalWeight;

    @JsonProperty("updated_timestamp")
    private long timestamp;
}
