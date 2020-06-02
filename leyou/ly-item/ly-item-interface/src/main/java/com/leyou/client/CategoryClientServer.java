package com.leyou.client;

import com.leyou.pojo.Category;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("category")
public interface CategoryClientServer {
    @RequestMapping("findByCategoryId")
    public Category findByCategoryId(@RequestParam("id") Long id);
}
