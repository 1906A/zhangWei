package com.leyou.client;

import com.leyou.pojo.SpecParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("specParam")
public interface SpecClientServer {
    @RequestMapping("paramsByCid")
    public List<SpecParam> selectSpecParamByCidAndSearching(@RequestParam("cid") Long cid);

    @RequestMapping("paramsByCidAndGeneric")
    public List<SpecParam> selectSpecParamByCidAndGeneric(@RequestParam("cid") Long cid, @RequestParam("generic") Boolean generic);
}
