package com.ss.sdk.service.data;

import com.github.pagehelper.PageHelper;
import com.ss.sdk.mapper.PersonDataMapper;
import com.ss.sdk.pojo.terminal.model.PersonData;
import com.ss.sdk.pojo.terminal.model.PersonInfo;
import com.ss.sdk.pojo.terminal.model.PersonVerification;
import com.ss.sdk.pojo.terminal.model.SfaceCapture;
import com.ss.sdk.service.data.impl.IPersonDataService;


import com.ss.sdk.utils.Base64Util;
import com.ss.sdk.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * com.unv.demo.service.data
 *
 * @author 李爽超 chao
 * @create 2020/03/20
 * @email lishuangchao@ss-cas.com
 **/
@Service
public class PersonDataServiceImpl implements IPersonDataService {

    private Logger logger = LogManager.getLogger(PersonDataServiceImpl.class);
    @Autowired
    private PersonDataMapper personDataMapper;
    @Autowired
    private PropertiesUtil propertiesUtil;
    @Override
    public int addPerson(PersonVerification personVerification) {
        /*PeopleTotalJob.peopleTotal = PeopleTotalJob.peopleTotal + 1;
        int result = 0;
        for (String key : MyWebSocket.tenantIdMap.keySet()) {
            if (MyWebSocket.tenantIdMap.get(key) != null) {
                Session session = MyWebSocket.tenantIdMap.get(key);
                session.sendText(String.format("%.2f", Double.valueOf(personVerification.getFaceInfoList().get(0).getTemperature())) + "&" + PeopleTotalJob.peopleTotal);
            }
        }*/
        int result = 0;
        try{
            if(StringUtils.isNotBlank(personVerification.getFaceInfoList().get(0).getFaceImage().getData())){
                //拼装数据
                SfaceCapture sfaceCapture = new SfaceCapture();
                sfaceCapture.setPeopleId(String.valueOf(personVerification.getFaceInfoList().get(0).getID()));
                sfaceCapture.setDeviceId(personVerification.getDeviceCode());
                //插入照片
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                String newName = sf.format(new Date());
                if(StringUtils.isNotBlank(personVerification.getFaceInfoList().get(0).getFaceImage().getData())) {
                    String url = this.propertiesUtil.getCaptureUrl() + "/" + newName + ".jpg";
                    Base64Util.saveImg(personVerification.getFaceInfoList().get(0).getFaceImage().getData(), url);
                    sfaceCapture.setCaptureUrl(url);
                }
                if(StringUtils.isNotBlank(personVerification.getFaceInfoList().get(0).getPanoImage().getData())) {
                    String fullUrl = this.propertiesUtil.getCaptureUrl() + "/" + newName + "_full" + ".jpg";
                    Base64Util.saveImg(personVerification.getFaceInfoList().get(0).getPanoImage().getData(), fullUrl);
                    sfaceCapture.setCaptureFullUrl(fullUrl);
                }
                //插入开门类型
                if(personVerification.getFaceInfoList().get(0).getCapSrc() == 1) {
                    sfaceCapture.setOpendoorMode(1);
                }
                else if(personVerification.getFaceInfoList().get(0).getCapSrc() == 2){
                    sfaceCapture.setOpendoorMode(4);
                }
                else if(personVerification.getFaceInfoList().get(0).getCapSrc() == 3){
                    sfaceCapture.setOpendoorMode(3);
                }
                else{
                    sfaceCapture.setOpendoorMode((int)personVerification.getFaceInfoList().get(0).getCapSrc());
                }
                //插入识别结果
                if(personVerification.getLibMatInfoList().get(0).getMatchStatus() == 1) {
                    sfaceCapture.setResultCode(1);
                }else{
                    sfaceCapture.setResultCode(0);
                }
                sfaceCapture.setCompareDate(personVerification.getFaceInfoList().get(0).getTimestamp() * 1000L);
                sfaceCapture.setTemp(personVerification.getFaceInfoList().get(0).getTemperature());
                //插入体温类型
                if(personVerification.getFaceInfoList().get(0).getTemperature() >= 37.5 ){
                    sfaceCapture.setTempState(1);
                }else{
                    sfaceCapture.setTempState(0);
                }
                sfaceCapture.setCreateTime(System.currentTimeMillis());
                //插入数据
                result = this.personDataMapper.insertSfaceCapture(sfaceCapture);
            }
        }catch (Exception e){
            this.logger.info("录入信息错误：" + e, e.toString());
        }
//        try {
//            if (personVerification.getFaceInfoList().get(0).getFaceImage().getData() != null && !"".equals(personVerification.getFaceInfoList().get(0).getFaceImage().getData())){
//                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
//                String newName = sf.format(new Date());
//                String url = this.propertiesUtil.getCaptureUrl() + "/" + newName + ".jpg";
//                Base64Util.saveImg(personVerification.getFaceInfoList().get(0).getFaceImage().getData(), url);
//            }
//            if (!"未识别".equals(personVerification.getLibMatInfoList().get(0).getMatchPersonInfo().getPersonName()) && !"体温正常".equals(personVerification.getLibMatInfoList().get(0).getMatchPersonInfo().getPersonName())) {
//                PersonData personData = new PersonData();
//                personData.setPersonName(personVerification.getLibMatInfoList().get(0).getMatchPersonInfo().getPersonName());
//                personData.setTime(String.valueOf(personVerification.getTimestamp()));
//                personData.setTemp(Double.parseDouble(String.format("%.2f", Double.valueOf(personVerification.getFaceInfoList().get(0).getTemperature()))));
//                PersonData resultData = this.personDataMapper.checkPerson(personData);
//                if (resultData != null) {
//                    personData.setId(resultData.getId());
//                    result = this.personDataMapper.updatePerson(personData);
//                } else {
//                    result = this.personDataMapper.insertPerson(personData);
//                }
//            }
//        } catch (Exception e){
//            this.logger.info("录入考勤信息错误：" + e, e.toString());
//        }
        return result;
    }

    @Override
    public List<PersonData> personPage(PersonData personData) {
        PageHelper.startPage(personData.getPageNum(), personData.getPageSize());
        List<PersonData> personDataList = this.personDataMapper.personPage(personData);
        return personDataList;
    }

    @Override
    public List<PersonInfo> personList(PersonData personData) {
        List<PersonInfo> personInfoList = this.personDataMapper.personList(personData);
        return personInfoList;
    }

}
