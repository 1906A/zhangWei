package com.leyou.service;

import com.leyou.dao.SpecParamMapper;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamService {
    @Autowired
    private SpecParamMapper mapper;


    /**
     * 根据组id查询对应的参数
     *
     * @param gid
     * @return
     */
    public List<SpecParam> selectSpecParamByGid(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        return mapper.select(specParam);
    }

    /**
     * 添加商品规格参数
     *
     * @param specParam
     * @return
     */
    public void addSpecParam(SpecParam specParam) {
        mapper.insert(specParam);
    }

    /**
     * 修改商品规格参数
     *
     * @param specParam
     * @return
     */
    public void updateSpecParam(SpecParam specParam) {
        mapper.updateByPrimaryKey(specParam);
    }

    /**
     * 删除商品规格参数
     *
     * @param id
     * @return
     */
    public void deleteSpecParam(Long id) {
        mapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据分类id查询对应的参数
     *
     * @param cid
     * @return
     */
    public List<SpecParam> selectSpecParamByCid(Long cid) {
        SpecParam param = new SpecParam();
        param.setCid(cid);
        return mapper.select(param);
    }

    /**
     * 根据分类id+搜索条件为1的参数查询对应的参数
     *
     * @param cid
     * @return
     */
    public List<SpecParam> selectSpecParamByCidAndSearching(Long cid) {
        SpecParam param = new SpecParam();
        param.setCid(cid);
        param.setSearching(true);
        return mapper.select(param);
    }


    /**
     * * 根据分类id+是否通用参数的值查询
     *
     * @param cid
     * @return
     */
    public List<SpecParam> selectSpecParamByCidAndGeneric(Long cid, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setGeneric(generic);
      return mapper.select(specParam);
    }
}
