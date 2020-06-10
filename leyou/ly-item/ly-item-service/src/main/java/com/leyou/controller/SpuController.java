package com.leyou.controller;

import com.leyou.common.PageResult;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("spu")
@RestController
public class SpuController {
    @Autowired
    private SpuService spuService;

    /**
     * 分页展示商品列表
     *
     * @param key
     * @param page
     * @param rows
     * @param saleable
     * @return
     */
    @RequestMapping("page")
    public PageResult<Spu> spuPage(
            @RequestParam("key") String key,
            @RequestParam("page") Integer page,
            @RequestParam("rows") Integer rows,
            @RequestParam("saleable") Integer saleable) {
        System.out.println(key + "-----------" + page + "---------" + rows + "------------" + saleable);
        //手写分页
//        PageResult<Spu> result = spuService.findBrand(key, page, rows, saleable);
        //分页插件
        PageResult<Spu> result = spuService.findBrand2(key, page, rows, saleable);
        return result;
    }

    /**
     * 添加修改商品信息
     *
     * @param spu
     */
    @RequestMapping("saveSpu")
    public void saveSpu(@RequestBody Spu spu) {
        if (spu.getId() != null) {
            spuService.updateSpu(spu);
        } else {
            spuService.saveSpu(spu);
        }
    }

    /**
     * 根据spu ID 查询商品集列表
     *
     * @param spuId
     */
    @RequestMapping("detail/{spuId}")
    public SpuDetail findSpuDetailBySpuId(@PathVariable("spuId") Long spuId) {
        return spuService.findSpuDetailBySpuId(spuId);
    }

    /**
     * 根据spu ID 删除商品集列表
     *
     * @param spuId
     */
    @RequestMapping("deleteById/{id}")
    public void deleteBySpuId(@PathVariable("id") Long spuId) {
        spuService.deleteBySpuId(spuId);
    }

    /**
     * 根据spu ID 修改上架下架
     *
     * @param spuId
     */
    @RequestMapping("putOrOut")
    public void putOrOut(@RequestParam("spuId") Long spuId,
                         @RequestParam("saleable") Integer saleable) {
//        System.out.println(saleable+"========="+spuId);
        spuService.putOrOut(spuId, saleable);
    }

    /**
     * 根据spuid查询spu
     *
     * @param id
     * @return
     */
    @RequestMapping("selectBySpuId")
    public Spu selectBySpuId(@RequestParam("id") Long id) {
        return spuService.selectBySpuId(id);
    }

    @RequestMapping("selectSpuBySpuId")
    public Spu selectSpuBySpuId(@RequestParam("spuId") Long spuId) {
        return spuService.selectSpuBySpuId(spuId);
    }

}
