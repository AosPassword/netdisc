package team.redrock.web.netdisc.filters;

import team.redrock.web.netdisc.beens.User;

import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class User_filter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse= (HttpServletResponse) servletResponse;

        User user= (User) httpServletRequest.getSession().getAttribute("user");
        if (user!=null){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else {
            httpServletResponse.getWriter().write("您还未曾登陆，页面一秒钟后刷新");
            httpServletResponse.setHeader("refresh", "1;url=" + httpServletRequest.getContextPath() + "/sign_in");
        }
    }

    @Override
    public void destroy() {

    }
}
