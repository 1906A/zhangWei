package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.PageResult;
import com.leyou.dao.BrandMapper;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    BrandMapper mapper;

    /**
     * 品牌管理的分页查询
     * 通用mapper的分页
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    public PageResult<Brand> findBrand(
            String key,
            Integer page,
            Integer rows,
            String sortBy,
            boolean desc
    ) {
        PageHelper.startPage(page, rows);

        Example example = new Example(Brand.class);
        if (desc == false) {
            example.setOrderByClause(sortBy + " desc");
        } else {
            example.setOrderByClause(sortBy + " asc");
        }
        if (key != null) {
            example.or().andLike("name", "%" + key + "%");
            example.or().andEqualTo("letter", key);
        }
        List<Brand> select = mapper.selectByExample(example);
        PageInfo<Brand> info = new PageInfo<Brand>(select);

        return new PageResult<Brand>(info.getTotal(), info.getList());
    }

    /**
     * 手写分页
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    public PageResult<Brand> findBrandLimit(
            String key,
            Integer page,
            Integer rows,
            String sortBy,
            boolean desc
    ) {
        //查询的集合
        List<Brand> select = mapper.findBrandLimit(key, (page - 1) * rows, rows, sortBy, desc);
        //总条数 total
        Long total = mapper.findBrandCount(key, sortBy, desc);
        return new PageResult<Brand>(total, select);
    }

    /**
     * 品牌管理添加
     *
     * @param brand
     * @param cids
     * @return
     */
    public void BrandAdd(Brand brand, List<String> cids) {
        //添加品牌表数据,实体类中添加了注解,可以返回主键
        mapper.insert(brand);
//        System.out.println(brand.getId());
        //分类表是多个,使用lambda表达式,循环添加中间表数据,id是便利的变量名
        cids.forEach(id -> {
            mapper.addBrandAndCategory(Long.parseLong(id), brand.getId());
        });

    }

    /**
     * 品牌管理修改
     *
     * @param brand
     * @return
     */
    public void BrandUpdate(Brand brand, List<String> cids) {
        //修改品牌表
        mapper.updateByPrimaryKey(brand);
        //修改分类表
        //删除再添加等同于修改
        mapper.deleteCategoryAndBrand(brand.getId());
        cids.forEach(cid -> {
            mapper.addBrandAndCategory(Long.parseLong(cid), brand.getId());
        });

    }

    /**
     * 品牌管理删除
     *
     * @param id
     * @return
     */
    public void BrandDelete(Long id) {
        //删除brand指定数据
        mapper.deleteByPrimaryKey(id);
        //删除连接表 tb_category_brand
        mapper.deleteCategoryAndBrand(id);
    }

    /**
     * 根据品牌id通过中间表返回指定分类对象
     *
     * @param pid
     */
    public List<Category> findCategoryByBrandId(Long pid) {
        return mapper.findCategoryByBrandId(pid);
    }

    /**
     * 根据分类id通过中间表返回指定品牌对象
     *
     * @param id
     */
    public List<Brand> findBrandByCategoryId(Long id) {
        return mapper.findBrandByCategoryId(id);
    }

    /**
     * 根据品牌id查询品牌对象
     *
     * @param id
     * @return
     */
    public Brand findByBrandId(Long id) {
       return mapper.selectByPrimaryKey(id);
    }
}
