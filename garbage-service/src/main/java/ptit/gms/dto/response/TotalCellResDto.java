package ptit.gms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalCellResDto {
    @JsonProperty("bin_code")
    private String bin_code;

    @JsonProperty("total_weight")
    private int totalWeight;

    @JsonProperty("updated_timestamp")
    private long updatedTimestamp;
}
