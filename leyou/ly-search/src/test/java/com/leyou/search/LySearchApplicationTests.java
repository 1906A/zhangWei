package com.leyou.search;

import com.leyou.common.PageResult;
import com.leyou.pojo.Spu;
import com.leyou.search.client.SpuClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LySearchApplicationTests {
    @Autowired
    SpuClient spuClient;
    @Autowired
    GoodsService service;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    GoodsRepository goodsRepository;

    @Test
    public void test01() {
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);

        PageResult<Spu> page = spuClient.spuPage("", 1, 200, 2);
        page.getItems().forEach(i -> {
            System.out.println(i.getId());
            try {
                Goods goods = service.convert(i);
                goodsRepository.save(goods);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
