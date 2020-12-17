/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.server.service
 * Date Created: 2019/5/13
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.ss.sdk.service.terminal;


import com.ss.sdk.pojo.accept.AddPerson;
import com.ss.sdk.pojo.terminal.person.*;
import com.ss.sdk.pojo.terminal.respone.LAPIResponse;
import com.ss.sdk.service.request.TerminalHttpCall;
import com.ss.sdk.service.terminal.impl.IPersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author dW5565
 */
@Service
public class PersonInfoServiceImpl implements IPersonInfoService {

    @Autowired
    TerminalHttpCall terminalHttpCall;

    /*
    * 终端人员下发，多数参数都是写死的，请自行根据文档调整
    * */
    @Override
    public boolean add(AddPerson addPerson, MultipartFile[] files) {
        PersonInfoList personInfoList = new PersonInfoList();
        List<PersonInfo> list1 = new ArrayList<>();
        personInfoList.setPersonInfoList(list1);
        PersonInfo personInfo = new PersonInfo();
        list1.add(personInfo);
        personInfoList.setNum(list1.size());

        personInfo.setPersonID(1L);
        personInfo.setLastChange(System.currentTimeMillis() / 1000);
        personInfo.setPersonName("test");
        personInfo.setGender(1);

        PersonTimeTemplateInfo personTimeTemplateInfo = new PersonTimeTemplateInfo();
        personTimeTemplateInfo.setBeginTime(System.currentTimeMillis() / 1000);
        personTimeTemplateInfo.setEndTime((System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L) / 1000);
        personTimeTemplateInfo.setIndex(0);
        personInfo.setTimeTemplate(personTimeTemplateInfo);

        List<IdentificationInfo> list = new ArrayList<>();
        IdentificationInfo identificationInfo = new IdentificationInfo();
        list.add(identificationInfo);
        identificationInfo.setNumber("111111111111");
        identificationInfo.setType(1);
        personInfo.setIdentificationList(list);
        personInfo.setIdentificationNum(list.size());

        List<PersonImageInfo> imageList = new ArrayList<>();
        if (files.length > 0) {
            // 加密
            try {
                PersonImageInfo personImageInfo = new PersonImageInfo();
                String picture = new BASE64Encoder().encode(files[0].getBytes());
                picture = picture.replaceAll("\n", "").replaceAll("\r", "");
                personImageInfo.setData(picture);
                personImageInfo.setFaceID(1L);
                personImageInfo.setName("1.jpg");
                personImageInfo.setSize(picture.length());
                imageList.add(personImageInfo);

            } catch (Exception e) {
                System.out.println("图片设置失败");
                return false;
            }
        }
        personInfo.setImageList(imageList);
        personInfo.setImageNum(imageList.size());

        if (terminalHttpCall.add(addPerson.getIp(), personInfoList, PersonInfoList.class)) {
            try {
                //System.out.println(JSON.toJSONString(personInfoList));
                LAPIResponse lapiResponse = terminalHttpCall.waitForRespone();
                if (lapiResponse.getStatusCode() != 0) {
                    return false;
                }
            } catch (Exception e) {
                System.out.println("获取结果失败" + e);
            }
        }

        return true;
    }

    /**
     * @return
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

}
