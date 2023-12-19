package gms.ptit.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaEventCheckPlate implements Serializable {
    @JsonProperty("checks")
    private Map<String, Boolean> checks;

    @JsonProperty("image")
    private byte[] image;

    @JsonProperty("bin_id")
    private String binId;

    @JsonProperty("company")
    private String company;

    @JsonProperty("lat")
    private BigDecimal lat;

    @JsonProperty("lon")
    private BigDecimal lon;

    @JsonProperty("timestamp")
    private long timestamp;
}