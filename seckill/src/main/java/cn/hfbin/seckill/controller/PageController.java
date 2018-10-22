package cn.hfbin.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by: 张胜
 * Date: 2018/7/9
 * Time: 12:36
 * Such description:
 */

@Controller
@RequestMapping("/page")
public class PageController {

	//程序的登录访问的页面（没有用ResponseBody，就是默认返回页面）
    @RequestMapping("login")
    public String loginPage(){

        return "login";
    }
}
