package cn.hfbin.seckill.controller;

import cn.hfbin.seckill.common.Const;
import cn.hfbin.seckill.entity.User;
import cn.hfbin.seckill.exception.HfbinException;
import cn.hfbin.seckill.param.LoginParam;
import cn.hfbin.seckill.redis.RedisService;
import cn.hfbin.seckill.redis.UserKey;
import cn.hfbin.seckill.result.CodeMsg;
import cn.hfbin.seckill.result.Result;
import cn.hfbin.seckill.service.UserService;
import cn.hfbin.seckill.util.CookieUtil;
import cn.hfbin.seckill.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by: zhangsheng
 * Date: 2018/7/9
 * Time: 12:37
 * Such description:
 */
@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;
    //用户登录的方法
    @RequestMapping("/login")
    @ResponseBody
    public Result<User> doLogin(HttpServletResponse response, HttpSession session , @Valid LoginParam loginParam) {
        //从数据库中，通过手机号码取出用户的信息（包括加密的信息）
    	Result<User> login = userService.login(loginParam);
       //登录成功
    	if (login.isSuccess()){
    		//获取sessionid,写入cookie中（名字：seckill_login_token，值：sessionid）
    		CookieUtil.writeLoginToken(response,session.getId());
    		//单点登录（1.key 的前缀，2.sesionid key,3.存入的值，4.设置过期的时间）
            redisService.set(UserKey.getByName , session.getId() ,login.getData(), Const.RedisCacheExtime.REDIS_SESSION_EXTIME );
        }
        return login;
    }

    @RequestMapping("/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request , response);
        redisService.del(UserKey.getByName , token);
        return "login";
    }
}
