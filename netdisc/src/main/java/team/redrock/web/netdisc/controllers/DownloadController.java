package team.redrock.web.netdisc.controllers;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

//@Controller
//@EnableAutoConfiguration
@Controller
public class DownloadController {

    @RequestMapping(value = "/load/download")
    void download(HttpServletRequest request,
                  HttpServletResponse response,
                  @RequestHeader(required = false) String range,
                  @RequestParam("method") String method,
                  @RequestParam("target") String target) throws IOException {
        //文件目录
        File file= new File(target);
        if (!file.isDirectory()) {

            //开始下载位置
            long startByte = 0;
            //结束下载位置
            long endByte = file.length() - 1;

            //有range的话
            if (range != null && range.contains("bytes=") && range.contains("-")) {
                System.out.println("Range->" + range);
                range = range.substring(range.lastIndexOf("=") + 1).trim();
                String ranges[] = range.split("-");
                try {
                    //判断range的类型
                    if (ranges.length == 1) {
                        //类型一：bytes=-2343
                        if (range.startsWith("-")) {
                            endByte = Long.parseLong(ranges[0]);
                        }
                        //类型二：bytes=2343-
                        else if (range.endsWith("-")) {
                            startByte = Long.parseLong(ranges[0]);
                        }
                    }
                    //类型三：bytes=22-2343
                    else if (ranges.length == 2) {
                        startByte = Long.parseLong(ranges[0]);
                        endByte = Long.parseLong(ranges[1]);
                    }

                } catch (NumberFormatException e) {
                    startByte = 0;
                    endByte = file.length() - 1;
                }
            }
            /**
             * 至此客户端已将封装好的下载数据的信息提交至服务器，服务器开始返回客户端所需要的文件的数据流
             * 通过封装一个response来返回要传递的数据
             */

            //要下载的长度（为啥要加一问小学数学老师去）
            long contentLength = endByte - startByte + 1;
            //文件名
            String fileName = file.getName();
            /**
             * 这里是个还算重要的地方，他会给客户端返回一个他所要返回的文件的类型
             * 如果这里特殊设置为application/octet-stream，那么即使后面Content-Disposition中
             * 将文件的显示方法设置为inline，他也不会返回内嵌的东西而返回下载的数据流
             */
            String contentType = request.getServletContext().getMimeType(fileName);
            System.out.println("contentType-->" + contentType);
//        String contentType ="application/octet-stream";

            //各种响应头设置
            //参考资料：https://www.ibm.com/developerworks/cn/java/joy-down/index.html
            //坑爹地方一：看代码
            response.setHeader("Accept-Ranges", "bytes");
            if (method.equalsIgnoreCase("inline")) {
                response.setStatus(response.SC_PARTIAL_CONTENT);
            }

            if (request.getHeader("User-Agent").toLowerCase()
                    .indexOf("firefox") > 0) {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1"); // firefox浏览器
            } else if (request.getHeader("User-Agent").toUpperCase()
                    .indexOf("MSIE") > 0) {
                fileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
            } else if (request.getHeader("User-Agent").toUpperCase()
                    .indexOf("CHROME") > 0) {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");// 谷歌
            }

            response.setContentType(contentType);
            response.setHeader("Content-Type", contentType);
            //inline表示浏览器内嵌了一个文件，而还有一个种类是attachment，表示要直接下载一个文件
            //参考资料：http://hw1287789687.iteye.com/blog/2188500
            if  (method.equalsIgnoreCase("inline")) {
                response.setHeader("Content-Disposition", "inline;filename=" + fileName);
            }else {
                response.setHeader("Content-Disposition","attachment;filename="+fileName);
            }
            response.setHeader("Content-Length", String.valueOf(contentLength));

            //坑爹地方三：Content-Range，格式为
            // [要下载的开始位置]-[结束位置]/[文件总大小]
            response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());


            BufferedOutputStream outputStream = null;
            RandomAccessFile randomAccessFile = null;
            //已传送数据大小
            long transmitted = 0;
            try {
                //意思是文件打开方式为只读
                randomAccessFile = new RandomAccessFile(file, "r");
                outputStream = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[4096];
                int len = 0;
                randomAccessFile.seek(startByte);
                System.out.println("startByte->" + startByte);
                /**
                 * 坑爹地方四：判断是否到了最后不足4096（buff的length）个byte这个逻辑（(transmitted + len) <= contentLength）
                 * 要放前面！不然会会先读取randomAccessFile
                 *
                 * (transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1
                 * 的作用是判断文件没有读取完毕并且剩余的字节数大于4096
                 *
                 * 逻辑是先经过判断符合以上条件，返回到len表示已经读过的数据流的下一个字节。
                 * 在正常的情况中，len等于buff的数组长度
                 * 数据是存储再randomAccessFile中的，使用read方法将randomAccessFile的数据读入buff之中
                 * 然后再使用write方法将buff中的0到len的数据写入到输出流中
                 * 然后刷新输出流
                 */
                while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                    outputStream.write(buff, 0, len);
                    transmitted += len;
                }
                //处理不足buff.length部分
                if (transmitted < contentLength) {
                    len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                    outputStream.write(buff, 0, len);
                    transmitted += len;
                }
                outputStream.flush();
                response.flushBuffer();
                randomAccessFile.close();
                System.out.println("下载完毕：" + startByte + "-" + endByte + "：" + transmitted);

            } catch (ClientAbortException e) {
                System.out.println("用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
                //捕获此异常表示拥护停止下载
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            response.sendRedirect("/index/root");
        }
    }
}
