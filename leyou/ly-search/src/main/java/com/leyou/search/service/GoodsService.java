package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.search.client.SkuClient;
import com.leyou.search.client.SpecClient;
import com.leyou.search.client.SpuClient;
import com.leyou.search.pojo.Goods;
import com.leyou.pojo.Sku;
import com.leyou.pojo.SpecParam;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService {

    @Autowired
    private SkuClient skuClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private SpuClient spuClient;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Goods convert(Spu spuVo) throws Exception {

        Goods goods = new Goods();

        //基础数据
        goods.setId(spuVo.getId());
        goods.setSubTitle(spuVo.getSubTitle());
        goods.setBrandId(spuVo.getBrandId());
        goods.setCid1(spuVo.getCid1());
        goods.setCid2(spuVo.getCid2());
        goods.setCid3(spuVo.getCid3());
        goods.setCreateTime(spuVo.getCreateTime());

        //all 标题+分类+品牌
        goods.setAll(spuVo.getTitle()+" "+spuVo.getCname().replace("/"," ")+" "+spuVo.getBname());

        //复杂数据
        //获取价格集合和json类型的sku对象
        //根据spuid查询sku
        List<Sku> skuList = skuClient.findSkuListBySpuId(spuVo.getId());

        List<Long> price = new ArrayList<>();
        skuList.forEach(sku -> {
            price.add(sku.getPrice());
        });

        goods.setPrice(price);
        //转json
        goods.setSkus(MAPPER.writeValueAsString(skuList));

        Map<String, Object> specs = new HashMap<>();

        //根据三级分类id和可搜索条件查询规格参数 spec-param
        List<SpecParam> specParamList = specClient.selectSpecParamByCidAndSearching(spuVo.getCid3());

        //根据spuid查询spudetail
        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spuVo.getId());
        specParamList.forEach(sp -> {
            if (sp.getGeneric()) { //判断是否是通用的,通用就查询GenericSpec字段
                try {
                    Map<Long, Object> genericSpec = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
                    });
                    String value = genericSpec.get(sp.getId()).toString();

                    if (sp.getNumeric()) {
                        value = chooseSegment(value, sp);
                    }

                    specs.put(sp.getName(), value);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else { //非通用,查询SpecialSpec字段

                Map<Long, Object> specialSpec = null;
                try {
                    specialSpec = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, Object>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String value = specialSpec.get(sp.getId()).toString();

                specs.put(sp.getName(), value);
            }

        });
        goods.setSpecs(specs);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}