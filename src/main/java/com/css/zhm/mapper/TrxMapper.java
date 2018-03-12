package com.css.zhm.mapper;

import com.css.zhm.entity.Trx;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 描述:
 * 交易记录
 *
 * @author zhm
 * @create 2018-03-11 10:11
 */
@Repository
public interface TrxMapper extends Mapper<Trx> {
    /**
     * 获取已购买的商品ID列表
     * @param personId 用户ID
     * @return 商品ID列表
     * @throws Exception exception
     */
    @Select("SELECT contentId FROM trx WHERE personId = #{personId} GROUP BY contentId")
    List<Integer> getBuyContentIdList(Integer personId) throws Exception;

    /**
     * 获取售出的商品ID列表
     * @return 商品ID列表
     * @throws Exception exception
     */
    @Select("SELECT contentId FROM trx GROUP BY contentId")
    List<Integer> getIsSellContentIdList() throws Exception;
}