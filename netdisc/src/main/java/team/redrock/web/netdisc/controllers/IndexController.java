package team.redrock.web.netdisc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import team.redrock.web.netdisc.utils.DataUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
@Controller
public class IndexController {
//    private String base_path;
    @RequestMapping("/index/**")
    public String index(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String,Object> map) throws IOException{
        DataUtil dataUtil=new DataUtil();
        String base_path="D:/";
        String root_path="root";
        String uri=request.getRequestURI();
        String target="";
        if (uri.length()>7) {
            if (uri.contains(root_path)) {
                target = base_path + uri.substring(7);
            }else {
                response.sendRedirect("/index/"+root_path);
            }
        }else {
            target =base_path+root_path;
        }
        target=target.replace("\\","/");
        ArrayList<Map> arrayList=dataUtil.get_data(target);
        if (arrayList.isEmpty()){
            response.sendRedirect("/load/download?method=inline&target="+target);
            return null;
        }else {
            map.put("file_array", arrayList);
            System.out.println("index.target->"+target);
            map.put("current",target);
            return "helloworld";
        }
    }
    @RequestMapping("/sign_in")
    public String go_to_sign_in(){
        return "sign_in";
    }


    @RequestMapping("/sign_up")
    public String go_to_sign_up(){
        return "sign_up";
    }

    @RequestMapping("/")
    public void go_to_index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/index/root");
    }

}
