package com.txzhang.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能 : 响应实体
 * date : 2019-05-12 22:12
 *
 * @version : 0.0.4-snapshot
 * @Author : txzhang@wisdombud.com
 * @since : JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {

    private T data;
    private boolean success;
    private String message;
}
