package com.css.zhm.service;

import com.css.zhm.entity.Inventory;
import com.css.zhm.entity.Trx;
import com.css.zhm.mapper.InventoryMapper;
import com.css.zhm.mapper.TrxMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述:
 * 库存相关操作
 *
 * @author zhm
 * @create 2018-03-11 10:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InventoryService {
    @Resource
    private InventoryMapper inventoryMapper;

    @Resource
    private TrxMapper trxMapper;

    public int getInventoryByCid(Integer cid) throws Exception {
        int allInventory = 0;
        int allSell = 0;
        // 获取进货总数
        Inventory inventory = new Inventory();
        inventory.setCid(cid);
        List<Inventory> inventories = inventoryMapper.select(inventory);
        if(inventories != null) {
            for(Inventory i : inventories) {
                allInventory += i.getNum();
            }
        }
        // 获取售出总数
        Trx trx = new Trx();
        trx.setContentId(cid);
        List<Trx> trxList = trxMapper.select(trx);
        if(trxList != null) {
            for(Trx t : trxList) {
                allSell += t.getNum();
            }
        }

        return allInventory - allSell;
    }
}