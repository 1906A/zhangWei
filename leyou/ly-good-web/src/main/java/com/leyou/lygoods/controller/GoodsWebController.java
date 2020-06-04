package com.leyou.lygoods.controller;

import com.leyou.lygoods.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class GoodsWebController {
    @Autowired
    SpuClient spuClient;

    @Autowired
    SkuClient skuClient;

    @Autowired
    SpecClient specClient;

    @Autowired
    SpecGroupClient specGroupClient;

    @Autowired
    CategoryClient categoryClient;

    @RequestMapping("test")
    public String test(Model model) {
        model.addAttribute("aaa", "aaaa");
        return "show";
    }

    /**
     * 请求商品详情
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("item/{id}.html")
    public String item(@PathVariable("id") Long id, Model model) {
        //获取spu
        Spu spu = spuClient.selectBySpuId(id);
        model.addAttribute("spu",spu);
        //获取spuDetail
        SpuDetail detail = spuClient.findSpuDetailBySpuId(spu.getId());
        model.addAttribute("spuDetail",detail);
        //获取sku
        List<Sku> skuList = skuClient.findSkuListBySpuId(spu.getId());
        model.addAttribute("skuList",skuList);
        //根据分类id获取规格参数组
        List<SpecGroup> specGroups = specGroupClient.selectSpecGroups(spu.getCid3());
        model.addAttribute("specGroupList",specGroups);
        //根据分类id获取规格参数
        List<SpecParam> specParams = specClient.selectSpecParamByCidAndSearching(spu.getCid3());
        model.addAttribute("specParamList",specParams);
        //三级分类
        List<Category> categoryList = categoryClient.selectByCIds(spu.getCid1(), spu.getCid2(), spu.getCid3());
        model.addAttribute("categoryList",categoryList);
        return "item";
    }
}
