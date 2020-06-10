package com.leyou.lygoods.service;

import com.leyou.lygoods.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

@Service
public class GoodsService {
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

    /**
     * 根据spuId查询数据
     *
     * @param spuId
     * @return
     */
    public HashMap<String, Object> item(Long spuId) {
        //获取spu
        Spu spu = spuClient.selectBySpuId(spuId);
        //获取spuDetail
        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spu.getId());

        //获取sku
        List<Sku> skuList = skuClient.findSkuListBySpuId(spu.getId());

        //根据分类id获取规格参数组及组内的参数信息
        List<SpecGroup> specGroups = specGroupClient.selectSpecGroups(spu.getCid3());

        //根据分类id获取规格参数
        List<SpecParam> specParams = specClient.selectSpecParamByCidAndGeneric(spu.getCid3(), false);

        HashMap<Long, Object> paramsMap = new HashMap<>();
        specParams.forEach(param -> {
            paramsMap.put(param.getId(), param.getName());
        });

        //三级分类
        List<Category> categoryList = categoryClient.selectByCIds(spu.getCid1(), spu.getCid2(), spu.getCid3());

        //品牌
        Brand brand = brandClient.findByBrandId(spu.getBrandId());

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("spu", spu);
        hashMap.put("spuDetail", spuDetail);
        hashMap.put("skuList", skuList);
        hashMap.put("specGroups", specGroups);
        hashMap.put("paramsMap", paramsMap);
        hashMap.put("categoryList", categoryList);
        hashMap.put("brand", brand);

        return hashMap;

    }

    /**
     * 创建静态页面
     *
     * @param spuId
     */
    public void createHtml(Long spuId) {
        PrintWriter printWriter = null;

        try {

            //1,创建上下文对象
            Context context = new Context();

            //2.放入数据
           /* context.setVariable("specGroups", specGroups);
            context.setVariable("skuList", skuList);
            context.setVariable("spuDetail", spuDetail);
            context.setVariable("spu", spu);
            context.setVariable("categoryList", categoryList);
            context.setVariable("paramsMap", paramsMap);
            context.setVariable("brand", brand);*/

            context.setVariables(this.item(spuId));
            //3.写入文件,输出流
            File file = new File("E:\\frame\\nginx-1.16.1\\html\\" + spuId + ".html");

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

    /**
     * 删除静态页面
     *
     * @param spuId
     */
    public void deleteHtml(Long spuId) {
        File file = new File("E:\\frame\\nginx-1.16.1\\html\\" + spuId + ".html");
        if (file != null && file.exists()) {//当 file不为空并且存在时
            file.delete();
        }
    }
}
