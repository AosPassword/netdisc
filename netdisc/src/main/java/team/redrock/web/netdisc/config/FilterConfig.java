package team.redrock.web.netdisc.config;

import org.redrock.web.filter.UTFfilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.redrock.web.netdisc.filters.Manager_filter;
import team.redrock.web.netdisc.filters.User_filter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean UtfFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new UTFfilter());
        registrationBean.setOrder(1);
        List<String> urlList = new ArrayList<String>();
        urlList.add("/*");
        registrationBean.setUrlPatterns(urlList);
        registrationBean.setName("UTF");
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean UserFilterRegistrationBean(){
        FilterRegistrationBean registrationBean=new FilterRegistrationBean();
        registrationBean.setFilter(new User_filter());
        registrationBean.setOrder(2);
        List<String> urlList=new ArrayList<>();
        urlList.add("/load/*");
        urlList.add("/checkChunk");
        urlList.add("/up");
        urlList.add("/upload.html");
        urlList.add("/upload");
        urlList.add("/merge");
        urlList.add("/checkFile");
        registrationBean.setUrlPatterns(urlList);
        registrationBean.setName("User");
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean ManagerFilterRegistrationBean(){
        FilterRegistrationBean registrationBean=new FilterRegistrationBean();
        registrationBean.setFilter(new Manager_filter());
        registrationBean.setOrder(2);
        List<String> urlList=new ArrayList<>();
        urlList.add("/delete");
        registrationBean.setUrlPatterns(urlList);
        registrationBean.setName("Manager");
        return registrationBean;
    }

}
