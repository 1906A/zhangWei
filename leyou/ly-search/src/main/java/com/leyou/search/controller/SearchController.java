package com.leyou.search.controller;

import com.leyou.common.PageResult;
import com.leyou.search.client.SpecClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.pojo.SpecParam;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class SearchController {
    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    CategoryClient categoryClient;

    @Autowired
    BrandClient brandClient;

    @Autowired
    SpecClient specClient;


    @RequestMapping("page")
    public PageResult<Goods> page(@RequestBody SearchRequest searchRequest) {
        log.info("--------------------" + searchRequest.getKey() + "------------" + searchRequest.getPage());
//使用多条件的查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
//使用all字段匹配,默认的关系是 or 改为and 关系
//        queryBuilder.withQuery(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));

        //过滤filter 查询条件
        BoolQueryBuilder filterBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));

         if (searchRequest.getFilter()!=null&&searchRequest.getFilter().size()>0){

             searchRequest.getFilter().keySet().forEach(key -> {

                 String field="specs."+key+".keyword";
                 //分类和品牌使用的是id 要特殊处理
                 if (key.equals("分类")){
                     field="cid3";
                 }else if (key.equals("品牌")){
                     field="brandId";
                 }
                 filterBuilder.filter(QueryBuilders.termQuery(field,searchRequest.getFilter().get(key)));
             });
         }
         queryBuilder.withQuery(filterBuilder);

//分页
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize()));

//排序
        queryBuilder.withSort(SortBuilders.fieldSort(searchRequest.getSortby())
                .order(searchRequest.getDescending() ? SortOrder.DESC : SortOrder.ASC));

//分类和品牌
        String categoryName = "categoryName";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryName).field("cid3"));

        String brandName = "brandName";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandName).field("brandId"));


        //返回一个聚合
        AggregatedPage<Goods> search = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        LongTerms cAgg = (LongTerms) search.getAggregation(categoryName);

        ArrayList<Category> clist = new ArrayList<>();
        //获取到聚合的某个桶
        cAgg.getBuckets().forEach(c -> {
            //获取到 key 是cid
            Long cid = (Long) c.getKey();
            //根据cid返回分类对象,将每个对象都添加到集合中
            clist.add(categoryClient.findByCategoryId(cid));
        });


//        AggregatedPage<Goods> bsearch = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        LongTerms bAgg = (LongTerms) search.getAggregation(brandName);

        ArrayList<Brand> blist = new ArrayList<>();
        //获取到品牌的桶,key是品牌id,将根据id返回的所有品牌添加到blist集合中
        bAgg.getBuckets().forEach(b -> {
            Long bid = (Long) b.getKey();
            blist.add(brandClient.findByBrandId(bid));
        });

        //规格参数页面动态展示
        ArrayList<Map<String, Object>> paramList = new ArrayList<>();
        //判断分类id不为空
        if (clist.size() == 1) {
            //查询规格参数
            List<SpecParam> specParams = specClient.selectSpecParamByCidAndSearching(clist.get(0).getId());
            specParams.forEach(params -> {
                String key = params.getName();//分桶的key
                queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs." + key + ".keyword"));
            });
            //重新查询拿到聚合
            AggregatedPage<Goods> search1 = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());
            //将桶转为map类型
            Map<String, Aggregation> aggregationMap = search1.getAggregations().asMap();

            aggregationMap.keySet().forEach(mKey -> {
                if (!(mKey.equals(categoryName) || mKey.equals(brandName))) {
                    StringTerms aggregation = (StringTerms) aggregationMap.get(mKey);
                    HashMap<String, Object> map = new HashMap<>();
                    //放入key和值
                    map.put("key", mKey);

                    ArrayList<Map<String, String>> list = new ArrayList<>();

                    aggregation.getBuckets().forEach(bucket -> {

                        HashMap<String, String> valueMap = new HashMap<>();
                    //放入options中的每个对象值
                        valueMap.put("name", bucket.getKeyAsString());

                        list.add(valueMap);
                    });
                    //放入options
                    map.put("options", list);
                    //key,options添加到list集合中进行返回
                    paramList.add(map);
                }
            });
        }


        // Page<Goods> page = goodsRepository.search(queryBuilder.build());


        return new SearchResult(search.getTotalElements(), search.getContent(), search.getTotalPages()
                , clist, blist, paramList);

    }
}
