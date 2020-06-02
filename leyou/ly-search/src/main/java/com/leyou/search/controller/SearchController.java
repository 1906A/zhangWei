package com.leyou.search.controller;

import com.leyou.common.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
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

@Slf4j
@RestController
public class SearchController {
    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    CategoryClient categoryClient;

    @Autowired
    BrandClient brandClient;


    @RequestMapping("page")
    public PageResult<Goods> page(@RequestBody SearchRequest searchRequest) {
        log.info("--------------------" + searchRequest.getKey() + "------------" + searchRequest.getPage());
//使用多条件的查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
//使用all字段匹配,默认的关系是 or 改为and 关系
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));
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

       // Page<Goods> page = goodsRepository.search(queryBuilder.build());


        return new SearchResult(search.getTotalElements(), search.getContent(), search.getTotalPages()
                                ,clist,blist);

    }
}
