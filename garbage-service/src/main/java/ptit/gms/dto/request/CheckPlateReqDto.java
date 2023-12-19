package ptit.gms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CheckPlateReqDto {
    @JsonProperty("plates")
    private List<String> plates;

    @JsonProperty("bin_id")
    private String binId;
}
