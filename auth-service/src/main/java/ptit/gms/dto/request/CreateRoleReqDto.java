package ptit.gms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoleReqDto {
    @JsonProperty("role_name")
    @NotBlank(message = "roleName (mô tả role) không được để trống")
    private String roleName;
}
