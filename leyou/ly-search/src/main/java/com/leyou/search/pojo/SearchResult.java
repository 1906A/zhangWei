package com.leyou.search.pojo;

import com.leyou.common.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchResult extends PageResult<Goods> {
    private List<Category> categoryList;
    private List<Brand> brandList;
    private List<Map<String,Object>> paramList;

    public SearchResult(Long total, List<Goods> items, Integer totalPage, List<Category> categoryList, List<Brand> brandList, List<Map<String, Object>> paramList) {
        super(total, items, totalPage);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.paramList = paramList;
    }

    public SearchResult(Long total, List<Goods> items, Integer totalPage, List<Category> categoryList, List<Brand> brandList) {
        super(total, items, totalPage);
        this.categoryList = categoryList;
        this.brandList = brandList;
    }
}
