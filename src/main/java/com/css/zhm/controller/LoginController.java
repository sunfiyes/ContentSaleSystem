package com.css.zhm.controller;
/**
 * 描述:
 * 登录
 *
 * @author zhm
 * @create 2018-03-10 10:28
 */
import com.css.zhm.entity.User;
import com.css.zhm.mapper.UserMapper;
import com.css.zhm.util.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private static String UserSession = "user";

    @Resource
    private UserMapper userMapper;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public Object userLogin(
            @Param("userName") String username,
            @Param("password") String password,
            HttpSession session
    ) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user = userMapper.selectOne(user);
        if(user != null) {
            session.setAttribute("user", user);
            return Message.success();
        } else {
            return Message.failed("登录失败");
        }
    }


    /**
     * 退出登录
     * @param session session
     * @return 登录页面
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    /**
     * 判断是否登录
     * @param session session
     * @return 判断结果
     */
    @RequestMapping(value = "/api/isLogin", method = RequestMethod.GET)
    public Object isLogin(HttpSession session) {
        if (session.getAttribute(UserSession) != null) {
            return Message.success();
        } else {
            return Message.failed();
        }
    }

}