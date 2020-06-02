package com.leyou.controller;

import com.leyou.pojo.Category;
import com.leyou.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryService service;


    /**
     * 根据节点id查询,为0就查询所有
     *
     * @param pid
     * @return
     */
    @RequestMapping("list")
    public List<Category> list(@RequestParam("pid") long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return service.findCategory(category);
    }

    /**
     * 添加商品分类
     *
     * @param category
     * @return
     */
    @RequestMapping("add")
    public String add(@RequestBody Category category) {
        String result = "succ";
        try {
            Integer integer = service.categoryAdd(category);
            if (integer != 1) {
                result = "fall";
            }
        } catch (Exception e) {
            log.info("添加商品分类错误");
            result = "fall";
        }
        return result;
    }

    /**
     * 修改商品分类
     *
     * @param category
     * @return
     */
    @RequestMapping("update")
    public String update(@RequestBody Category category) {
        String result = "succ";
        try {
            Integer integer = service.categoryUpdate(category);
            if (integer != 1) {
                result = "fall";
            }
        } catch (Exception e) {
            log.info("添加商品分类错误");
            result = "fall";
        }
        return result;
    }

    /**
     * 根据指定ID删除商品分类
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteById")
    public String deleteById(@RequestParam("id") Long id) {
        String result = "succ";
        try {
            Integer integer = service.categoryDelete(id);
            if (integer != 1) {
                result = "fall";
            }
        } catch (Exception e) {
            log.info("添加商品分类错误");
            result = "fall";
        }
        return result;
    }

    /**
     * 根据分类id查询分类名
     * @param id
     * @return
     */
    @RequestMapping("findByCategoryId")
    public Category findByCategoryId(@RequestParam("id") Long id) {
       return service.findByCategoryId(id);
    }

}
