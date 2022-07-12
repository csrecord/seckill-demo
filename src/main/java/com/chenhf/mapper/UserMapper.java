package com.chenhf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenhf.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenhf
 */
//Dao层
@Repository
public interface UserMapper extends BaseMapper<User> {

}
