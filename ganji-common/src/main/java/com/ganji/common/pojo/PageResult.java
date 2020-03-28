package com.ganji.common.pojo;

import java.util.List;

public class PageResult<T> {


    private Long total;
    private Integer totalPage;
    private List<T> item;


    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getItem() {
        return item;
    }

    public void setItem(List<T> item) {
        this.item = item;
    }

    public PageResult(){

    }

    public PageResult(Long total,Integer totalPage,List<T> items){
         this.total=total;
         this.totalPage=totalPage;
         this.item=items;
    }

    public PageResult(Long total,List<T> items){
        this.total=total;
        this.item=items;
    }


}
