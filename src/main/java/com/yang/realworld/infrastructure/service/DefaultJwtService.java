package com.yang.realworld.infrastructure.service;

import com.yang.realworld.core.service.JwtService;
import com.yang.realworld.core.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Optional;
import jdk.nashorn.internal.runtime.options.Option;

public class DefaultJwtService implements JwtService {

  private String secret;
  private int sessionTime;

  public DefaultJwtService(String secret, int sessionTime) {
    this.secret = secret;
    this.sessionTime = sessionTime;
  }

  @Override
  public String toToken(User user) {
    return Jwts.builder()
        .setSubject(user.getId())
        .setExpiration(expireTimeFromNow())
        .signWith(SignatureAlgorithm.HS512,secret)
        .compact();
  }

  @Override
  public Optional<String> getSubFromToken(String token) {
    try{
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      return Optional.ofNullable(claimsJws.getBody().getSubject());
    }catch (Exception e){
      return Optional.empty();
    }

  }

  private Date expireTimeFromNow(){
    return new Date(System.currentTimeMillis()+sessionTime*1000);
  }
}
