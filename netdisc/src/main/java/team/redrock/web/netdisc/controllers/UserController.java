package team.redrock.web.netdisc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import team.redrock.web.netdisc.beens.User;
import team.redrock.web.netdisc.mappers.UserMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    UserMapper userMapper;

    @PostMapping("/user/sign_in_handler")
    public String sign_in(@RequestParam("email") String email,
                          @RequestParam("password") String password,
                          HttpServletRequest request,
                          HttpServletResponse response){
        try {
            User check = (User) userMapper.checkUser(email, password);
            if (check != null) {
                request.getSession().setAttribute("user", check);
                response.sendRedirect("/");
                return null;
            } else {
                return "sign_up";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "sign_up";
        }
    }

    @PostMapping("/user/sign_up_handler")
    public String sign_up(@RequestParam("username") String usermame,
                          @RequestParam("password") String password,
                          @RequestParam("email") String email,
                          HttpServletResponse response,
                          HttpServletRequest request) throws IOException {
        User user=new User();
        user.setIdentity(1);
        user.setEmail(email);
        user.setUsername(usermame);
        user.setPassword(password);
        User check=userMapper.findUserByUsernameOrEmail(user);
        if (check.getUsername()!=null){
            response.getWriter().write("注册失败，用户名或邮箱重复，页面两秒钟后刷新");
            response.setHeader("refresh", "2;url="+request.getContextPath()+"/");
            return null;
        }else {
            userMapper.insertUser(user);
            return "sign_in";
        }
    }

    @RequestMapping(value = "/user/sign_out_handler",method = RequestMethod.GET)
    public String sign_out(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("user",null);
        response.sendRedirect("/");
        return null;
    }

}
