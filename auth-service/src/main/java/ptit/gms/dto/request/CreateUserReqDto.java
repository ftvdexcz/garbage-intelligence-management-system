package ptit.gms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateUserReqDto {
    @JsonProperty("username")
    @NotBlank(message = "username (tên người dùng) không được để trống")
    private String username;

    @JsonProperty("password")
    @Length(min = 8, message = "password (mật khẩu) ít nhất 8 kí tự")
    @NotBlank(message = "password (mật khẩu) không đuợc để trống")
    private String password;

    @JsonProperty("phone")
    @NotBlank(message = "phone (số điện thoại) không đuợc để trống")
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})", message = "phone (sđt) không hợp lệ")
    private String phone;

    @JsonProperty("email")
    @NotBlank(message = "email không được để trống")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "email không hợp lệ")
    private String email;

    @JsonProperty("role_type")
    private Integer roleType;
}
