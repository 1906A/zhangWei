package com.leyou.dao;

import com.leyou.pojo.Spu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuMapper extends Mapper<Spu> {
    List<Spu> selectSpu(
            @Param("key") String key,
            @Param("saleable") Integer saleable
    );
    List<Spu> selectSpuLimit(
            @Param("key") String key,
            @Param("page") Integer page,
            @Param("rows") Integer rows,
            @Param("saleable") Integer saleable
    );

    Long findSpuCount(@Param("key") String key,
                      @Param("saleable") Integer saleable);


    Spu selectSpuBySpuId(@Param("spuId") Long spuId);
}
