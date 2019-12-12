package lsl.java.web.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.lang.Nullable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    //设置token过期时间7200s，有点短，再加长100倍
    private static final long EXPIRE_TIME=1000*7200*100;
    //设置token的私钥，随便设置一个
    private static final String TOKEN_SECRET="652a32f032e56239652320ff326c2365e0";

    /**
     * 基于私钥、过期时间，生成token，在token中附带用户名与用户id
     * @param userName token携带用户名信息
     * @param password token携带用户密码做校验
     * @return token
     */
    public static String sign(String userName,String password){
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);//设置过期时间
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);//私钥以及加密算法
        //设置头部信息
        Map<String, Object> header = new HashMap<>(2);
        header.put("type", "JWT");
        header.put("alg", "HS256");
        //设置头部，附带信息，设置过期时间，加上算法生成token
        return JWT.create().withHeader(header)
                .withClaim("name", userName)
                .withClaim("password", password)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 对给定 token进行校验
     * @param token 给定token
     * @return token是否正确
     */
    public static boolean verify(String token){
        try{
            Algorithm algorithm=Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier=JWT.require(algorithm).build();//校验器
            DecodedJWT jwt =verifier.verify(token);//解码token校验，出错则抛出异常
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 保证token正确，获取JWT
     * @return 获取JWT，根据JWT能获取Claim
     */
    public static DecodedJWT getJWT(String token){
        Algorithm algorithm=Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier=JWT.require(algorithm).build();//校验器
        return verifier.verify(token);
    }

    /**
     * 登录控制，通过cookie获取token，校验，成功返回token，失败null
     */
    @Nullable
    public static String loginCheck(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        //避免空指针
        if(cookies==null)return null;
        //遍历获取cookie校验，成功取出信息登录并刷新token,重置cookie
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("token")){
                String token=cookie.getValue();//获取token
                if(TokenUtil.verify(token)){
                    return token;
                }
            }
        }

        //校验失败
        return null;
    }
}
