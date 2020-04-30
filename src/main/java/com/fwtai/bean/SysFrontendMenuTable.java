package com.fwtai.bean;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * (SysFrontendMenuTable)表实体类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/29 20:40
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public class SysFrontendMenuTable extends Model<SysFrontendMenuTable> {

    private String frontendMenuId;
    
    private String frontendMenuName;
    
    private String frontendMenuUrl;
    
    private String pid;
    
    private Integer frontendMenuSort;
    
    private String description;


    public String getFrontendMenuId() {
        return frontendMenuId;
    }

    public void setFrontendMenuId(String frontendMenuId) {
        this.frontendMenuId = frontendMenuId;
    }

    public String getFrontendMenuName() {
        return frontendMenuName;
    }

    public void setFrontendMenuName(String frontendMenuName) {
        this.frontendMenuName = frontendMenuName;
    }

    public String getFrontendMenuUrl() {
        return frontendMenuUrl;
    }

    public void setFrontendMenuUrl(String frontendMenuUrl) {
        this.frontendMenuUrl = frontendMenuUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getFrontendMenuSort() {
        return frontendMenuSort;
    }

    public void setFrontendMenuSort(Integer frontendMenuSort) {
        this.frontendMenuSort = frontendMenuSort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.frontendMenuId;
    }
    }