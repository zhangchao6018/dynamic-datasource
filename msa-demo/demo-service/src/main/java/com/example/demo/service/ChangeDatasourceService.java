package com.example.demo.service;

import com.example.demo.dao.domain.TbTest;
import com.example.demo.dao.mapper.TbTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author: zhangchao22
 **/
@Service
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class ChangeDatasourceService {
    @Autowired
    private TbTestMapper tbTestMapper;

    /**
     *
     * @param id
     * @return
     */
    public TbTest selectByPrimaryKey(String id) {
        return tbTestMapper.selectByPrimaryKey(id);
    }
}
