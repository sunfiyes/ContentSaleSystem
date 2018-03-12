package com.css.zhm.controller;

import com.css.zhm.dto.ContentDto;
import com.css.zhm.entity.Content;
import com.css.zhm.entity.Inventory;
import com.css.zhm.mapper.ContentMapper;
import com.css.zhm.mapper.InventoryMapper;
import com.css.zhm.service.ContentService;
import com.css.zhm.util.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 描述:
 * 卖家相关操作（商品发布、进货等）
 *
 * @author zhm
 * @create 2018-03-11 14:47
 */
@Controller
public class SellerController {

    @Resource
    private ContentService contentService;

    @Resource
    private InventoryMapper inventoryMapper;

    @Resource
    private ContentMapper contentMapper;

    /**
     * 跳转发布页面
     * @return 发布页面
     */
    @RequestMapping(value = "/public", method = RequestMethod.GET)
    public String pul() {
        return "public";
    }

    /**
     * 发布内容
     * @param title 标题
     * @param summary 摘要
     * @param price 价格
     * @param salePrice 促销价
     * @param image 图片地址
     * @param detail 正文
     * @param map map
     * @return 内容
     * @throws Exception exception
     */
    @RequestMapping(value = "/publicSubmit", method = RequestMethod.POST)
    public String publicSubmit(
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("price") Double price,
            @RequestParam("salePrice") Double salePrice,
            @RequestParam("image") String image,
            @RequestParam("detail") String detail,
            ModelMap map
    ) throws Exception {
        try {
            Content content = new Content();
            content.setTitle(title);
            content.setSummary(summary);
            content.setPrice(price);
            content.setSaleprice(salePrice);
            content.setIcon(image.getBytes("utf-8"));
            content.setText(detail.getBytes("utf-8"));
            ContentDto contentDto = contentService.addContent(content);

            map.addAttribute("product", contentDto);
            return "publicSubmit";
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 上传图片
     * @param request request
     * @return 上传结果
     * @throws Exception exception
     */
    @RequestMapping(value = "/api/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 将文件放置位置设置为静态资源目录image中
            String logoPathDir = request.getSession().getServletContext().getRealPath("/") +"/image/";
            File logoSaveFile = new File(logoPathDir);
            if (!logoSaveFile.exists()){
                logoSaveFile.mkdirs();
            }
            // 页面控件的文件流
            MultipartFile multipartFile = multipartRequest.getFile("file");
            // 获取文件的后缀
            String suffix = multipartFile.getOriginalFilename().substring(
                    multipartFile.getOriginalFilename().lastIndexOf("."));
            // 使用UUID生成文件名称
            String imageName = UUID.randomUUID().toString() + suffix;

            // 拼成完整的文件保存路径加文件
            String fileName = logoPathDir + imageName;
            File file = new File(fileName);
            try {
                multipartFile.transferTo(file);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 生成图片网址
            String imgUrl = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath() + "/image/" + imageName;

            return Message.returnResult(imgUrl);
        } catch (Exception e) {
            return Message.failed();
        }
    }


    /**
     * 进货
     * @param cid 内容ID
     * @param num 进货数量
     * @return 进货结果
     * @throws Exception exception
     */
    @RequestMapping(value = "/inventory/add", method = RequestMethod.POST)
    @ResponseBody
    public Object addInventory(
            @RequestParam("cid") Integer cid,
            @RequestParam("num") Integer num
    ) throws Exception {
        try {
            Inventory inventory = new Inventory();
            inventory.setCid(cid);
            inventory.setNum(num);
            inventoryMapper.insertSelective(inventory);

            return Message.success();
        } catch (Exception e) {
            return Message.failed("进货失败");
        }
    }

    /**
     * 跳转内容编辑页面
     * @param cid 内容ID
     * @param map map
     * @return 页面
     * @throws Exception exception
     */
    @RequestMapping(value = "/content/edit", method = RequestMethod.GET)
    public String editPage(
            @RequestParam("id") Integer cid,
            ModelMap map
    ) throws Exception {
        // 获取商品信息
        Content content = contentMapper.selectByPrimaryKey(cid);

        ContentDto contentDto = new ContentDto();
        contentDto.setId(content.getId());
        contentDto.setTitle(content.getTitle());
        contentDto.setSummary(content.getSummary());
        contentDto.setPrice(content.getPrice());
        contentDto.setSaleprice(content.getSaleprice());
        byte[] icon = content.getIcon();
        if (icon != null) {
            contentDto.setImage(new String(icon, "utf-8"));
        }
        byte[] text = content.getText();
        if (text != null) {
            contentDto.setDetail(new String(text, "utf-8"));
        }

        map.addAttribute("product", contentDto);

        return "edit";
    }

    /**
     * 保存内容修改
     * @param id 内容ID
     * @param title 标题
     * @param summary 摘要
     * @param price 价格
     * @param salePrice 促销价
     * @param image 图片地址
     * @param detail 正文
     * @param map map
     * @return 内容
     * @throws Exception exception
     */
    @RequestMapping(value = "/content/editSubmit", method = RequestMethod.POST)
    public String editSubmit(
            @RequestParam("id") Integer id,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("price") Double price,
            @RequestParam("salePrice") Double salePrice,
            @RequestParam("image") String image,
            @RequestParam("detail") String detail,
            ModelMap map
    ) throws Exception {
        try {
            Content content = new Content();
            content.setId(id);
            content.setTitle(title);
            content.setSummary(summary);
            content.setPrice(price);
            content.setSaleprice(salePrice);
            content.setIcon(image.getBytes("utf-8"));
            content.setText(detail.getBytes("utf-8"));
            ContentDto contentDto = contentService.editContent(content);

            map.addAttribute("product", contentDto);
            return "editSubmit";
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除内容
     * @param cid 内容ID
     * @return 删除结果
     * @throws Exception exception
     */
    @RequestMapping(value = "/content/delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(
            @RequestParam("cid") Integer cid
    ) throws Exception {
        try {
            contentService.deleteContent(cid);

            return Message.success();
        } catch (Exception e) {
            return Message.failed("内容删除失败");
        }
    }

    /**
     * 切换内容状态
     * @param cid 内容ID
     * @return 切换结果
     * @throws Exception exception
     */
    @RequestMapping(value = "/content/switchStatus", method = RequestMethod.POST)
    @ResponseBody
    public Object switchStatus(
            @RequestParam("cid") Integer cid
    ) throws Exception {
        try {
            contentService.switchStatus(cid);

            return Message.success();
        } catch (Exception e) {
            return Message.failed();
        }
    }

}
