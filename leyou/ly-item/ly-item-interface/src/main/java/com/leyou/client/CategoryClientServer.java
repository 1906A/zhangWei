package com.leyou.client;

import com.leyou.pojo.Category;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryClientServer {

    @RequestMapping("findByCategoryId")
    public Category findByCategoryId(@RequestParam("id") Long id);

    @RequestMapping("selectByCIds")
    public List<Category> selectByCIds(
            @RequestParam("cid1") Long cid1,
            @RequestParam("cid2") Long cid2,
            @RequestParam("cid3") Long cid3
    );
}
