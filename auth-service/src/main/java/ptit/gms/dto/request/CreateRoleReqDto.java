package ptit.gms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRoleReqDto {
    @JsonProperty("role_name")
    @NotBlank(message = "roleName (mô tả role) không được để trống")
    private String roleName;

    @JsonProperty("role_type")
    @NotNull(message = "roleType (loại role) không được để trống")
    @Min(value = 0, message = "roleType (loại role) không hợp lệ")
    private Integer roleType;
}
