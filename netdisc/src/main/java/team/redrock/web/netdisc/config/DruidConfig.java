//package team.redrock.web.netdisc.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import org.apache.catalina.servlets.DefaultServlet;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//
//@MapperScan("team.redrock.web.netdisc.mappers")
//public class DruidConfig {
//
//    @ConfigurationProperties(prefix = "spring.datasource.druid")
//    @Bean
//    public DataSource druid(){
//        return new DruidDataSource();
//    }
//
//    @Bean
//    public ServletRegistrationBean DruidServlet(){
//        ServletRegistrationBean bean=new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
//
//        Map<String,String> initParams = new HashMap<>();
//        initParams.put("loginUsername","815923529");//登录druid监控的账户
//        initParams.put("loginPassword","asdasd123123");//登录druid监控的密码
//        initParams.put("allow","");//默认就是允许所有访问
//
//        bean.setInitParameters(initParams);
//        return bean;
//    }
//
//    @Bean
//    public FilterRegistrationBean DruidFilter(){
//        FilterRegistrationBean bean=new FilterRegistrationBean();
//        bean.setFilter(new WebStatFilter());
//
//        Map<String,String> initParams = new HashMap<>();
//        initParams.put("exclusions","*.js,*.css,/druid/*");
//
//        bean.setInitParameters(initParams);
//
//        bean.setUrlPatterns(Arrays.asList("/*"));
//
//        return  bean;
//    }
//}
