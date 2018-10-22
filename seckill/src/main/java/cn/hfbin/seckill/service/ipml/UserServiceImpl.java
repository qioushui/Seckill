package cn.hfbin.seckill.service.ipml;

import cn.hfbin.seckill.entity.User;
import cn.hfbin.seckill.dao.UserMapper;
import cn.hfbin.seckill.param.LoginParam;
import cn.hfbin.seckill.result.CodeMsg;
import cn.hfbin.seckill.result.Result;
import cn.hfbin.seckill.service.UserService;
import cn.hfbin.seckill.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/10
 * Time: 12:01
 * Such description:
 */
@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;
    @Override
    public Result<User> login(LoginParam loginParam) {
    	//判断手机号码有木有
        User user = userMapper.checkPhone(loginParam.getMobile());
        //没有查出来
        if(user == null){
           //返回手机不存在的
           return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        }
       //获取密码
        String dbPwd= user.getPassword();
        //md5加密的盐
        String saltDB = user.getSalt();
        //算出来加密的密文
        String calcPass = MD5Util.formPassToDBPass(loginParam.getPassword(), saltDB);
        //比较密文是否相同
        if(!StringUtils.equals(dbPwd , calcPass)){
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        //登录校验完成（就执行返回成功，将密码置空）
        user.setPassword(StringUtils.EMPTY);
        return Result.success(user);
    }
}
