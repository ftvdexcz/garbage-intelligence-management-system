package ptit.gms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class UpdateBinResDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("company")
    private String company;

    @JsonProperty("address")
    private String address;

    @JsonProperty("lat")
    private BigDecimal lat;

    @JsonProperty("lon")
    private BigDecimal lon;

    @JsonProperty("capacity")
    private int capacity;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("created_user")
    private String createdUser;

    @JsonProperty("owner")
    private CompanyOwnerResDto owner;

    @JsonProperty("camera_url")
    private String cameraUrl;
}