package com.txzhang.mongo.controller;

import com.txzhang.mongo.entity.R;
import com.txzhang.mongo.service.GridFsService;
import com.txzhang.mongo.util.ResultHelper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能 :
 * date : 2019-05-12 22:03
 *
 * @version : 0.0.4-snapshot
 * @Author : txzhang@wisdombud.com
 * @since : JDK 1.8
 */
@Slf4j
@Controller
@RequestMapping("/api")
public class GridFSController {

    @Autowired
    private GridFsService gridFsService;

    @ApiOperation(value = "文件上传", notes = "文件上传", tags = "可以一次上传多个")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public R save(MultipartFile file) {
        try {
            return gridFsService.save(file);
        } catch (final Throwable e) {
            log.error("保存失败！", e);
            return ResultHelper.fail(null, "保存失败！");
        }
    }

    @ApiOperation(value = "附件删除", notes = "附件删除", tags = "一次删除一个")
    @PostMapping(value = "/delete")
    @ResponseBody
    public R delete(@RequestParam(value = "fileId", required = true) String fileId) {
        try {
            return gridFsService.delete(fileId);
        } catch (final Throwable e) {
            return ResultHelper.fail("删除失败！");
        }
    }

    @PostMapping(value = "/delete-file-name")
    @ResponseBody
    public R deleteByFileName(@RequestParam(value = "fileName", required = true) String fileName) {
        try {
            return gridFsService.deleteByFileName(fileName);
        } catch (final Throwable e) {
            return ResultHelper.fail("删除失败！");
        }
    }

    @ApiOperation("图片预览，下载")
    @RequestMapping("/preview")
    public void preview(String fileId, HttpServletResponse response, HttpServletRequest request) {
        try {
            gridFsService.preview(fileId, response, request);
        } catch (final Throwable e) {
            log.error("预览失败！", e);
        }
    }

    @RequestMapping("/upload-file.html")
    public String index() {
        return "upload-file.html";
    }
}
