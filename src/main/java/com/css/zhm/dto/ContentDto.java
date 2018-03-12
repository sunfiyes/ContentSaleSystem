package com.css.zhm.dto;

import com.css.zhm.entity.Content;

/**
 * 描述:
 * 销售内容信息
 *
 * @author zhm
 * @create 2018-03-11 10:29
 */
public class ContentDto extends Content {

    /**
     * 图片
     */
    private String image;

    /**
     * 正文
     */
    private String detail;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 是否有销售记录
     */
    private Boolean isSell = false;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Boolean getSell() {
        return isSell;
    }

    public void setSell(Boolean sell) {
        isSell = sell;
    }
}