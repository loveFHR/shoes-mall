package com.ewan.controller;
import cn.hutool.core.io.FileUtil;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.FileConstant;
import com.ewan.exception.BusinessException;
import com.ewan.service.UserService;
import com.ewan.utils.CommonUtils;
import com.ewan.utils.MinioUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * 对象存储服务
 *
 * @author cx
 * @date 2024/5/14
 */
@RestController
@RequestMapping("/upload")
public class FileUploadController {
    @Resource
    private UserService userService;
    /**
     * 单张图片 最大300MB
     *
     * @param file 图片文件
     * @return url
     */
    @PostMapping("/one")
    public BaseResponse<String> updateFile(MultipartFile file, HttpServletRequest request) {//        userService.getLoginUser(request);
        String fileName = CommonUtils.getRandomStr() + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        try {
            MinioUtils.putObject(FileConstant.BUCKET_NAME, file, fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResultUtils.success(FileConstant.HEAD_URL + FileUtil.FILE_SEPARATOR + FileConstant.BUCKET_NAME + FileUtil.FILE_SEPARATOR + fileName);
    }
    /**
     * 批量图片  总量最大2700MB(total)
     *
     * @param files 图片文件（数组）
     * @return url
     */
    @PostMapping("/batch")
    public BaseResponse<List<String>> updateFileBatch(MultipartFile[] files, HttpServletRequest request) {//        userService.getLoginUser(request);
        List<String> list = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String fileName = CommonUtils.getRandomStr() + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
                MinioUtils.putObject(FileConstant.BUCKET_NAME, file, fileName);
                list.add(FileConstant.HEAD_URL + FileUtil.FILE_SEPARATOR + FileConstant.BUCKET_NAME + FileUtil.FILE_SEPARATOR + fileName);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        return ResultUtils.success(list);
    }
}
    
    