package team.redrock.web.netdisc.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import team.redrock.web.netdisc.utils.FileUtil;

@Controller
public class UpController {

    @GetMapping( "/up")
    public String page(Map<String,String> map,
                       @RequestParam("target") String target){
        map.put("target",target);
        return "upload";
    }

    /**
     * @author van
     * 检查文件存在与否
     */
    @PostMapping("/checkFile")
    @ResponseBody
    public Boolean checkFile(@RequestParam(value = "md5File") String md5File) {
        Boolean exist = false;

        //实际项目中，这个md5File唯一值，应该保存到数据库或者缓存中，通过判断唯一值存不存在，来判断文件存不存在，这里我就不演示了
		/*if(true) {
			exist = true;
		}*/
        return exist;
    }

    /**
     * @author van
     * 检查分片存不存在
     */
    @PostMapping("/checkChunk")
    @ResponseBody
    public Boolean checkChunk(@RequestParam(value = "md5File") String md5File,
                              @RequestParam(value = "chunk") Integer chunk) {
        Boolean exist = false;
        String path = "D:/"+md5File+"/";//分片存放目录
        String chunkName = chunk+ ".tmp";//分片名
        File file = new File(path+chunkName);
        if (file.exists()) {
            exist = true;
        }
        return exist;
    }

    /**
     * @author van
     * 修改上传
     */
    @PostMapping("/upload")
    @ResponseBody
    public Boolean upload(@RequestParam(value = "file") MultipartFile file,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "chunk",required= false) Integer chunk) { //第几片，从0开始
        String path = "D:/"+md5File+"/";
        File dirfile = new File(path);
        if (!dirfile.exists()) {//目录不存在，创建目录
            dirfile.mkdirs();
        }
        String chunkName;
        if(chunk == null) {//表示是小文件，还没有一片
            chunkName = "0.tmp";
        }else {
            chunkName = chunk+ ".tmp";
        }
        String filePath = path+chunkName;
        File savefile = new File(filePath);

        try {
            if (!savefile.exists()) {
                savefile.createNewFile();//文件不存在，则创建
            }
            file.transferTo(savefile);//将文件保存
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * @author van
     * 合成分片
     */
    @PostMapping("/merge")
    @ResponseBody
    public Boolean merge(@RequestParam(value = "chunks",required =false) Integer chunks,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "name") String name,
                         @RequestParam(value = "target") String target) throws Exception {
        String path = "D:";
        FileOutputStream fileOutputStream = new FileOutputStream(target+File.separator+name);  //合成后的文件
        try {
            byte[] buf = new byte[1024];
            for(long i=0;i<chunks;i++) {
                String chunkFile=i+".tmp";
                File file = new File(path+"/"+md5File+"/"+chunkFile);
                InputStream inputStream = new FileInputStream(file);
                int len = 0;
                while((len=inputStream.read(buf))!=-1){
                    fileOutputStream.write(buf,0,len);
                }
                inputStream.close();
            }
            File file=new File(path+File.separator+md5File);
            FileUtil.deleteAll(file);
        } catch (Exception e) {
            return false;
        }finally {
            fileOutputStream.close();
        }
        return true;
    }
}