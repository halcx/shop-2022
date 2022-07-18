package net.shop.intercepter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import net.shop.enums.BizCodeEnum;
import net.shop.model.LoginUser;
import net.shop.utils.CommonUtils;
import net.shop.utils.JWTUtil;
import net.shop.utils.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    /**
     * 前置拦截器
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //拿到token并且解密
        String accessToken = request.getHeader("token");
        if(accessToken==null){
            //再试一下url方式
            accessToken = request.getParameter("token");
        }
        //token不为空的时候
        if(StringUtils.isNotBlank(accessToken)){
            //解密
            Claims claims = JWTUtil.checkJWT(accessToken);
            if(claims == null){
                //未登陆,这里要返回一个json数据给前端告诉他未登陆
                CommonUtils.sendJsonMessage(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_UNLOGIN));
                return false;
            }
            Long userId = Long.valueOf(claims.get("id").toString());
            String headImg = (String)claims.get("head_img");
            String name = (String)claims.get("name");
            String mail = (String)claims.get("mail");

            LoginUser loginUser = LoginUser
                    .builder()
                    .headImg(headImg)
                    .name(name)
                    .id(userId)
                    .mail(mail)
                    .build();

            threadLocal.set(loginUser);
            return true;
        }
        //未登陆,这里要返回一个json数据给前端告诉他未登陆
        CommonUtils.sendJsonMessage(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_UNLOGIN));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
