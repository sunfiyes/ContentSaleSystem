package com.css.zhm.dto;

import com.css.zhm.entity.Temporary;

public class AccountDto extends Temporary {

    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */
    private String image;

    /**
     * 促销价
     */
    private Double price;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 支付金额
     */
    private Double payment;

    /**
     * 状态
     */
    private String status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}