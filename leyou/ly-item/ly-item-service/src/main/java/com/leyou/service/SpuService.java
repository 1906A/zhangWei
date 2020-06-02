package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.PageResult;
import com.leyou.dao.SKuMapper;
import com.leyou.dao.SpuDetailMapper;
import com.leyou.dao.SpuMapper;
import com.leyou.dao.StockMapper;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SpuService {
    @Autowired
    private SpuMapper mapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SKuMapper sKuMapper;
    @Autowired
    private StockMapper stockMapper;

    /**
     * 手写分页查询全部
     *
     * @param key
     * @param page
     * @param rows
     * @param saleable
     * @return
     */
    public PageResult<Spu> findBrand(String key, Integer page, Integer rows, Integer saleable) {
        List<Spu> spus = mapper.selectSpuLimit(key, (page - 1) * rows, rows, saleable);
        Long spuCount = mapper.findSpuCount(key, saleable);
        return new PageResult<Spu>(spuCount, spus);
    }

    /**
     * 使用pageHelp插件分页查询
     *
     * @param key
     * @param page
     * @param rows
     * @param saleable
     * @return
     */
    public PageResult<Spu> findBrand2(String key, Integer page, Integer rows, Integer saleable) {
        PageHelper.startPage(page, rows);
        List<Spu> spus = mapper.selectSpu(key, saleable);
        PageInfo<Spu> info = new PageInfo<>(spus);
        return new PageResult<Spu>(info.getTotal(), info.getList());
    }

    /**
     * 添加商品信息
     *
     * @param s
     */
    public void saveSpu(Spu s) {
        Date date = new Date();
        s.setSaleable(false);//默认保存时不上架
        s.setValid(true);//逻辑删除值为1,表示有效数据
        s.setCreateTime(date);
        s.setLastUpdateTime(date);
        mapper.insert(s);//保存spu表

        SpuDetail spuDetail = s.getSpuDetail();
        spuDetail.setSpuId(s.getId());
        spuDetailMapper.insert(spuDetail);//保存spu扩展表

        List<Sku> skus = s.getSkus();
        skus.forEach(sku -> {
            sku.setSpuId(s.getId());
            sku.setEnable(true);
            sku.setCreateTime(date);
            sku.setLastUpdateTime(date);
            sKuMapper.insert(sku);//保存sku表

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);//保存商品库存表
        });

    }

    /**
     * 根据spu ID 查询商品集列表
     *
     * @param spuId
     */
    public SpuDetail findSpuDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 修改商品集列表
     *
     * @param s
     */
    public void updateSpu(Spu s) {
        Date date = new Date();
        s.setSaleable(null);//默认保存时不上架
        s.setValid(null);//逻辑删除值为1,表示有效数据
        s.setCreateTime(null);
        s.setLastUpdateTime(date);
        mapper.updateByPrimaryKeySelective(s);//只会修改属性值不为null的字段,为空的就保持原来的值

        SpuDetail spuDetail = s.getSpuDetail();
        spuDetail.setSpuId(s.getId());
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);//保存spu扩展表

        List<Sku> skus = s.getSkus();
        skus.forEach(sku1 -> {
            sku1.setEnable(false);//删除sku记录操作
            sku1.setSpuId(s.getId());
            sku1.setCreateTime(null);
            sku1.setLastUpdateTime(date);
            sKuMapper.updateByPrimaryKeySelective(sku1);//保存sku表

            stockMapper.deleteByPrimaryKey(sku1.getId());//删除库存表操作
        });
        List<Sku> skus1 = s.getSkus();
        skus1.forEach(sku -> {
            sku.setSpuId(s.getId());
            sku.setEnable(true);
            sku.setCreateTime(date);
            sku.setLastUpdateTime(date);
            sKuMapper.insert(sku);//保存sku表

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);//保存商品库存表
        });
    }

    /**
     * 根据spu ID 删除商品集列表
     *
     * @param spuId
     */
    public void deleteBySpuId(Long spuId) {
        List<Sku> skus = sKuMapper.getSkuListBySpuId(spuId);
        skus.forEach(sku -> {
            //删除sku
            sku.setEnable(false);
            sKuMapper.updateByPrimaryKeySelective(sku);
            //删除库存
            stockMapper.deleteByPrimaryKey(sku.getId());
        });

        //删除detail
        spuDetailMapper.deleteByPrimaryKey(spuId);

        //删除spu
        mapper.deleteByPrimaryKey(spuId);


    }

    /**
     * 根据spu ID 修改上架下架
     *
     * @param spuId
     */
    public void putOrOut(Long spuId, Integer saleable) {
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setSaleable(saleable == 1 ? true : false);
        mapper.updateByPrimaryKeySelective(spu);
    }
}
