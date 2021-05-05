package cn.edu.tjcu.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

       //强制性转换，将ServletRequest req转化为HttpServletRequest
        HttpServletRequest request= (HttpServletRequest) req;
        //获取经过的URI
        String uri = request.getRequestURI();
//2.判断是否包含登录相关资源路径,要注意排除掉 css/js/图片/验证码等资源
if(uri.contains("/login.jsp")||uri.contains("/loginServlet")||uri.contains("/css/")||uri.contains("/loginServlet")||uri.contains("/fonts/")||uri.contains("/js/")||uri.contains("/checkCodeServlet")){
            chain.doFilter(req, resp);
        }else {
            //不包含，需要验证用户是否登录
            //3.从获取session中获取user
            Object user = request.getSession().getAttribute("user");
            if(user!=null){
                //不为空就是登录了
                chain.doFilter(req, resp);
            }else {
                request.setAttribute("login_msg","你还没登录，请跳转到登录页面登录");
                request.getRequestDispatcher("/login.jsp").forward(request,resp);
            }
        }

    }


    public void destroy() {
    }

}
