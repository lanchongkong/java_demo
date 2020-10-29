package com.syk.store.alarm.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunyukun
 * @since 2020/10/29 2:46 下午
 */
public class AppleStoreConstant {

    private final static String IPHONE_12_PRO_SLIVER_256 = "MGLF3CH/A";
    private final static String IPHONE_12_PRO_SLIVER_256_NAME = "银色 iphone12 pro 256";

    public final static Map<String, String> ITEM_INFO = new HashMap<>();

    static {
        ITEM_INFO.put(IPHONE_12_PRO_SLIVER_256, IPHONE_12_PRO_SLIVER_256_NAME);
    }
}
