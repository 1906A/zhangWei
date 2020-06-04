package com.leyou.dao;

import com.leyou.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

//@org.apache.ibatis.annotations.Mapper
public interface CategoryMapper extends Mapper<Category> {
    @Select("SELECT * FROM tb_category WHERE id IN (#{cid1},#{cid2},#{cid3})")
    List<Category> selectByCIds(Long cid1, Long cid2, Long cid3);

}
