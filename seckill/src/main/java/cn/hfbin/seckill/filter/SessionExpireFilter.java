package cn.hfbin.seckill.filter;
import cn.hfbin.seckill.common.Const;
import cn.hfbin.seckill.entity.User;
import cn.hfbin.seckill.redis.RedisService;
import cn.hfbin.seckill.redis.UserKey;
import cn.hfbin.seckill.util.CookieUtil;
import cn.hfbin.seckill.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 重新设置用户session在redis的有效期
 */
@Component
public class SessionExpireFilter implements Filter {
    @Autowired
    RedisService redisService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    /*过滤器（它依赖于servlet容器。在实现上，基于函数回调，它可以对几乎所
    	有请求进行过滤，但是缺点是一个过滤器实例只能在容器初始化时调用一次。使用过滤器的目的，
    	是用来做一些过滤操作，获取我们想要获取的数据，比如：在Javaweb中，对传入的request、response提前过滤掉一些信息，
    	或者提前设置一些参数，然后再传入servlet或者Controller进行业务逻辑操作。通常用的场景是：在过滤器中修改字符编码（CharacterEncodingFilter）、
    	在过滤器中修改HttpServletRequest的一些参数（XSSFilter(自定义过滤器)），如：过滤低俗文字、危险字符等*/

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        //token 就是 sessionId
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        System.out.println("SessionExpireFilter----loginToken"+loginToken);
        if(StringUtils.isNotEmpty(loginToken)){
            //判断logintoken是否为空或者""；
            //如果不为空的话，符合条件，继续拿user信息
            User user = redisService.get(UserKey.getByName,loginToken, User.class);
            System.out.println("SessionExpireFilter----user"+user);
            if(user != null){
                //如果user不为空，则重置session的时间，即调用expire命令（不用手动删除）
                redisService.expice(UserKey.getByName , loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }


    @Override
    public void destroy() {

    }
}
