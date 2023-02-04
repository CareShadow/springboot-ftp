package com.example.utils;

import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MinioUtils
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/2/4 8:43
 * @Version 1.0
 **/

@Service
@Slf4j
public class MinioUtils {

    private final String bucket;
    private final MinioClient minioClient;

    @Value("${minio.expiry}")
    private Integer expiry;

    public MinioUtils(@Value("${minio.url}") String url,
                        @Value("${minio.access}") String access,
                        @Value("${minio.secret}") String secret,
                        @Value("${minio.bucket}") String bucket) throws Exception {
        this.bucket = bucket;
        minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(access, secret)
                .build();
        // 初始化Bucket
        initBucket();
    }

    private void initBucket() throws Exception {
        // 应用启动时检测Bucket是否存在
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        // 如果Bucket不存在，则创建Bucket
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            log.info("成功创建 Bucket [{}]", bucket);
        }
    }

    /**
     * 上传文件
     *
     * @param is          输入流
     * @param object      对象（文件）名
     * @param contentType 文件类型
     */
    public void putObject(InputStream is, String object, String contentType) throws Exception {
        long start = System.currentTimeMillis();
//        minioClient.putObject(PutObjectArgs.builder()
//                .bucket(bucket)
//                .object(object)
//                .contentType(contentType)
//                .stream(is, -1, 1024 * 1024 * 10) // 不得小于 5 Mib
//                .build());
        minioClient.putObject(
                PutObjectArgs.builder().bucket("file").object("path/to/").stream(
                        new ByteArrayInputStream(new byte[] {}), 0, -1)
                        .build());
        log.info("成功上传文件至云端 [{}]，耗时 [{} ms]", object, System.currentTimeMillis() - start);
    }

    /**
     * @Description 下载文件
     * @Param [object]
     * @Return io.minio.GetObjectResponse
     * @Date 2023/2/4 15:57
     * @Author CareShadow
     * @Version 1.0
     **/
    public GetObjectResponse getObject(String object) throws Exception {
        long start = System.currentTimeMillis();
        GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build());
        log.info("成功获取 Object [{}]，耗时 [{} ms]", object, System.currentTimeMillis() - start);
        return response;
    }

   /**
    * @Description 删除MinIO中的文件
    * @Param [object]
    * @Return void
    * @Date 2023/2/4 15:56
    * @Author CareShadow
    * @Version 1.0
    **/
    public void removeObject(String object) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build());
        log.info("成功删除 Object [{}]", object);
    }


    /**
     * @Description 获取视频和图片的时效预览地址
     * @Param [bucket, object]
     * @Return java.lang.String
     * @Date 2023/2/4 16:04
     * @Author CareShadow
     * @Version 1.0
     **/
    public String getPreViewUrl(String bucket, String object) throws Exception{
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .object(object)
                .expiry(expiry, TimeUnit.DAYS)
                .build());
        log.info("文件路径[{}]", url);
        return url;
    }
}

