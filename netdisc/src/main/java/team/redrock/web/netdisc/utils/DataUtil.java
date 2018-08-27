package team.redrock.web.netdisc.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataUtil {


    public static ArrayList<Map> get_data(String target) {
        ArrayList<Map> file_array = new ArrayList<Map>();
        File file = null;
        try {
            file = new File(target);
        } catch (Exception e) {
            Map<String, Object> empty_map = new HashMap<>();
            empty_map.put("file_name", "此文件夹为空");
            empty_map.put("size", "");
            empty_map.put("last_modified", "");
            empty_map.put("file_path", "#");
            empty_map.put("file_type", "");
            file_array.add(empty_map);
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            FormatUtil formatUtil = new FormatUtil();
            Map<String, Object> empty_map = new HashMap<>();
            empty_map.put("file_name", "返回上一级目录");
            empty_map.put("size", "");
            empty_map.put("file_absolute_path","false");
            empty_map.put("file_delete_path","false");
            empty_map.put("last_modified", "");
            empty_map.put("file_path", target.substring(target.indexOf(":")+1,target.lastIndexOf("/")));
            empty_map.put("file_type", "");
            file_array.add(empty_map);


            for (File filedemo : files) {
                Map<String, Object> map = new HashMap<>();
                map.put("file_name", filedemo.getName());
                if (filedemo.isDirectory()) {
                    map.put("file_type", "Directory");
                    map.put("size", "");
                    map.put("file_absolute_path","false");
                } else {
                    map.put("file_type", filedemo.getName().substring(filedemo.getName().lastIndexOf(".") + 1));
                    map.put("size", formatUtil.getNetFileSizeDescription(filedemo.length()));
                    map.put("file_absolute_path",filedemo.getAbsolutePath().replace("\\","/"));
                }
                map.put("file_delete_path",filedemo.getAbsolutePath().replace("\\","/"));
                map.put("last_modified", formatUtil.timeStamptoDate(filedemo.lastModified()));
                map.put("file_path", filedemo.getAbsolutePath().substring(filedemo.getAbsolutePath().indexOf(":") + 1).replace("\\","/"));
                file_array.add(map);
            }

            return file_array;
        } else {
            return file_array;
        }
    }
}
