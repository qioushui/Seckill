package cn.hfbin.seckill.config;

import cn.hfbin.seckill.filter.SessionExpireFilter;
import cn.hfbin.seckill.interceptor.AuthorityInterceptor;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;

/**
 * Created by: zhangsheng
 * Date: 2018/7/11
 * Time: 20:58
 * Such description:
 */
//要想这个拦截器工作，我们要重写WebMvcConfigurerAdapter中的addInterceptors方法，将我们的拦截器添加进去就可以了：
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    AuthorityInterceptor authorityInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        // 映射为 user 的控制器下的所有映射
        //registry.addInterceptor(authorityInterceptor).addPathPatterns("/user/login").excludePathPatterns("/index", "/");
        registry.addInterceptor(authorityInterceptor);
        super.addInterceptors(registry);
    }

    @Bean("myFilter")
    public Filter uploadFilter() {
       //设置sessionExpireFilter
    	return new SessionExpireFilter();
    }

    @Bean
    public FilterRegistrationBean uploadFilterRegistration() {
       //配置sessionExpireFilter
    	FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("myFilter"));
        registration.addUrlPatterns("/**");
        registration.setName("MyFilter");
        registration.setOrder(1);
        return registration;
    }


}
