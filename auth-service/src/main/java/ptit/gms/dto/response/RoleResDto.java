package ptit.gms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResDto {
    @JsonProperty("code")
    private String code;

    @JsonProperty("role_name")
    private String roleName;
}
