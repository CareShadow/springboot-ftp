package com.example.pojo;

import com.example.entity.Resource;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ResourceVO
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/5/9 22:22
 * @Version 1.0
 **/
@Data
public class ResourceVO implements Serializable {
    private Integer roleId;
    private String roleName;
    private List<Resource> resourceList;
}
