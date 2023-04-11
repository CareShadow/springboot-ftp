package com.example.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
     * @param is     输入流
     * @param object 对象（文件）名
     */
    public void putObject(InputStream is, String object) throws Exception {
        long start = System.currentTimeMillis();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket("slice")
                .object(object)
                .stream(is, -1, 1024 * 1024 * 10) // 不得小于 5 Mib
                .build());
        log.info("成功上传文件至云端 [{}]，耗时 [{} ms]", object, System.currentTimeMillis() - start);
    }


    public void putObject(InputStream is, String object, String contentType) throws Exception {
        long start = System.currentTimeMillis();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .stream(is, -1, 1024 * 1024 * 10) // 不得小于 5 Mib
                .contentType(contentType)
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
    public InputStream getObject(String bucket,String object) throws Exception {
        long start = System.currentTimeMillis();
        InputStream in = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build());
        log.info("成功获取 Object [{}]，耗时 [{} ms]", object, System.currentTimeMillis() - start);
        return in;
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
    public String getPreViewUrl(String bucket, String object) throws Exception {
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .object(object)
                .expiry(expiry, TimeUnit.DAYS)
                .build());
        log.info("文件路径[{}]", url);
        return url;
    }

    /**
     * @Description 判断文件是否存在，及判断文件信息是否存在 true false
     * @Param [fileMD5]
     * @Return boolean
     * @Date 2023/2/8 22:26
     * @Author CareShadow
     * @Version 1.0
     **/
    public boolean JudgeFileMD5(String path, String fileName) throws Exception {
        StatObjectResponse statObject = minioClient.statObject(StatObjectArgs.builder()
                .bucket(bucket)
                .object(path + fileName)
                .build());
        log.debug("文件是否存在： {}", statObject);
        return statObject != null;
    }

    /**
     * @Description 获取已经上传的分片序号列表
     * @Param [fileMD5]
     * @Return java.util.List<java.lang.Integer>
     * @Date 2023/3/8 22:20
     * @Author CareShadow
     * @Version 1.0
     **/
    public List<Integer> getUploaderChunk(String fileMD5) throws Exception {
        // 计算出文件路径出来
        String path = fileMD5 + "/chunks/";
        Iterable<Result<Item>> items = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket("slice")
                .prefix(path)
                .build());
        Iterator<Result<Item>> iterator = items.iterator();
        List<Integer> result = new ArrayList<>();
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            result.add(Integer.valueOf(item.objectName().replace(path, "")));
        }
        log.debug("已上传文件列表：{}", result);
        return result;
    }

    /**
     * @Description 在MinIO端创建文件路径
     * @Param [folderPath]
     * @Return io.minio.ObjectWriteResponse
     * @Date 2023/3/22 21:39
     * @Author CareShadow
     * @Version 1.0
     **/
    public ObjectWriteResponse createFolderPath(String folderPath) throws Exception {
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                .stream(new ByteArrayInputStream(new byte[]{}, 0, -1), -1, 1024 * 1024 * 10)
                .bucket(bucket)
                .object(folderPath)
                .build()
        );
        return objectWriteResponse;
    }

    public void removeFolder(String bucket, String path) throws Exception {
        // 列出指定前缀的文件对象路径
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(path)
                .build());
        List<DeleteObject> objects = new LinkedList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            objects.add(new DeleteObject(item.objectName(), item.objectName()));
        }

        // 批量删除对象
        Iterable<Result<DeleteError>> deleteErrors = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucket)
                .objects(objects)
                .build());
    }
}

