package com.txzhang.mongo.util;

import com.txzhang.mongo.entity.R;

/**
 * 功能 :
 * date : 2019-05-12 23:08
 *
 * @version : 0.0.4-snapshot
 * @Author : txzhang@wisdombud.com
 * @since : JDK 1.8
 */
public class ResultHelper {

    public static R success(String message){
        return new R(null, true, message);
    }

    public static <T> R success(T data, String message){
        return new R(data, true, message);
    }

    public static R fail(String message) {
        return new R(null, false, message);
    }

    public static <T> R fail(T data, String message){
        return new R(data, false, message);
    }
}
