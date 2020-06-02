package com.leyou.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<C> {
    private Long total;//总条数

    private List<C> items;//集合数据

    private Integer totalPage;//总页数

    public PageResult(Long total) {
        this.total = total;
    }

    public PageResult(Long total, List<C> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, List<C> items, Integer totalPage) {
        this.total = total;
        this.items = items;
        this.totalPage = totalPage;
    }

    public PageResult() {
    }
}
