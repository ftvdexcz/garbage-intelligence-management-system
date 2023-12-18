package ptit.gms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBinReqDto {
    @JsonProperty("company")
    private String company;

    @JsonProperty("lat")
    @DecimalMin(value = "-90.0", message = "lat (vĩ độ) phải lớn hơn -90.0")
    @DecimalMax(value = "90.0", message = "lat (vĩ độ) phải nhỏ hơn 90.0")
    private BigDecimal lat;

    @JsonProperty("lon")
    @DecimalMin(value = "-180.0", message = "lon (kinh độ) phải lớn hơn -180.0")
    @DecimalMax(value = "180.0", message = "lon (kinh độ) phải nhỏ hơn 180.0")
    private BigDecimal lon;

    @JsonProperty("capacity")
    @Min(value = 0, message = "capacity (sức chứa) phải lớn hơn 0")
    private Integer capacity;

    @JsonProperty("address")
    private String address;

    @JsonProperty("owner_name")
    private String ownerName;

    @JsonProperty("owner_phone")
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})", message = "owner_phone (sđt người đại diện) không hợp lệ")
    private String ownerPhone;

    @JsonProperty("owner_email")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "owner_email (email người đại diện) không hợp lệ")
    private String ownerEmail;

    @JsonProperty("camera_url")
    private String cameraUrl;
}
