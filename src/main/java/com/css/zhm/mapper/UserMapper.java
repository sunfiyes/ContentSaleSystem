package com.css.zhm.mapper;

/**
 * 描述:
 * 用户
 *
 * @author zhm
 * @create 2018-03-10 11:40
 */
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import com.css.zhm.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends Mapper<User>,MySqlMapper<User>{

}
