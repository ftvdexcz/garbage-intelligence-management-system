package ptit.gms.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaEventLoadCell implements Serializable {
    @JsonProperty("bin_id")
    private String binId;

    @JsonProperty("company")
    private String company;

    @JsonProperty("lat")
    private BigDecimal lat;

    @JsonProperty("lon")
    private BigDecimal lon;

    @JsonProperty("total_weight")
    private int totalWeight;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("weight")
    private int weight;
}
