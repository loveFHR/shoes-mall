package com.ewan.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Minio工具类
 *
 * @author : Ewan
 */
public class MinioUtils {

    private static MinioClient minioClient;
    private static String endpoint;
    private static String accessKey;
    private static String secretKey;

    private static final String SEPARATOR = "/";

    private MinioUtils() {
    }

    public MinioUtils(String endpoint, String accessKey, String secretKey) {
        MinioUtils.endpoint = endpoint;
        MinioUtils.accessKey = accessKey;
        MinioUtils.secretKey = secretKey;
        createMinioClient();
    }

    /**
     * 创建minioClient
     */
    public void createMinioClient() {
        try {
            if (null == minioClient) {
                minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
                System.err.println("创建Minio通道成功!");
            }
        } catch (Exception e) {
            System.err.println("创建Minio通道失败!");
        }
    }

    /**
     * 获取上传文件的基础路径
     *
     * @return url
     */
    public static String getBasisUrl(String bucketName) {
        return endpoint + SEPARATOR + bucketName + SEPARATOR;
    }

    /**
     * 验证bucketName是否存在
     *
     * @return boolean true:存在
     */
    public static boolean bucketExists(String bucketName)
            throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    public static void createBucket(String bucketName)
            throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 获取全部bucket
     */
    public static List<Bucket> getAllBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */
    public static Optional<Bucket> getBucket(String bucketName) throws Exception {
        return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据bucketName删除信息
     *
     * @param bucketName bucket名称
     */
    public static void removeBucket(String bucketName) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 对象
     * @return true：存在
     */
    public static boolean doesObjectExist(String bucketName, String objectName) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件夹名称（去掉/）
     * @return true：存在
     */
    public static boolean doesFolderExist(String bucketName, String objectName) {
        boolean exist = false;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).prefix(objectName).recursive(false).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir() && objectName.equals(item.objectName())) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    public static List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws Exception {
        List<Item> list = new ArrayList<>();
        Iterable<Result<Item>> objectsIterator = minioClient.listObjects(
                ListObjectsArgs.
                        builder().
                        bucket(bucketName).
                        prefix(prefix).
                        recursive(recursive).
                        build());
        if (objectsIterator != null) {
            for (Result<Item> o : objectsIterator) {
                Item item = o.get();
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 通过MultipartFile，上传文件
     *
     * @param bucketName  存储桶
     * @param file        文件
     * @param objectName  对象名
     * @param contentType 文件类型
     */
    public static ObjectWriteResponse putObject(String bucketName, MultipartFile file, String objectName, String contentType) throws Exception {
        InputStream inputStream = file.getInputStream();
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).contentType(contentType)
                        .stream(inputStream, inputStream.available(), -1).build());
    }

    /**
     * 通过MultipartFile，上传文件
     *
     * @param bucketName 存储桶
     * @param file       文件
     * @param objectName 对象名
     */
    public static ObjectWriteResponse putObject(String bucketName, MultipartFile file, String objectName) throws Exception {
        InputStream inputStream = file.getInputStream();
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName)
                        .stream(inputStream, inputStream.available(), -1).build());
    }

    /**
     * 上传本地文件
     *
     * @param bucketName 存储桶
     * @param objectName 对象名称
     * @param fileName   本地文件路径
     */
    public static ObjectWriteResponse putObject(String bucketName, String objectName, String fileName)
            throws Exception {
        return minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName).object(objectName).filename(fileName).build());
    }

    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     */
    public static ObjectWriteResponse putObject(String bucketName, String objectName,
                                                InputStream inputStream)
            throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                        inputStream, inputStream.available(), -1).build());
    }

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径
     */
    public static ObjectWriteResponse putDirObject(String bucketName, String objectName)
            throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                        new ByteArrayInputStream(new byte[]{}), 0, -1).build());
    }

    /**
     * 拷贝文件
     *
     * @param bucketName    bucket名称
     * @param objectName    文件名称
     * @param srcBucketName 目标bucket名称
     * @param srcObjectName 目标文件名称
     */
    public static ObjectWriteResponse copyObject(String bucketName, String objectName,
                                                 String srcBucketName, String srcObjectName)
            throws Exception {
        return minioClient.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder().bucket(bucketName).object(objectName).build())
                        .bucket(srcBucketName)
                        .object(srcObjectName)
                        .build());
    }

    /**
     * 文件下载
     *
     * @param bucketName 桶名称
     * @param request    请求
     * @param response   请求响应
     */
    public static void downloadFile(String bucketName, String originalName,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        try {
            InputStream file = getObject(bucketName, originalName);
            //文件名乱码处理
            String useragent = request.getHeader("USER-AGENT").toLowerCase();
            if (useragent.contains("msie") || useragent.contains("like gecko") || useragent.contains("trident")) {
                originalName = URLEncoder.encode(originalName, StandardCharsets.UTF_8.displayName());
            } else {
                originalName = new String(originalName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + originalName);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = file.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            file.close();
            servletOutputStream.close();
        } catch (Exception e) {
            System.err.println(String.format("下载文件:%s异常", originalName));
        }
    }

    /**
     * 获取文件流
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    public static InputStream getObject(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 断点下载
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度
     * @return 流
     */
    public InputStream getObject(String bucketName, String objectName, long offset, long length)
            throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build());
    }


    /**
     * 获取路径下文件列表
     *
     * @param bucketName bucket名称
     * @param prefix     文件名称
     * @param recursive  是否递归查找，如果是false,就模拟文件夹结构查找
     * @return 二进制流
     */
    public static Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive) {
        return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build());
    }

    /**
     * 获取路径下文件列表
     *
     * @param bucketName bucket名称
     * @param recursive  是否递归查找，如果是false,就模拟文件夹结构查找
     * @return 二进制流
     */
    public static Iterable<Result<Item>> listObjects(String bucketName, boolean recursive) {
        return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).recursive(recursive).build());
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     */
    public static void removeObject(String bucketName, String objectName)
            throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 批量删除文件
     *
     * @param bucketName bucket
     * @param keys       需要删除的文件列表
     */
    public static void removeObjects(String bucketName, List<String> keys) {
        List<DeleteObject> objects = new LinkedList<>();
        keys.forEach(s -> {
            objects.add(new DeleteObject(s));
            try {
                removeObject(bucketName, s);
            } catch (Exception e) {
                System.err.println("批量删除失败!");
            }
        });
    }

    /**
     * 生成预览链接，最大7天有效期；
     * 如果想永久有效，在 minio 控制台设置仓库访问规则总几率
     *
     * @param object      文件名称
     * @param contentType 预览类型 image/gif", "image/jpeg", "image/jpg", "image/png", "application/pdf
     * @param validTime   有效时间 不能超过7天
     * @param timeUnit    单位 时 分 秒 天
     * @return java.lang.String
     **/
    public static String getPreviewUrl(String bucketName, String object, String contentType, Integer validTime, TimeUnit timeUnit) {
        Map<String, String> reqParams = null;
        if (contentType != null) {
            reqParams = new HashMap<>();
            reqParams.put("response-content-type", contentType != null ? contentType : "application/pdf");
        }
        String url = null;
        try {
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(object)
                            .expiry(validTime, timeUnit)
                            .extraQueryParams(reqParams)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Description 文件列表
     *
     * @param limit 范围 1-1000
     **/
    public List<Item> listObjects(int limit, String bucketName) {
        List<Item> objects = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .maxKeys(limit)
                        .includeVersions(true)
                        .build());
        try {
            for (Result<Item> result : results) {
                objects.add(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    /**
     * Description 网络文件转储 minio
     *
     * @param httpUrl 文件地址
     **/
    public static void netToMinio(String httpUrl, String bucketName) {
        int i = httpUrl.lastIndexOf(".");
        String substring = httpUrl.substring(i);
        URL url;
        try {
            url = new URL(httpUrl);
            URLConnection urlConnection = url.openConnection();
            // agent 模拟浏览器
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            // 临时文件转储
            File tempFile = File.createTempFile(UUID.randomUUID().toString().replace("-", ""), substring);
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            // 上传minio
            putObject(bucketName, tempFile.getAbsolutePath(), tempFile.getName());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description 文件转字节数组
     *
     * @param path 文件路径
     * @return byte[] 字节数组
     **/
    public byte[] fileToBytes(String path) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            fis = new FileInputStream(path);
            int temp;
            byte[] bt = new byte[1024 * 10];
            while ((temp = fis.read(bt)) != -1) {
                bos.write(bt, 0, temp);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (Objects.nonNull(fis)) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    /**
     * 将URLDecoder编码转成UTF8
     */
    public static String getUtf8ByURLDecoder(String str) throws UnsupportedEncodingException {
        String url = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }
}

