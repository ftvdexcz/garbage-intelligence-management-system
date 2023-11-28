package ptit.gms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthReqDto {
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
