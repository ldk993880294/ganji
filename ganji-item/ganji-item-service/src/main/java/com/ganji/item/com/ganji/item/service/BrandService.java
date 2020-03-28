package com.ganji.item.com.ganji.item.service;


import com.ganji.common.pojo.PageResult;
import com.ganji.item.com.ganji.item.mapper.BrandMapper;
import com.ganji.item.pojo.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<List<Brand>> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        //初始化example对象
        Example example=new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        //根据name模糊查询。或者根据首字母查询
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }

        //添加分页条件
        PageHelper.startPage(page,rows);

        //添加排序条件
        if(StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+""+(desc?"desc":"asc"));
        }

        List<Brand> brands=this.brandMapper.selectByExample(example);

        //包装成pageInfo
        PageInfo<Brand> pageInfo=new PageInfo<>(brands);

        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 新增品牌
     */
    @Transactional
    public void saveBrand(Brand brand,List<Long> cids){
        //先新增brand
        this.brandMapper.insertSelective(brand);
        //在新增中间表
        cids.forEach(cid->{
            this.brandMapper.insertBrandAndCategory(cid,brand.getId());
        });

    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.create.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
         public void listen(Long id){
             System.out.println(id);
    }
}
