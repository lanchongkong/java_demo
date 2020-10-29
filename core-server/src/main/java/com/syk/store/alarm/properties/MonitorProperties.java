package com.syk.store.alarm.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sunyukun
 * @since 2020/10/29 1:51 下午
 */
@ConfigurationProperties(prefix = "syk.alarm.monitor")
@Component
public class MonitorProperties {

    /**
     * 商品编号
     */
    private String item;

    /**
     * 地址
     */
    private String address;

    /**
     * 获取token
     */
    private String tokenUrl;

    private String appKey;

    private String appSecret;

    /**
     * 会话Id
     */
    private String chatId;

    private String messageUrl;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }
}
