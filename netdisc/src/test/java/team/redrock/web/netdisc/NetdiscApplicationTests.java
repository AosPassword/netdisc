package team.redrock.web.netdisc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import team.redrock.web.netdisc.utils.FormatUtil;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NetdiscApplicationTests {
    @Test
    public void contextLoads() {
        FormatUtil formatUtil=new FormatUtil();
        File file=new File("D:\\root");
        File[] files=file.listFiles();
        for (File demo:files){
            System.out.println(demo.getAbsolutePath().substring(demo.getAbsolutePath().indexOf(":")+1));
        }
    }

}
