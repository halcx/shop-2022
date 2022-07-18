package net.shop.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import net.shop.model.LoginUser;

import java.util.Date;

@Slf4j
public class JWTUtil {
    /**
     * 过期时间 70天
     */
    private static final long EXPIRE = 60*1000*60*24*7*10;

    /**
     * 密钥
     */
    private static final String SECRET = "shop.net.666";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "shop2022";

    /**
     * subject
     */
    private static final String SUBJECT = "shop";


    /**
     * 根据用户信息生成令牌
     * @param loginUser
     * @return
     */
    public static String generateJsonWebToken(LoginUser loginUser){
        if(loginUser==null){
            throw new NullPointerException("loginUser对象为空");
        }
        Long userId = loginUser.getId();
        String token = Jwts.builder().setSubject(SUBJECT).claim("head_img", loginUser.getHeadImg())
                .claim("id", userId)
                .claim("name", loginUser.getName())
                .claim("mail", loginUser.getMail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
        token = TOKEN_PREFIX+token;
        return token;
    }

    /**
     * 校验token的算法
     * @param token
     * @return
     */
    public static Claims checkJWT(String token){
        try {
            final Claims body = Jwts.parser().setSigningKey(SECRET).
                    parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
            return body;
        }catch (Exception e){
            log.info("jwt token揭秘失败");
            return null;
        }
    }
}
