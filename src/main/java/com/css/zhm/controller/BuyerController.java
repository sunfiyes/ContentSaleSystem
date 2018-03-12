package com.css.zhm.controller;

import com.css.zhm.dto.AccountDto;
import com.css.zhm.entity.Content;
import com.css.zhm.entity.Temporary;
import com.css.zhm.entity.User;
import com.css.zhm.mapper.ContentMapper;
import com.css.zhm.mapper.TemporaryMapper;
import com.css.zhm.service.BuyerService;
import com.css.zhm.service.InventoryService;
import com.css.zhm.util.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 描述:
 * 买家相关操作
 *
 * @author zhm
 * @create 2018-03-12 11:46
 */
@Controller
public class BuyerController {
    private static String UserSession = "user";

    @Resource
    private TemporaryMapper temporaryMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private BuyerService buyerService;

    @Resource
    private InventoryService inventoryService;

    /**
     * 查看购物车
     * @param map map
     * @param session session
     * @return 购物车页面
     */
    @RequestMapping(value = "/settleAccount/list", method = RequestMethod.GET)
    public String list(
            ModelMap map,
            HttpSession session
    ) throws Exception {
        List<AccountDto> sadList = new ArrayList<AccountDto>();

        // 获取用户ID
        Integer personId = ((User) session.getAttribute(UserSession)).getId();
        // 根据用户ID获取用户购物车信息
        Example example = new Example(Temporary.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("pid", personId);
        List<Temporary> temporaryList = temporaryMapper.selectByExample(example);
        for(Temporary temporary : temporaryList) {
            AccountDto sad = new AccountDto();
            Content content = contentMapper.selectByPrimaryKey(temporary.getCid());
            // 查询商品库存
            int inventory = inventoryService.getInventoryByCid(temporary.getCid());
            // 计算支付金额
            Double price = content.getSaleprice();
            Integer num = temporary.getNum();
            Double payment = price * num;

            sad.setId(temporary.getId());
            sad.setCid(temporary.getCid());
            byte[] icon = content.getIcon();
            if (icon != null) {
                sad.setImage(new String(icon, "utf-8"));
            }
            sad.setTitle(content.getTitle());
            sad.setPrice(price);
            sad.setNum(num);
            sad.setPayment(payment);
            sad.setStatus(content.getStatus());
            sad.setInventory(inventory);

            sadList.add(sad);
        }

        map.addAttribute("settleAccountList", sadList);

        return "settleAccount";
    }

    /**
     * 加入购物车
     * @param cid 商品ID
     * @param num 商品数量
     * @param session session
     * @return 加入结果
     */
    @RequestMapping(value = "/settleAccount/add", method = RequestMethod.POST)
    @ResponseBody
    public Object addSettleAccount(
            @RequestParam("cid") Integer cid,
            @RequestParam("num") Integer num,
            HttpSession session
    ) throws Exception {
        try {
            // 获取用户ID
            Integer personId = ((User) session.getAttribute(UserSession)).getId();
            // 查询商品库存
            int inventory = inventoryService.getInventoryByCid(cid);
            // 如果数量大于库存，返回错误
            if (num > inventory) {
                return Message.failed("商品加购件数(含已加购件数)已超过库存");
            }
            // 查询购物车中是否含有同种商品
            Temporary temporary = new Temporary();
            temporary.setCid(cid);
            temporary.setPid(personId);
            temporary = temporaryMapper.selectOne(temporary);
            if(temporary != null) {
                // 如果含有同种商品，则更新数量，数量为购物车中数量加新增数量
                int tnum = temporary.getNum();
                int newNum = tnum + num;
                if (newNum > inventory) {
                    return Message.failed("商品加购件数(含已加购件数)已超过库存");
                }
                temporary.setNum(newNum);
                temporaryMapper.updateByPrimaryKeySelective(temporary);
            } else {
                // 如果购物车中没有该商品，则新增购物车记录
                temporary = new Temporary();
                temporary.setCid(cid);
                temporary.setPid(personId);
                temporary.setNum(num);
                temporaryMapper.insertSelective(temporary);
            }
            return Message.success();
        } catch (Exception e) {
            return Message.failed("加入购物车失败！");
        }
    }

    /**
     * 根据购物车ID修改购物车商品数量
     * @param id 购物车ID
     * @param num 商品数量
     * @return 修改结果
     */
    @RequestMapping(value = "/settleAccount/edit", method = RequestMethod.POST)
    public Object editSettleAccount(
            @RequestParam("id") Integer id,
            @RequestParam("num") Integer num
    ) throws Exception {
        try {
            Temporary temporary = temporaryMapper.selectByPrimaryKey(id);
            // 查询商品库存
            int inventory = inventoryService.getInventoryByCid(temporary.getCid());
            // 如果数量大于库存，返回错误
            if (num > inventory) {
                return Message.failed("数量大于库存！");
            }
            temporary.setNum(num);
            temporaryMapper.updateByPrimaryKeySelective(temporary);

            return Message.success();
        } catch (Exception e) {
            return Message.failed("修改数量失败！");
        }
    }

    /**
     * 删除购物车记录
     * @param id 购物车ID
     * @return 删除结果
     */
    @RequestMapping(value = "/settleAccount/delete", method = RequestMethod.POST)
    public Object delete(
            @RequestParam("id") Integer id
    ) throws Exception {
        try {
            temporaryMapper.deleteByPrimaryKey(id);

            return Message.success();
        } catch (Exception e) {
            return Message.failed("删除记录失败！");
        }
    }

    /**
     * 购买内容
     * @param tidList 购物车ID列表
     * @param cid 内容ID
     * @param num 购买数量
     * @param session session
     * @return 购买结果
     * @throws Exception exception
     */
    @RequestMapping(value = "/settleAccount/buy", method = RequestMethod.POST)
    @ResponseBody
    public Object buy(
            @RequestParam(value = "tidList", defaultValue = "") String tidList,
            @RequestParam(value = "cid", defaultValue = "") Integer cid,
            @RequestParam(value = "num", defaultValue = "") Integer num,
            HttpSession session
    ) throws Exception {
        try {
            // 获取用户ID
            Integer personId = ((User) session.getAttribute(UserSession)).getId();
            if(!"".equals(tidList)) {
                boolean isTrue = true;
                // 购物车购买
                StringTokenizer tmp = new StringTokenizer(tidList, ",");
                while ( tmp.hasMoreElements() ){
                    boolean isThisTrue = buyerService.buyFromTemporary(personId, Integer.parseInt(tmp.nextToken()));
                    if(!isThisTrue) {
                        isTrue = false;
                    }
                }
                if(isTrue) {
                    return Message.success();
                } else {
                    return Message.failed("部分商品购买数量已超过库存");
                }
            } else {
                // 直接购买
                boolean isTrue = buyerService.buy(personId, cid, num);
                if(isTrue) {
                    return Message.success();
                } else {
                    return Message.failed("商品购买数量已超过库存");
                }
            }
        } catch (Exception e) {
            return Message.failed("购买失败！");
        }
    }

    /**
     * 跳转用户财务页面
     * @param map map
     * @param session session
     * @return 财务页面
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String account(
            ModelMap map,
            HttpSession session
    ) throws Exception {
        Integer personId = ((User) session.getAttribute(UserSession)).getId();
        map.addAttribute("buyList", buyerService.buyList(personId));
        return "account";
    }

    /**
     * 跳转已购商品详情页面
     * @param id 购买记录ID
     * @param map map
     * @return 已购商品详情页面
     */
    @RequestMapping(value = "/snapshot", method = RequestMethod.GET)
    public String snapshot(
            @RequestParam("id") Integer id,
            ModelMap map
    ) throws Exception {
        try {
            map.addAttribute("product", buyerService.getContentInfoByTid(id));
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "snapshot";
    }

}
