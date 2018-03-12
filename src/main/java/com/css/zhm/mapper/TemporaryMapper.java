package com.css.zhm.mapper;

/**
 * 描述:
 * 购物车
 *
 * @author zhm
 * @create 2018-03-12 11:46
 */
import com.css.zhm.entity.Temporary;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface TemporaryMapper extends Mapper<Temporary>,MySqlMapper<Temporary> {

}
