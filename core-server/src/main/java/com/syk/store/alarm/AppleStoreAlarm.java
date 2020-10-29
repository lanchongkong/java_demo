package com.syk.store.alarm;

import static com.syk.store.alarm.constant.AppleStoreConstant.ITEM_INFO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.syk.store.alarm.bean.StoreEntity;
import com.syk.store.alarm.properties.AppStoreProperties;
import com.syk.store.alarm.properties.MonitorProperties;
import com.taobao.api.ApiException;

/**
 * @author sunyukun
 * @since 2020/10/29 1:44 下午
 */
@EnableScheduling
@Service
public class AppleStoreAlarm {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppleStoreAlarm.class);

    private final AppStoreProperties appStoreProperties;

    private final MonitorProperties monitorProperties;

    private final RestTemplate restTemplate;

    private Map<String, StoreEntity> storeInfos = new HashMap<>();

    private List<String> locations = new ArrayList<>();

    public AppleStoreAlarm(AppStoreProperties appStoreProperties, MonitorProperties monitorProperties,
        RestTemplate restTemplate) {
        this.appStoreProperties = appStoreProperties;
        this.monitorProperties = monitorProperties;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "${syk.apple.store.cron:0/10 * * * * ?}")
    public void appleStoreMonitor() throws ApiException {
        if (storeInfos.isEmpty()) {
            LOGGER.info("开始请求店铺信息");
            JSONObject store = restTemplate.getForObject(appStoreProperties.getStoreUrl(), JSONObject.class);
            List<?> stores = store.getObject("stores", List.class);
            stores.parallelStream().forEach(storeJson -> {
                StoreEntity storeEntity = JSONObject.parseObject(JSONObject.toJSONString(storeJson), StoreEntity.class);
                storeInfos.put(storeEntity.getStoreNumber(), storeEntity);
            });
        }

        if (CollectionUtils.isEmpty(locations)) {
            locations.addAll(Arrays.asList(monitorProperties.getAddress().split(",")));
        }

        JSONObject stock = restTemplate.getForObject(appStoreProperties.getAvailabilityUrl(), JSONObject.class);

        for (Map.Entry<String, StoreEntity> storeEntry : storeInfos.entrySet()) {
            JSONObject detailInfo = stock.getJSONObject("stores").getJSONObject(storeEntry.getKey());
            for (Map.Entry<String, String> itemEntry : ITEM_INFO.entrySet()) {
                JSONObject target = detailInfo.getJSONObject(itemEntry.getKey()).getJSONObject("availability");
                if (target.getBoolean("contract") || target.getBoolean("unlocked")) {
                    LOGGER.info("产品：{},在地区{},店铺{}有库存！！！", itemEntry.getValue(), storeEntry.getValue().getCity(),
                        storeEntry.getValue().getStoreName());
                    if (locations.contains(storeEntry.getValue().getCity())
                        && itemEntry.getKey().equals(monitorProperties.getItem())) {
                        String msg = String.format("城市：%s,店铺：%s,产品：%s 有库存", storeEntry.getValue().getCity(),
                            storeEntry.getValue().getStoreName(), itemEntry.getValue());
                        String accessKey = this.getAccessKey();
                        this.sendMessage(msg, accessKey);
                    }
                }
            }
        }
    }

    private String getAccessKey() throws ApiException {
        DefaultDingTalkClient client = new DefaultDingTalkClient(monitorProperties.getTokenUrl());
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(monitorProperties.getAppKey());
        request.setAppsecret(monitorProperties.getAppSecret());
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        if (response.isSuccess()) {
            return response.getAccessToken();
        }
        return null;
    }

    private void sendMessage(String msgInfo, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(monitorProperties.getMessageUrl());
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid(monitorProperties.getChatId());

        OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
        msg.setMsgtype("text");
        OapiChatSendRequest.Text text = new OapiChatSendRequest.Text();
        text.setContent(msgInfo);
        msg.setText(text);

        request.setMsg(msg);
        OapiChatSendResponse response = client.execute(request, accessToken);
    }

}
