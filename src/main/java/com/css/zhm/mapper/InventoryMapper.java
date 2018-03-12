package com.css.zhm.mapper;

import com.css.zhm.entity.Inventory;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 描述:
 * 库存
 *
 * @author zhm
 * @create 2018-03-11 10:05
 */
public interface InventoryMapper extends Mapper<Inventory>,MySqlMapper<Inventory> {

}