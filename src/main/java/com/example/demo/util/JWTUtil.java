package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

/**
 * 创建人:连磊
 * 日期: 2018/12/21. 10:55
 * 描述：
 */
public class JWTUtil {


    /**
     *开 发 者：连磊
     *开发时间：2018/12/21 15:14
     *方 法 名：getUserToken
     *传入参数：[userName, userId, expTime, base64Encryption]
     *返 回 值：java.lang.String
     *描    述：为当前登录用户获取token用于后面的验证
     **/
    public static String getUserToken(String userName , String userId , Long expTime ,String base64Encryption){

        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim(StaticUtils.U_NAME, userName)
                .claim(StaticUtils.U_ID, userId)
                .signWith(SignatureAlgorithm.HS256, getEncryption(base64Encryption));
        long timeMillis = System.currentTimeMillis();
        Date date = new Date(timeMillis);
        if (expTime > 0L){
            long expTimes = timeMillis + expTime;
            Date date1 = new Date(expTimes);
            builder.setExpiration(date1).setNotBefore(date);
        }
        return builder.compact();
    }

    /**
     *开 发 者：连磊
     *开发时间：2018/12/21 15:12
     *方 法 名：validateJwt
     *传入参数：[token, base64Encryption]
     *返 回 值：io.jsonwebtoken.Claims
     *描    述：校验当前token是否正确（超时，未登录）
     **/
    public static Claims validateJwt(String token ,String base64Encryption){
        if (null == token){
            return null;
        }
        try {
            Claims body = Jwts.parser().setSigningKey(getEncryption(base64Encryption)).parseClaimsJws(token).getBody();
            return body;
        }catch (Exception e){
            return null;
        }
    }

    /**
     *开 发 者：连磊
     *开发时间：2018/12/21 15:12
     *方 法 名：extractInfo
     *传入参数：[token, secret]
     *返 回 值：java.util.Map<java.lang.String,java.lang.Object>
     *描    述：获取所有加密的参数
     **/
    public static Map<String,Object> extractInfo(String token, String secret){
        Claims claims = validateJwt(token,secret);
        if(null != claims){
            Map<String,Object> info = new HashMap<String,Object>();
            Set<String> keySet = claims.keySet();
            //通过迭代，提取token中的参数信息
            Iterator<String> iterator = keySet.iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                Object value =  claims.get(key);
                info.put(key,value);

            }
            return info;
        }
        return null;
    }

    /**
     *开 发 者：连磊
     *开发时间：2018/12/21 15:12
     *方 法 名：getEncryption
     *传入参数：[data]
     *返 回 值：java.lang.String
     *描    述：二重加密
     **/
    public static String getEncryption(String data){
        StringBuffer stringBuffer = new StringBuffer("");
        for(int x=0;x<data.length();x++){
            char c=data.charAt(x);
            c += (char)1;
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }
}
