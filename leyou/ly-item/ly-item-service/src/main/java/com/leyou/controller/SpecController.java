package com.leyou.controller;

import com.leyou.pojo.SpecGroup;
import com.leyou.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("spec")
@RestController
public class SpecController {
    @Autowired
    private SpecGroupService specGroupService;

    /**
     * 查询规格参数列表
     *
     * @param cid
     */
    @RequestMapping("groups/{cid}")
    public List<SpecGroup> selectSpecGroups(@PathVariable("cid") Long cid) {
        return specGroupService.selectSpecGroup(cid);
    }

    /**
     * 保存商品规格
     *
     * @param specGroup
     */
    @RequestMapping("group")
    public void saveSpecGroup(@RequestBody SpecGroup specGroup) {
        if (specGroup.getId() == null) {
            specGroupService.saveSpecGroup(specGroup);
        } else {
            specGroupService.updateSpecGroup(specGroup);
        }
    }

    /**
     * 删除商品规格
     *
     * @param id
     */
    @RequestMapping("group/{id}")
    public void deleteSpecGroup(@PathVariable("id") long id) {
        specGroupService.deleteSpecGroup(id);
    }

}
