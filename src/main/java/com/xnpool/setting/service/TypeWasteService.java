package com.xnpool.setting.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.TypeWasteExample;
import com.xnpool.setting.domain.pojo.TypeWaste;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zly
 * @since 2020-05-05
 */
public interface TypeWasteService extends IService<TypeWaste> {

    Object selectByOther(String mineName, String isOpen, String brand, int pageNum, int pageSize);

    HashMap<String, Object> drop_list();
}
