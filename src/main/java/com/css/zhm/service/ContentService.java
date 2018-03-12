package com.css.zhm.service;

import com.css.zhm.dto.ContentDto;
import com.css.zhm.entity.Content;
import com.css.zhm.entity.Inventory;
import com.css.zhm.mapper.ContentMapper;
import com.css.zhm.mapper.InventoryMapper;
import com.css.zhm.mapper.TrxMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 销售内容相关操作实现
 *
 * @author zhm
 * @create 2018-03-11 10:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentService {
    @Resource
    private TrxMapper trxMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private InventoryMapper inventoryMapper;

    @Resource
    private InventoryService inventoryService;

    private String statusOn = "0";

    private String statusUn = "1";

    public List<ContentDto> getContentList() throws Exception {
        Example example = new Example(Content.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("status", "0");
        List<Content> contents = contentMapper.selectByExample(example);
        return getContentList(contents);
    }

    public List<ContentDto> getBuyContentList(Integer pid) throws Exception {
        List<Integer> cidList = trxMapper.getBuyContentIdList(pid);
        if(cidList.size() == 0) {
            return null;
        }
        Example example = new Example(Content.class);
        Example.Criteria c = example.createCriteria();
        c.andIn("id", cidList);
        List<Content> contents = contentMapper.selectByExample(example);

        return getContentList(contents);
    }

    public List<ContentDto> getNotBuyContentList(Integer pid) throws Exception {
        List<Integer> cidList = trxMapper.getBuyContentIdList(pid);
        if(cidList.size() == 0) {
            return getContentList();
        }
        Example example = new Example(Content.class);
        Example.Criteria c = example.createCriteria();
        c.andNotIn("id", cidList);
        c.andEqualTo("status", "0");
        List<Content> contents = contentMapper.selectByExample(example);

        return getContentList(contents);
    }

    public List<ContentDto> getAllContents() throws Exception {
        List<ContentDto> contentList = new ArrayList<ContentDto>();
        // 获取所有内容列表
        List<Content> contents = contentMapper.selectAll();
        // 获取有销售记录的内容ID列表
        List<Integer> cidList = trxMapper.getIsSellContentIdList();
        for(Content content : contents) {
            ContentDto contentDto = new ContentDto();
            contentDto.setId(content.getId());
            byte[] icon = content.getIcon();
            if (icon != null) {
                contentDto.setImage(new String(icon, "utf-8"));
            }
            contentDto.setTitle(content.getTitle());
            contentDto.setSaleprice(content.getSaleprice());
            contentDto.setInventory(inventoryService.getInventoryByCid(content.getId()));
            contentDto.setStatus(content.getStatus());
            for (Integer cid: cidList) {
                if(cid.equals(content.getId())) {
                    contentDto.setSell(true);
                }
            }
            contentList.add(contentDto);
        }

        return contentList;
    }

    public List<ContentDto> getIsSellContents() throws Exception {
        List<ContentDto> contentList = new ArrayList<ContentDto>();
        // 获取有销售记录的内容ID列表
        List<Integer> cidList = trxMapper.getIsSellContentIdList();
        if(cidList.size() == 0) {
            return null;
        }
        for(Integer cid : cidList) {
            Content content = contentMapper.selectByPrimaryKey(cid);
            ContentDto contentDto = new ContentDto();
            contentDto.setId(content.getId());
            byte[] icon = content.getIcon();
            if (icon != null) {
                contentDto.setImage(new String(icon, "utf-8"));
            }
            contentDto.setTitle(content.getTitle());
            contentDto.setSaleprice(content.getSaleprice());
            contentDto.setInventory(inventoryService.getInventoryByCid(content.getId()));
            contentDto.setStatus(content.getStatus());
            contentDto.setSell(true);
            contentList.add(contentDto);
        }

        return contentList;
    }

    public List<ContentDto> getIsNotSellContents() throws Exception {
        // 获取有销售记录的内容ID列表
        List<Integer> cidList = trxMapper.getIsSellContentIdList();
        // 如果没有已销售的内容，则返回所有内容
        if(cidList.size() == 0) {
            return getAllContents();
        }
        // 获取未销售的所有内容
        Example example = new Example(Content.class);
        Example.Criteria c = example.createCriteria();
        c.andNotIn("id", cidList);
        List<Content> contents = contentMapper.selectByExample(example);

        return getContentList(contents);
    }

    public ContentDto addContent(Content content) throws Exception {
        // 保存内容
        contentMapper.insertSelective(content);
        content = contentMapper.selectOne(content);

        ContentDto contentDto = new ContentDto();
        contentDto.setId(content.getId());
        contentDto.setTitle(content.getTitle());
        contentDto.setSummary(content.getSummary());
        contentDto.setPrice(content.getPrice());
        contentDto.setSaleprice(content.getSaleprice());
        contentDto.setStatus(content.getStatus());
        contentDto.setImage(new String(content.getIcon(), "utf-8"));
        contentDto.setDetail(new String(content.getText(), "utf-8"));

        return contentDto;
    }

    public ContentDto editContent(Content content) throws Exception {
        // 保存修改
        contentMapper.updateByPrimaryKeySelective(content);
        // 读取商品信息
        content = contentMapper.selectByPrimaryKey(content.getId());
        ContentDto contentDto = new ContentDto();
        contentDto.setId(content.getId());
        contentDto.setTitle(content.getTitle());
        contentDto.setSummary(content.getSummary());
        contentDto.setPrice(content.getPrice());
        contentDto.setSaleprice(content.getSaleprice());
        contentDto.setImage(new String(content.getIcon(), "utf-8"));
        contentDto.setDetail(new String(content.getText(), "utf-8"));

        return contentDto;
    }

    public void deleteContent(Integer cid) throws Exception {
        // 删除库存
        Example example = new Example(Inventory.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("cid", cid);
        inventoryMapper.deleteByExample(example);
        // 删除商品
        contentMapper.deleteByPrimaryKey(cid);
    }

    public void switchStatus(Integer cid) throws Exception {
        Content content = contentMapper.selectByPrimaryKey(cid);
        String status = content.getStatus();
        if(statusOn.equals(status)) {
            status = statusUn;
        } else {
            status = statusOn;
        }
        content.setStatus(status);
        contentMapper.updateByPrimaryKeySelective(content);
    }

    /**
     * 将内容转化为ContentDto
     * @param contents 内容列表
     * @return 内容列表
     * @throws Exception exception
     */
    private List<ContentDto> getContentList(List<Content> contents) throws Exception {
        List<ContentDto> contentList = new ArrayList<ContentDto>();

        for(Content content : contents) {
            ContentDto contentDto = new ContentDto();
            contentDto.setId(content.getId());
            byte[] icon = content.getIcon();
            if (icon != null) {
                contentDto.setImage(new String(icon, "utf-8"));
            }
            contentDto.setTitle(content.getTitle());
            contentDto.setSaleprice(content.getSaleprice());
            contentDto.setInventory(inventoryService.getInventoryByCid(content.getId()));
            contentDto.setStatus(content.getStatus());
            contentList.add(contentDto);
        }

        return contentList;
    }

}