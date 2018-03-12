package com.css.zhm.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user")
public class User {
    /**
     * person.id
     * 主键
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * person.username
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * person.password
     * 密码md5加密
     */
    @Column(name = "password")
    private String password;

    /**
     * person.nickname
     * 用户昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * person.usertype
     * 类型，买家0，卖家1
     */
    @Column(name = "usertype")
    private Byte usertype;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Byte getUsertype() {
        return usertype;
    }

    public void setUsertype(Byte usertype) {
        this.usertype = usertype;
    }
}