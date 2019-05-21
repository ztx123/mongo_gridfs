package com.txzhang.mongo.service;

import com.txzhang.mongo.entity.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能 : 文件上传service
 * date : 2019-05-12 21:56
 *
 * @version : 0.0.4-snapshot
 * @Author : txzhang@wisdombud.com
 * @since : JDK 1.8
 */
public interface GridFsService {

    R save(MultipartFile file) throws IOException;

    R delete(String fileId);

    R deleteByFileName(String... fileName);

    void preview(String fileId, HttpServletResponse response, HttpServletRequest request) throws IOException;

    byte[] download(String fileId);
}
