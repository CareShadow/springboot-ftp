package com.example.service;

import com.example.entity.FileFolder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.FileVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxl
 * @since 2022-02-11
 */
public interface FileFolderService extends IService<FileFolder> {
    List<FileVO> getFolderList(Integer id);
    Integer getFolderCount(Integer id);
}
