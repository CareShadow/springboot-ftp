package com.example.service.impl;

import com.example.entity.ApplicationConfig;
import com.example.dao.ApplicationConfigMapper;
import com.example.service.ApplicationConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
@Service
public class ApplicationConfigServiceImpl extends ServiceImpl<ApplicationConfigMapper, ApplicationConfig> implements ApplicationConfigService {

}
