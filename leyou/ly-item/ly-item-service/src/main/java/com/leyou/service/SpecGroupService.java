package com.leyou.service;

import com.leyou.dao.SpecGroupMapper;
import com.leyou.pojo.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupService {
    @Autowired
    private SpecGroupMapper mapper;

    /**
     * 保存商品规格组
     *
     * @param specGroup
     * @return
     */
    public Integer saveSpecGroup(SpecGroup specGroup) {
        return mapper.insert(specGroup);
    }

    /**
     * 查询商品组
     *
     * @param cid
     */
    public List<SpecGroup> selectSpecGroup(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return mapper.select(specGroup);
    }

    /**
     * 删除商品规格组
     *
     * @param id
     */
    public void deleteSpecGroup(long id) {
        mapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改商品规格组
     *
     * @param specGroup
     */
    public void updateSpecGroup(SpecGroup specGroup) {
        mapper.updateByPrimaryKey(specGroup);
    }

}
