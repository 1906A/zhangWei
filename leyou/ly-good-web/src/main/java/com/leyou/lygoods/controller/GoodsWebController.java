package com.leyou.lygoods.controller;

import com.leyou.lygoods.client.*;
import com.leyou.lygoods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;

import java.util.HashMap;

@Controller
@Slf4j
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

    @Autowired
    BrandClient brandClient;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    GoodsService goodsService;

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
        /*//获取spu
        Spu spu = spuClient.selectBySpuId(id);
        model.addAttribute("spu", spu);
        //获取spuDetail
        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spu.getId());
        model.addAttribute("spuDetail", spuDetail);

        //获取sku
        List<Sku> skuList = skuClient.findSkuListBySpuId(spu.getId());
        model.addAttribute("skuList", skuList);

        //根据分类id获取规格参数组及组内的参数信息
        List<SpecGroup> specGroups = specGroupClient.selectSpecGroups(spu.getCid3());
        model.addAttribute("specGroups", specGroups);

        //根据分类id获取规格参数
        List<SpecParam> specParams = specClient.selectSpecParamByCidAndGeneric(spu.getCid3(), false);

        HashMap<Long, Object> paramsMap = new HashMap<>();
        specParams.forEach(param -> {
            paramsMap.put(param.getId(), param.getName());
        });
        model.addAttribute("paramsMap", paramsMap);

        //三级分类
        List<Category> categoryList = categoryClient.selectByCIds(spu.getCid1(), spu.getCid2(), spu.getCid3());
        model.addAttribute("categoryList", categoryList);

        //品牌
        Brand brand = brandClient.findByBrandId(spu.getBrandId());
        model.addAttribute("brand", brand);


*/
        HashMap<String, Object> map = goodsService.item(id);
        model.addAllAttributes(map);

        //写入静态文件
        goodsService.createHtml(id);

        return "item";
    }

    /**
     * 使用templateEngine完成页面静态化
     *
     * @param spu
     * @param spuDetail
     * @param categoryList
     * @param brand
     * @param skuList
     * @param specGroups
     * @param paramsMap
     */
/*
    private void createHtml(Spu spu, SpuDetail spuDetail, List<Category> categoryList, Brand brand, List<Sku> skuList, List<SpecGroup> specGroups, HashMap<Long, Object> paramsMap) {
        PrintWriter printWriter = null;

        try {

            //1,创建上下文对象
            Context context = new Context();

            //2.放入数据
            context.setVariable("specGroups", specGroups);
            context.setVariable("skuList", skuList);
            context.setVariable("spuDetail", spuDetail);
            context.setVariable("spu", spu);
            context.setVariable("categoryList", categoryList);
            context.setVariable("paramsMap", paramsMap);
            context.setVariable("brand", brand);
            //3.写入文件,输出流
            File file = new File("E:\\frame\\nginx-1.16.1\\html\\" + spu.getId() + ".html");

            printWriter = new PrintWriter(file);
            //4.执行静态化--
            templateEngine.process("item", context, printWriter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                //5.写入完毕,关闭流
                printWriter.close();
            }
        }


    }
*/
}
