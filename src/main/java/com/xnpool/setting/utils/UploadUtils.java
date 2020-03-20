package com.xnpool.setting.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.xnpool.setting.common.BaseController.FAIL;
import static com.xnpool.setting.common.BaseController.SUCCESS;

/**
 * @Description 文件上传工具类
 * @Author zly
 * @Date 13:14 2020/2/7
 * @Param
 * @return
 */
@Component
@Slf4j
public class UploadUtils {



    /**
     * @return
     * @Description 检查文件
     * @Author zly
     * @Date 13:20 2020/2/7
     * @Param
     */
    private static ResponseResult checkFile(@RequestParam("file") MultipartFile file) {
        //判断是否为空
        if (file.isEmpty()) {
            return new ResponseResult(501, "上传失败!文件不能为空!");
        }
        //文件名
        String fileName = file.getOriginalFilename();
        //后缀名检查
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!suffixName.contains("doc") && !suffixName.contains("docx") && !suffixName.contains("pdf")) {
            return new ResponseResult(501, "只能上传word文档或者pdf文档");
        }

        //文件大小检查
        if (file.getSize() > 10485760) {
            return new ResponseResult(501, "文件大小不能超过10M");
        }
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 上传文件到服务器上
     * @Author zly
     * @Date 13:24 2020/2/7
     * @Param
     * @return
     */
    public static ResponseResult getFileToUpload(@RequestParam("file") MultipartFile file,String filePath) {
        ResponseResult resp = checkFile(file);
        if (200==resp.getStatus()) {
            try {
                //上传文件到服务器上
                String path = System.getProperty("user.dir") + filePath;
                uploadFile(file, file.getOriginalFilename(), path);
                log.info("文件上传的位置:"+path);
                return new ResponseResult(SUCCESS, "上传文件成功");
            } catch (FileNotFoundException e) {
                log.info("文件上传异常:" + e.getMessage());
                return new ResponseResult(FAIL, "文件上传出错,请重试!"+e.getMessage());
            } catch (IOException e) {
                log.info("文件上传异常:" + e.getMessage());
                return new ResponseResult(FAIL, "文件上传出错,请重试!"+e.getMessage());
            }
        }else {
            return new ResponseResult(resp.getStatus(), resp.getMessage());
        }
    }

    /**
     * @return
     * @Description 文件上传功能
     * @Author zly
     * @Date 11:13 2020/2/7
     * @Param
     */
    private static void uploadFile(MultipartFile upload, String uploadFileName, String path) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = upload.getInputStream();
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            os = new FileOutputStream(path + System.getProperty("file.separator") + uploadFileName);
            byte buffer[] = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            File f = new File(path + System.getProperty("file.separator") + uploadFileName);
            if (f.exists()) {
                f.delete();
            }
        } finally {
            os.close();
            is.close();
        }
    }
}

