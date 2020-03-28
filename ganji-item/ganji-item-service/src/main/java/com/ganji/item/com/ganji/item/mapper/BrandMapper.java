package com.ganji.item.com.ganji.item.mapper;

import com.ganji.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {


@Insert("insert into tb_category_brand(category_id,brand_id)values(#{cid},#{bid}")
    int insertBrandAndCategory(@Param("cid") Long cid,@Param("bid")Long bid);
}
