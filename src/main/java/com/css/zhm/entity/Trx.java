package com.css.zhm.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述:
 * 交易实体
 *
 * @author zhm
 * @create 2018-03-11 10:10
 */
@Table(name = "trx")
public class Trx {
    /**
     * trx.id
     * 主键
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * trx.contentId
     * 商品ID
     */
    @Column(name = "contentId")
    private Integer contentId;

    /**
     * trx.personId
     * 用户ID
     */
    @Column(name = "personId")
    private Integer personId;

    /**
     * trx.price
     * 购买单价
     */
    @Column(name = "price")
    private Double price;

    /**
     * trx.num
     * 购买数量
     */
    @Column(name = "num")
    private Integer num;

    /**
     * trx.payment
     * 支付金额
     */
    @Column(name = "payment")
    private Double payment;

    /**
     * trx.time
     * 购买时间
     */
    @Column(name = "time")
    private Long time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}