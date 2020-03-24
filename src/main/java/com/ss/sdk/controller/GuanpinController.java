package com.ss.sdk.controller;

import com.ss.sdk.model.GuanpinRequest;
import com.ss.sdk.model.GuanpinResponse;
import com.ss.sdk.service.IGuanpinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.ss.sdk.controller
 *
 * @author 李爽超 chao
 * @create 2020/03/16
 * @email lishuangchao@ss-cas.com
 **/
@RestController
@RequestMapping({"/guanpinPush"})
public class GuanpinController {

    private Logger logger = LoggerFactory.getLogger(GuanpinController.class);

    @Autowired
    private IGuanpinService guanpinService;

    /**
     * 冠品人员权限获取
     * @param para
     * @return
     */
    @RequestMapping(value = {"/getPersonPermission"}, method = {RequestMethod.POST})
    public GuanpinResponse getPersonPermission(GuanpinRequest para) throws Exception {
        GuanpinResponse guanpinResponse = new GuanpinResponse();
        try {
            int personPermission = this.guanpinService.getPersonPermission(para);
            if (personPermission > 0){
                guanpinResponse.setResult(1);
                guanpinResponse.setStatus("200");
                guanpinResponse.setSuccess(true);
                guanpinResponse.setMsg("欢迎您");
            } else {
                guanpinResponse.setResult(1);
                guanpinResponse.setStatus("201");
                guanpinResponse.setSuccess(true);
            }
        } catch (Exception e){
            guanpinResponse.setResult(1);
            guanpinResponse.setStatus("201");
            guanpinResponse.setSuccess(false);
            this.logger.info("冠品人员权限验证失败:" + e.toString(), e);
        }
        return guanpinResponse;
    }

    /**
     * 冠品抓拍信息录入
     * @param para
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/dataUpload"}, method = {RequestMethod.POST})
    public GuanpinResponse dataUpload(GuanpinRequest para) throws Exception {
        GuanpinResponse guanpinResponse = new GuanpinResponse();
        try {
            int result = this.guanpinService.dataUpload(para);
            if (result > 0){
                guanpinResponse.setResult(1);
                guanpinResponse.setSuccess(true);
            } else {
                guanpinResponse.setResult(1);
                guanpinResponse.setSuccess(false);
            }
        } catch (Exception e){
            guanpinResponse.setResult(1);
            guanpinResponse.setSuccess(false);
            this.logger.info("冠品抓拍信息录入失败:" + e.toString(), e);
        }
        return guanpinResponse;
    }
}
