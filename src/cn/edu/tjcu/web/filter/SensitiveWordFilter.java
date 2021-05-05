package cn.edu.tjcu.web.filter;

import org.apache.taglibs.standard.extra.spath.RelativePath;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class SensitiveWordFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
        try {
            //加载文件
            ServletContext context = config.getServletContext();
            String realPath = context.getRealPath("/WEB-INF/classes/敏感词汇.txt");
            //读取文件
            BufferedReader br = new BufferedReader(new FileReader(realPath));
            //将文件的每一行添加到list中
            String line=null;
            while ((line=br.readLine())!=null){
                list.add(line);
            }
            br.close();
            System.out.println(br);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
      //proxy：代理，instance:例子
        ServletRequest prxoy_req= (ServletRequest) Proxy.newProxyInstance(req.getClass().getClassLoader(), req.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                if(method.getName().equals("getParmeter")){
                    //增强返回值
                    //返回返回值
                    String value = (String) method.invoke(req, args);

                    for (String s : list) {
                        if(value.contains(s)){
                            value.replaceAll(s,"***");
                        }
                    }
                    return value;

                }
                return  method.invoke(req,args);
            }
        });


        //放行
        chain.doFilter(prxoy_req, resp);
    }
    private  List<String> list=new ArrayList<String>();//敏感词汇list集合


    public void destroy() {
    }

}
