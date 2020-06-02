package com.leyou.search.pojo;

import com.leyou.common.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import lombok.Data;

import java.util.List;
@Data
public class SearchResult extends PageResult<Goods> {
    private List<Category> categoryList;
    private List<Brand> brandList;

    public SearchResult(Long total, List<Goods> items, Integer totalPage, List<Category> categoryList, List<Brand> brandList) {
        super(total, items, totalPage);
        this.categoryList = categoryList;
        this.brandList = brandList;
    }
}
