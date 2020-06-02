package com.leyou.controller;

import com.leyou.pojo.SpecParam;
import com.leyou.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("specParam")
@RestController
public class SpecParamController {
    @Autowired
    private SpecParamService specParamService;


    /**
     * 根据组id查询对应的参数
     *
     * @param gid
     * @return
     */
    @RequestMapping("params")
    public List<SpecParam> selectSpecParamByGid(@RequestParam("gid") Long gid) {
        return specParamService.selectSpecParamByGid(gid);
    }

    /**
     * 根据分类id查询对应的参数
     *
     * @param id
     * @return
     */
    @RequestMapping("params/{id}")
    public List<SpecParam> selectSpecParamByCid(@PathVariable("id") Long id) {
        return specParamService.selectSpecParamByCid(id);
    }

    /**
     * * 根据分类id+搜索条件为1的参数查询对应的参数
     *
     * @param cid
     * @return
     */
    @RequestMapping("paramsByCid")
    public List<SpecParam> selectSpecParamByCidAndSearching(@RequestParam("cid") Long cid) {
        return specParamService.selectSpecParamByCidAndSearching(cid);
    }

    /**
     * 添加商品分组参数
     *
     * @param specParam
     * @return
     */
    @RequestMapping("param")
    public void addSpecParam(@RequestBody SpecParam specParam) {
        if (specParam.getId() != null) {
            specParamService.updateSpecParam(specParam);
        } else {
            specParamService.addSpecParam(specParam);
        }
    }

    /**
     * 删除商品分组参数
     *
     * @param id
     * @return
     */
    @RequestMapping("param/{id}")
    public void addSpecParam(@PathVariable("id") Long id) {
        specParamService.deleteSpecParam(id);
    }
}
