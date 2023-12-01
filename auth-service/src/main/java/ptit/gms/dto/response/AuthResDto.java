package ptit.gms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResDto {
    @JsonProperty("access_token")
    private String token;

    @JsonProperty("user")
    private UserResDto user;
}
