package ptit.gms.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TruckResDto {
    @JsonProperty("plate")
    @NotBlank(message = "plate (bển số) không được để trống")
    private String plate;

    @JsonProperty("company")
    @NotBlank(message = "company (đơn vị thu gom) không được để trống")
    private String company;

    @JsonProperty("capacity")
    @Min(value = 0, message = "capacity (sức chứa) phải lớn hơn 0")
    private int capacity;

    @JsonProperty("created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    @JsonProperty("updated_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedDate;
}
