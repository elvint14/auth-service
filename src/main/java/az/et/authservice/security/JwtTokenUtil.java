package az.et.authservice.security;

import az.et.authservice.entity.RoleEntity;
import az.et.authservice.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.token.private}")
    private String privateKey;

    @Value("${jwt.token.public}")
    private String publicKey;

    private final JwtUserDetailsService jwtUserDetailsService;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String createAccessToken(UserEntity user) {
        final Claims claims = Jwts.claims()
                .setSubject(user.getUsername());
        claims.put("id", user.getId());
        claims.put(
                "roles",
                user.getRoles()
                        .stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toList())
        );

        Date now = new Date();
        int accessTokenTime = 3600000;
        Date expiration = new Date(now.getTime() + accessTokenTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                .compact();
    }

    public String createRefreshToken(UserEntity user, boolean rememberMe) {
        final Claims claims = Jwts.claims()
                .setSubject(user.getUsername());
        claims.put("id", user.getId());
        claims.put(
                "roles",
                user.getRoles()
                        .stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toList())
        );

        Date now = new Date();
        int refreshTokenTime = 86400000;
        Date expiration = rememberMe
                ? new Date(now.getTime() + refreshTokenTime * 90L)
                : new Date(now.getTime() + refreshTokenTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                .compact();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token); //may be different exceptions
    }

    public Authentication getAuthentication(String token) {
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private PrivateKey getPrivateKey() {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            return kf.generatePrivate(keySpecPKCS8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PublicKey getPublicKey() {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            return kf.generatePublic(keySpecX509);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();
    }
}