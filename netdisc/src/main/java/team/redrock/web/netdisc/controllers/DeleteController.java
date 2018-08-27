package team.redrock.web.netdisc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.redrock.web.netdisc.utils.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
public class DeleteController {
    @RequestMapping("/delete")
    public String delete_handler(@RequestParam("target")String target,
                                 HttpServletResponse response) throws IOException {
        FileUtil.deleteAll(new File(target));
        response.sendRedirect("/index/"+target.substring(target.indexOf(":")+1,target.lastIndexOf("/")));
        return null;
    }
}
