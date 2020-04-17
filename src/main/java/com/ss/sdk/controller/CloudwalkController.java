package com.ss.sdk.controller;

import com.ss.sdk.model.CloudwalkCapture;
import com.ss.sdk.service.ICloudwalkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * com.ss.sdk.controller
 *
 * @author 李爽超 chao
 * @create 2020/04/09
 * @email lishuangchao@ss-cas.com
 **/
@RestController
@RequestMapping({"/cloudwalk"})
public class CloudwalkController {

    private Logger logger = LoggerFactory.getLogger(CloudwalkController.class);

    @Autowired
    private ICloudwalkService cloudwalkService;

    @RequestMapping(value = {"/terminal/uploadSnapRecord"}, method = {RequestMethod.POST})
    public int uploadSnapRecord(HttpServletRequest request, @RequestBody CloudwalkCapture cloudwalkCapture, BindingResult bindingResult) throws Exception {

        int resultCode = 0;
        try {
            resultCode = this.cloudwalkService.insertCapture(cloudwalkCapture);
        } catch (Exception e) {
            this.logger.error("云从设备存储抓拍照信息异常" + e.toString(), e);
            e.printStackTrace();
        }
        return resultCode;
    }

    @RequestMapping(value = {"/terminal/uploadSnapPanorama"}, method = {RequestMethod.POST})
    public int uploadSnapPanorama(HttpServletRequest request, @RequestBody CloudwalkCapture cloudwalkCapture, BindingResult bindingResult) throws Exception {

        int resultCode = 0;
        try {
            resultCode = this.cloudwalkService.insertCapture(cloudwalkCapture);
        } catch (Exception e) {
            this.logger.error("云从设备存储全景照信息异常" + e.toString(), e);
            e.printStackTrace();
        }
        return resultCode;
    }

    @RequestMapping(value = {"/terminal/keepHeart"}, method = {RequestMethod.POST})
    public int keepHeart(HttpServletRequest request, @RequestBody CloudwalkCapture cloudwalkCapture, BindingResult bindingResult) throws Exception {

        this.logger.info("keepHeart");
        return 1;
    }
}
