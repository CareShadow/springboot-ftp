package com.example.service;

import com.example.entity.MyFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.FileVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
public interface MyFileService extends IService<MyFile> {
    List<FileVO> getFileList(Integer id, Long userId);
    Integer getFileCount(Integer id);
    boolean batchDelete(List<Integer> ids);
}
