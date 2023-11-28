package ptit.gms.utils.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import ptit.gms.config.Config;
import ptit.gms.exception.ApiException;
import ptit.gms.utils.TimeUtils;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    @Autowired
    Config config;

    public String signToken(String userId) {
        Date expiryDate = new Date(TimeUtils.getCurrentTimestampMs() + config.getJwtExpirationMs());

        String accessToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(new SecretKeySpec(config.getJwtSecretKey().getBytes(), SignatureAlgorithm.HS256.getJcaName()), SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    public String validateToken(String token) {
        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(new SecretKeySpec(config.getJwtSecretKey().getBytes(), SignatureAlgorithm.HS256.getJcaName()))
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            return claims.getSubject();
        }catch (MalformedJwtException ex) {
            log.error("[JwtTokenProvider - validateToken] error: Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("[JwtTokenProvider - validateToken] error: Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("[JwtTokenProvider - validateToken] error: Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("[JwtTokenProvider - validateToken] error: JWT claims string is empty.");
        } catch (Exception ex){
            log.error("[JwtTokenProvider - validateToken] error: {}", ex.getMessage());
        }
        throw ApiException.ErrForbidden().build();
    }

    public String parseAccessToken(WebRequest request){
        String accessToken = request.getHeader("Authorization");
        try{
            if(accessToken != null && accessToken.startsWith("Bearer")){
                accessToken = accessToken.split(" ")[1];

                return accessToken;
            }

            throw ApiException.ErrForbidden().build();
        }catch(ArrayIndexOutOfBoundsException ex){
            throw ApiException.ErrForbidden().build();
        }
    }
}
