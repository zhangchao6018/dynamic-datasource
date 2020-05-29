package com.example.demo.dao.mapper;

import com.example.demo.dao.domain.TbTest;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

/**
 * @Description:
 * @Author: zhangchao22
 **/
public interface TbTestMapper {

    @Select({
            "select",
            "id, name",
            "from tb_test",
            "where id = #{assessId,jdbcType=CHAR}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT),
            @Result(column="name", property="name", jdbcType=JdbcType.CHAR),

    })
    TbTest selectByPrimaryKey(String id);
}
