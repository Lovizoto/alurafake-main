package br.com.alura.AluraFake.security.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    /*
     Em produção, as variáveis secretKey e expirationTime
     devem estar em um arquivo properties,
     com uma possível classe de mapeamento dos names dos properties
    */

    @Value("${jwt.secret.key")
    private String SECRET_KEY = "chaveDeAcessoComMultiplos Carcteres";
    private long EXPIRATION_TIME = 86400000L; //24h = 3600s(1h) * 24h * 1000ms(1s)

    public String extractUsername(String token) {

    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parse(token)
                .getBody();
    }

    private byte[] getSignInKey() {
        return null;
    }

}
