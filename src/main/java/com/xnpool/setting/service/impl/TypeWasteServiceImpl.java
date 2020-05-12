package com.xnpool.setting.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.TypeWasteExample;
import com.xnpool.setting.domain.pojo.TypeWaste;
import com.xnpool.setting.domain.mapper.TypeWasteMapper;
import com.xnpool.setting.service.TypeWasteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xnpool.setting.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zly
 * @since 2020-05-05
 */
@Service
public class TypeWasteServiceImpl extends ServiceImpl<TypeWasteMapper, TypeWaste> implements TypeWasteService {

    @Autowired
    private TypeWasteMapper typeWasteMapper;

    @Override
    public Object selectByOther(String mineName, String isOpen, String brand, int pageNum, int pageSize) {
        if (StringUtils.isEmpty(mineName) && StringUtils.isEmpty(isOpen) && StringUtils.isEmpty(brand)) {
            //无搜索条件
            Page<TypeWasteExample> page = new Page<>(pageNum, pageSize);
            List<TypeWasteExample> TypeWasteList = typeWasteMapper.selectByOther(page);
            page.setRecords(TypeWasteList);
            return page;
        } else {
            List<TypeWasteExample> TypeWasteList = typeWasteMapper.selectByOther(null);
            //过滤矿场
            if (!StringUtils.isEmpty(mineName)) {
                TypeWasteList = TypeWasteList.stream().filter(a -> mineName.equals(a.getMineName())).collect(Collectors.toList());
            }
            //是否启用
            if (!StringUtils.isEmpty(isOpen)) {
                TypeWasteList = TypeWasteList.stream().filter(a -> Integer.valueOf(isOpen).equals(a.getIsOpen())).collect(Collectors.toList());
            }
            //过滤品牌
            if (!StringUtils.isEmpty(brand)) {
                TypeWasteList = TypeWasteList.stream().filter(a -> brand.equals(a.getBrand())).collect(Collectors.toList());
            }
            HashMap<String, Object> startPage = PageUtil.startPage(TypeWasteList, pageNum, pageSize);
            return startPage;
        }

    }

    @Override
    public HashMap<String, Object> drop_list() {
        HashSet<String> mineNameSet = new HashSet<>();
        HashSet<String> brandSet = new HashSet<>();
        List<TypeWasteExample> TypeWasteList = typeWasteMapper.selectByOther(null);
        TypeWasteList.forEach(typeWasteExample -> {
            mineNameSet.add(typeWasteExample.getMineName());
            brandSet.add(typeWasteExample.getBrand());
        });
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("mineName",mineNameSet);
        resultMap.put("brand",brandSet);
        return resultMap;
    }
}
