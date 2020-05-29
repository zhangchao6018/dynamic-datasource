package com.example.demo.dao.domain;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhangchao22
 **/
public class TbTest implements Serializable {
    private static final long serialVersionUID = -8360057985665898634L;
    private Long id;
    private String name;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TbTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
