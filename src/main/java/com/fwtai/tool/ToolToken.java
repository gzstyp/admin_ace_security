package com.fwtai.tool;

import com.fwtai.bean.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

/**
 * json web token 工具类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-26 0:37
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Component
public class ToolToken{

    public static final String roles = "roles";

    private final static long expiry = 1000 * 60 * 60;//1个小时

    private final static String issuer = "贵州富翁泰科技有限责任公司";

    private final static String secret = "V1JGR0dEZDJRZzAyYUhCVkhjVjZ1Umg5bHZvOG05VlVYd0FMUXlydEZOQUxhcitLWjM5ZitjNjR0WlgwSFBOQg==";

    public String extractUserId(final String token){
        return extractClaim(token,Claims::getId);
    }

    public <T> T extractClaim(final String token,final Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token){
        final Key key = Keys.hmacShaKeyFor(secret.getBytes());
        final JwtParserBuilder builder = Jwts.parserBuilder();
        return builder.requireIssuer(issuer).setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    //是否有效
    public Boolean isTokenExpired(final String token){
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }

    public Boolean validateToken(final String token,final String userId){
        final String uid = extractUserId(token);
        return (uid.equals(userId) && !isTokenExpired(token));
    }

    // setSubject 不能和s etClaims() 同时使用,如果用不到 userId() 的话可以把setId的值设为 userName !!!
    public String generateToken(final String userId){
        final long date = System.currentTimeMillis();
        final Key key = Keys.hmacShaKeyFor(secret.getBytes());
        final JwtBuilder builder = Jwts.builder().signWith(key,SignatureAlgorithm.HS384);
        return builder.setId(userId).setIssuer(issuer).setIssuedAt(new Date(date)).setExpiration(new Date(date + expiry)).compact();
    }

    // setSubject 不能和s etClaims() 同时使用,如果用不到 userId() 的话可以把setId的值设为 userName !!!
    public String generateToken(final AuthUser authUser){
        final long date = System.currentTimeMillis();
        final Key key = Keys.hmacShaKeyFor(secret.getBytes());
        final JwtBuilder builder = Jwts.builder().signWith(key,SignatureAlgorithm.HS384);
        final Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        if(authorities != null && authorities.size() > 0){
            builder.claim(this.roles,authorities);
        }
        return builder.setId(authUser.getUserId()).setIssuer(issuer).setIssuedAt(new Date(date)).setExpiration(new Date(date + expiry)).compact();
    }
}