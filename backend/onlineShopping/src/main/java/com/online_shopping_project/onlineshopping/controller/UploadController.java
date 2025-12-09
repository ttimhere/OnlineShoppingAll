package com.online_shopping_project.onlineshopping.controller;
import com.online_shopping_project.onlineshopping.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
/**
 * 文件上传控制层，用于处理文件的上传请求。
 * 上传后的文件会存储在服务器本地的 uploads 目录下，并通过静态资源映射方式对外提供访问。
 */
@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    private static final String ROOT_DIR = "/usr/local/java/uploads";
    private static final String PRODUCT_DIR = "product";//商品图片目录路径
    //上传图片接口：处理前端传来的文件，保存到本地并返回文件访问 URL。
    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {//检查文件是否为空
            return Result.error(400, "文件为空");
        }
        //构建按日期分目录的路径
        String dateDir = LocalDate.now().toString();
        File dir = new File(ROOT_DIR + File.separator + PRODUCT_DIR + File.separator + dateDir);
        if (!dir.exists() && !dir.mkdirs()) {// 若目录不存在则创建，创建失败则返回错误
            return Result.error(500, "创建目录失败");}
        //生成唯一文件名，防止重名覆盖
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString().replace("-", "");
        String finalName = ext == null ? filename : filename + "." + ext;
        //保存文件到目标目录
        File dest = new File(dir, finalName);
        file.transferTo(dest);
        //生成可公开访问的 URL
        String publicUrl = "/product/" + dateDir + "/" + finalName;// 由静态资源映射对外暴露
        //封装返回数据
        Map<String, String> data = new HashMap<>();
        data.put("url", publicUrl);
        return Result.success(data);//返回成功结果
    }
}
