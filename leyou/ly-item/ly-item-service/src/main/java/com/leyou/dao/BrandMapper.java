package com.leyou.dao;

import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
//@org.apache.ibatis.annotations.Mapper
public interface BrandMapper extends Mapper<Brand> {

    List<Brand> findBrandLimit(
            @Param("key") String key,
            @Param("page") Integer page,
            @Param("rows") Integer rows,
            @Param("sortBy") String sortBy,
            @Param("desc") boolean desc);

    Long findBrandCount(
            @Param("key") String key,
            @Param("sortBy") String sortBy,
            @Param("desc") boolean desc);

    @Insert("insert into tb_category_brand values (#{cid},#{bid})")
    void addBrandAndCategory(Long cid, Long bid);

    @Delete("delete from tb_category_brand where brand_id=#{id}")
    void deleteCategoryAndBrand(Long id);

    @Select("SELECT c.* FROM tb_category_brand t,tb_category c WHERE t.category_id=c.id AND t.brand_id=#{pid}")
    List<Category> findCategoryByBrandId(Long pid);

    @Select("SELECT * FROM tb_category_brand t,tb_brand b WHERE t.brand_id=b.id AND t.category_id=#{cid}")
    List<Brand> findBrandByCategoryId(Long id);
}
