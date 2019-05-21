package com.txzhang.mongo.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.txzhang.mongo.entity.R;
import com.txzhang.mongo.service.GridFsService;
import com.txzhang.mongo.util.ResultHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 功能 : mongo gridfs 日常操作
 * date : 2019-05-12 21:57
 *
 * @version : 0.0.4-snapshot
 * @Author : txzhang@wisdombud.com
 * @since : JDK 1.8
 */
@Slf4j
@Service
public class GridFsServiceImpl implements GridFsService {

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    @Override
    public R save(final MultipartFile file) throws IOException {
        log.info("start save file...");
        DBObject dbObject = new BasicDBObject();
        dbObject.put("createTime", new Date());
        String fileName = file.getOriginalFilename();
        log.info("File Name : " + fileName);
        InputStream inputStream = null;
        inputStream = file.getInputStream();
        final ObjectId store = gridFsTemplate.store(inputStream, fileName, dbObject.toString());
        log.info("当前上传的文件_id：" + store.toString());
        return ResultHelper.success(store.toString(), "保存成功！");
    }

    @Override
    public R delete(final String fileId) {
        final Query query = new Query().addCriteria(Criteria.where("_id").is(fileId));
        // 查询单个文件
        GridFSFile gridFSfile = gridFsTemplate.findOne(query);
        if (null == gridFSfile) {
            return ResultHelper.fail("文件不存在！");
        }
        gridFsTemplate.delete(query);
        return ResultHelper.success("删除成功！");
    }

    @Override
    public R deleteByFileName(final String... fileNames) {
        for (String fileName : fileNames) {
            final Query query = new Query().addCriteria(Criteria.where("filename").is(fileName));
            // 查询单个文件
            GridFSFile gridFSfile = gridFsTemplate.findOne(query);
            if (null == gridFSfile) {
                return ResultHelper.fail("文件不存在！");
            }
            gridFsTemplate.delete(query);
        }
        return ResultHelper.success("删除成功！");
    }

    @Override
    public void preview(final String fileId, HttpServletResponse response, HttpServletRequest request) throws IOException {
        log.info("当前要预览的文件id是：" + fileId);
        final GridFSFile gridFSFile = gridFsTemplate.findOne(new Query().addCriteria(Criteria.where("_id").is(fileId)));
        final GridFSDownloadStream in = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource fsResource = new GridFsResource(gridFSFile, in);
        String fileName = gridFSFile.getFilename();
        log.info("预览文件名称：" + fileName);
        //处理中文文件乱码
        final String header = request.getHeader("User-Agent").toUpperCase();
        if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            //非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
        IOUtils.copy(fsResource.getInputStream(), response.getOutputStream());
    }

    @Override
    public byte[] download(final String fileId) {
        return null;
    }
}
