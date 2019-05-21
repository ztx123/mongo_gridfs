package com.txzhang.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 功能 :
 * date : 2019-05-16 11:12
 *
 * @version : 0.0.4-snapshot
 * @Author : txzhang@wisdombud.com
 * @since : JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "txzhang_log")
public class LogEntity<T> {

    @Id
    private String id;
    private String url;
    private String methodName;
    private String args;
    private String ip;
    private String className;
    private Date operateTime;
    private T result; //执行结果
    private Long consuming; //消费时长
}
