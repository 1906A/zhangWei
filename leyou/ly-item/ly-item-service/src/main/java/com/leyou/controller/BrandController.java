package com.leyou.controller;

import com.leyou.common.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("brand")       ///brand/page?key=&page=1&rows=5&sortBy=id&desc=false
public class BrandController {
    @Autowired
    BrandService service;

    @RequestMapping("page")
    public Object BrandPage(
            @RequestParam("key") String key,
            @RequestParam("page") Integer page,
            @RequestParam("rows") Integer rows,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("desc") Boolean desc) {
        log.info("------------" + key + "--" + page + "--" + rows + "--" + sortBy + "--" + desc);
        //通用mapper
        PageResult<Brand> result = service.findBrand(key, page, rows, sortBy, desc);
        //手写sql分页   limit
//        PageResult<Brand> result = service.findBrandLimit(key, page, rows, sortBy, desc);
        return result;
    }

    /**
     * 添加品牌
     * 添加指定
     *
     * @param brand
     * @param cids
     */
    @RequestMapping("addOrEditBrand")
    public void addOrEditBrand(Brand brand,
                               @RequestParam("cids") List<String> cids) {
//      判断主键是否为null,为空就是添加否则就是修改
        if (brand.getId() != null) {
            service.BrandUpdate(brand, cids);
        } else {
            service.BrandAdd(brand, cids);
        }
    }

    /**
     * 指定id删除品牌
     *
     * @param id
     */
    @RequestMapping("deleteById/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        service.BrandDelete(id);
    }

    /**
     * 根据品牌id通过中间表返回指定分类对象
     *
     * @param pid
     */
    @RequestMapping("bid/{id}")
    public List<Category> findCategoryByBrandId(@PathVariable("id") Long pid) {
        System.out.println(service.findCategoryByBrandId(pid));
        return service.findCategoryByBrandId(pid);

    }
    /**
     * 根据分类id通过中间表返回指定品牌对象
     *
     * @param id
     */
    @RequestMapping("cid/{id}")
    public List<Brand> findBrandByCategoryId(@PathVariable("id") Long id) {
        return service.findBrandByCategoryId(id);

    }

    /**
     * 根据品牌id查询品牌对象
     *
     * @param id
     * @return
     */
    @RequestMapping("findByBrandId")
    public Brand findByBrandId(@RequestParam("id")Long id){
        return service.findByBrandId(id);
    }

}
