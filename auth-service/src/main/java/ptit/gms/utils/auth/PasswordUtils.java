package ptit.gms.utils.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    public String encryptPassword(String password){
        return passwordEncoder.encode(password);
    }

    public boolean decryptPassword(String password, String hashedPassword){
        return passwordEncoder.matches(password, hashedPassword);
    }

}
