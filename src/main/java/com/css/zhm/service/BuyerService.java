package com.css.zhm.service;

/**
 * 描述:
 * 买家相关操作
 *
 * @author zhm
 * @create 2018-03-12 11:46
 */
import com.css.zhm.dto.BuyDto;
import com.css.zhm.entity.Content;
import com.css.zhm.entity.Temporary;
import com.css.zhm.entity.Trx;
import com.css.zhm.mapper.ContentMapper;
import com.css.zhm.mapper.TemporaryMapper;
import com.css.zhm.mapper.TrxMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BuyerService {
    @Resource
    private TrxMapper trxMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private TemporaryMapper temporaryMapper;

    @Resource
    private InventoryService inventoryService;

    public boolean buy(Integer personId, Integer cid, Integer num) throws Exception {
        // 查询商品库存
        int inventory = inventoryService.getInventoryByCid(cid);
        // 如果数量大于库存，返回错误
        if (num > inventory) {
            return false;
        }
        buyContent(personId, cid, num);
        return true;
    }

    public boolean buyFromTemporary(Integer personId, Integer tid) throws Exception {
        Temporary temporary = temporaryMapper.selectByPrimaryKey(tid);
        Integer cid = temporary.getCid();
        Integer num = temporary.getNum();
        // 查询商品库存
        int inventory = inventoryService.getInventoryByCid(cid);
        // 如果数量大于库存，返回错误
        if (num > inventory) {
            return false;
        }
        // 从购物车中删除
        temporaryMapper.deleteByPrimaryKey(tid);
        // 购买内容
        buyContent(personId, cid, num);
        return true;
    }

    /**
     * 购买商品
     * @param personId 用户ID
     * @param cid 商品ID
     * @param num 购买数量
     */
    private void buyContent(Integer personId, Integer cid, Integer num) {
        Content content = contentMapper.selectByPrimaryKey(cid);
        Double price = content.getSaleprice();
        Double payment = price * num;
        Trx trx = new Trx();
        trx.setContentId(cid);
        trx.setPersonId(personId);
        trx.setPrice(price);
        trx.setNum(num);
        trx.setPayment(payment);
        trx.setTime((new Date()).getTime());
        trxMapper.insertSelective(trx);
    }

    public List<BuyDto> buyList(Integer userId) throws Exception {
        List<BuyDto> buyList = new ArrayList<BuyDto>();

        Example example = new Example(Trx.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("personId", userId);
        example.setOrderByClause("time desc");
        List<Trx> trxList = trxMapper.selectByExample(example);
        for (Trx trx : trxList) {
            BuyDto buyDto = new BuyDto();
            Content content = contentMapper.selectByPrimaryKey(trx.getContentId());
            buyDto.setId(trx.getId());
            buyDto.setPrice(trx.getPrice());
            buyDto.setNum(trx.getNum());
            buyDto.setPayment(trx.getPayment());
            buyDto.setTime(trx.getTime());
            buyDto.setTitle(content.getTitle());
            buyDto.setSummary(content.getSummary());
            byte[] icon = content.getIcon();
            if (icon != null) {
                buyDto.setImage(new String(icon, "utf-8"));
            }
            byte[] text = content.getText();
            if (text != null) {
                buyDto.setDetail(new String(text, "utf-8"));
            }

            buyList.add(buyDto);
        }

        return buyList;
    }

    public BuyDto getContentInfoByTid(Integer tid) throws Exception {
        BuyDto buyDto = new BuyDto();
        // 获取购买记录信息
        Trx trx = trxMapper.selectByPrimaryKey(tid);
        buyDto.setId(trx.getId());
        buyDto.setPrice(trx.getPrice());
        buyDto.setNum(trx.getNum());
        buyDto.setPayment(trx.getPayment());
        buyDto.setTime(trx.getTime());
        // 获取购买的商品的信息
        Content content = contentMapper.selectByPrimaryKey(trx.getContentId());
        buyDto.setTitle(content.getTitle());
        buyDto.setSummary(content.getSummary());
        byte[] icon = content.getIcon();
        if (icon != null) {
            buyDto.setImage(new String(icon, "utf-8"));
        }
        byte[] text = content.getText();
        if (text != null) {
            buyDto.setDetail(new String(text, "utf-8"));
        }

        return buyDto;
    }

}
