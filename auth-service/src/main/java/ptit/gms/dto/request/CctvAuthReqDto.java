package ptit.gms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CctvAuthReqDto {
    @JsonProperty("ip")
    private String ip;

    @JsonProperty("user")
    private String user;

    @JsonProperty("password")
    private String password;

    @JsonProperty("path")
    private String path;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("id")
    private String id;

    @JsonProperty("action")
    private String action;

    @JsonProperty("query")
    private String query;
}
