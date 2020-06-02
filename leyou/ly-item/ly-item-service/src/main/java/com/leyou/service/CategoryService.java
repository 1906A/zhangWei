package com.leyou.service;

import com.leyou.dao.CategoryMapper;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryMapper mapper;

    /**
     * 查询所有分类
     *
     * @param category
     * @return
     */
    public List<Category> findCategory(Category category) {
        return mapper.select(category);
    }

    /**
     * 商品分类添加
     *
     * @param category
     * @return
     */
    public Integer categoryAdd(Category category) {
        return mapper.insert(category);
    }

    /**
     * 商品分类修改
     *
     * @param category
     * @return
     */
    public Integer categoryUpdate(Category category) {
        return mapper.updateByPrimaryKey(category);
    }

    /**
     * 商品分类删除
     *
     * @param id
     * @return
     */
    public Integer categoryDelete(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据分类id查询分类名
     *
     * @param id
     * @return
     */
    public Category findByCategoryId(Long id) {
       return mapper.selectByPrimaryKey(id);
    }
}
