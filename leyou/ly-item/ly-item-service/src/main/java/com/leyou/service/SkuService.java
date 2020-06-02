package com.leyou.service;

import com.leyou.dao.SKuMapper;
import com.leyou.pojo.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuService {
    @Autowired
    private SKuMapper sKuMapper;

    /**
     * 根据spuid查询商品集合
     *
     * @param id
     * @return
     */
    public List<Sku> findSkuListBySpuId(Long id) {
        return sKuMapper.getSkuListBySpuId(id);
    }
}
