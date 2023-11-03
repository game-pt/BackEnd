package com.a405.gamept.global;

import com.a405.gamept.global.error.enums.ErrorMessage;
import com.a405.gamept.global.error.exception.custom.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Component
@Slf4j
public class JwtProvider implements InitializingBean {

    private final long accessValidityIn;  // token 유효 기간
    private final long refreshValidityIn;  // token 유효 기간
    private final String secret;
    private Key key;


    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        this.accessValidityIn = 2 * 60 * 60;  // 2시간
        this.refreshValidityIn = 14 * 24 * 60 * 60;  // 2주일
    }

    // Bean 주입 받은 후 할당
    @Override
    public void afterPropertiesSet() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 토큰 정보
    public String generateToken(String clientId, String role, boolean isRefresh) {
        Claims claims = Jwts.claims().setSubject(clientId);  // 사용하는 클레임 세트
        claims.put("role", role);  // 임의 역할 부여
        // key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder().setClaims(claims).setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + ((isRefresh)? refreshValidityIn : accessValidityIn))) // 해당 옵션 없으면 expire X
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘과 signature에 들어갈 secret 값
                .compact();
    }

    // 토큰으로부터 받은 정보를 기반으로 Authentication 객체 반환
    public Authentication getAuthentication(String accessToken) {
        return new UsernamePasswordAuthenticationToken(getClientId(accessToken), "", createAuthorityList(getRole(accessToken)));
    }

    // 토큰 검증
    public boolean validateToken(String token) throws BusinessException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(removeBearer(token));
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new BusinessException(ErrorMessage.TOKEN_INVALID);
        } catch (ExpiredJwtException e) {  // Access 토큰이 만료 되었을 때
            throw new BusinessException(ErrorMessage.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new BusinessException(ErrorMessage.TOKEN_UNSUPPORTED);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorMessage.TOKEN_INVALID);
        }
    }

    // Authorization Header를 통해 인증
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰에 담겨있는 clientId 획득
    public String getToken(String token) {
        return removeBearer(token);
    }
    // 토큰에 담겨있는 clientId 획득
    public String getClientId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(removeBearer(token))
                .getBody().getSubject();
    }

    private String getRole(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(removeBearer(accessToken))
                .getBody()
                .get("role", String.class);

    }

    private String removeBearer(String token) {
        return token.replace("Bearer ", "");
    }

}