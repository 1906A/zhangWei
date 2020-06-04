package com.leyou.lyUpdaload.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("upload")
public class UploadController {
    //从配置文件动态获取ip地址
    @Value("${user.httpImage}")
    private String httpImage;

    public static final List<String> FILE_TYPE = Arrays.asList("jpg", "png");

    @RequestMapping("image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            //验证图片格式
            String filename = file.getOriginalFilename();
            //获取后缀类型
            String type = filename.substring(filename.lastIndexOf(".") + 1);
            if (!FILE_TYPE.contains(type)) {
                return null;
            }
            //验证图片内容
            BufferedImage read = ImageIO.read(file.getInputStream());
            if (read == null) {//没有读到东西
                return null;
            }
            String path = System.currentTimeMillis() + file.getOriginalFilename();
            //验证图片大小 通过yml文件配置
            file.transferTo(new File("D:\\imges\\" + path));

            return httpImage + path;

           /*

           //加载客户端配置文件，配置文件中指明了tracker服务器的地址
            ClientGlobal.init("fastdfs.conf");
            //验证配置文件是否加载成功
            System.out.println(ClientGlobal.configInfo());

            //创建TrackerClient对象，客户端对象
            TrackerClient trackerClient = new TrackerClient();

            //获取到调度对象，也就是与Tracker服务器取得联系
            TrackerServer trackerServer = trackerClient.getConnection();

            //创建存储客户端对象
            StorageClient storageClient = new StorageClient(trackerServer, null);
//上传非死的图片
            String[] upload_file = storageClient.upload_file(file.getBytes(), type, null);

            for (String string : upload_file) {
                System.out.println(string);
            }


            return httpImage + upload_file[0]+"/"+upload_file[1];
*/
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }
}
