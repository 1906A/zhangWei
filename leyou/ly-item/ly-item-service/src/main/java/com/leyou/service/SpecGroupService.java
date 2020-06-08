package com.leyou.service;

import com.leyou.dao.SpecGroupMapper;
import com.leyou.dao.SpecParamMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupService {
    @Autowired
    private SpecGroupMapper mapper;
    @Autowired
    private SpecParamMapper specParamMapper;

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
//根据分类id 查询规格参数及组内的参数列表

        specGroup.setCid(cid);

        List<SpecGroup> groupList = mapper.select(specGroup);

        groupList.forEach(group -> {

            SpecParam param = new SpecParam();

            param.setGroupId(group.getId());
//            List<SpecParam> paramList = specParamMapper.select(param);
            group.setSpecParamList(specParamMapper.select(param));
        });
        return groupList;
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
