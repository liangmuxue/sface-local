package com.ss.sdk.mapper;

import com.ss.sdk.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {

    List<Device> findAllDevice();

    List<Device> findHivVideoTempDevice();

    Device findDevice(Device device);

    int updateDevice(List<Device> deviceList);

    List<WhiteList> findWhiteList();

    int insertIssue(Issue issue);

    int insertWhiteList(Issue issue);

    List<Issue> findIssueList();

    int updateIssue(List<Issue> issueList);

    int delWhiteList(Issue issue);

    int insertCapture(Capture capture);

    int updateCapture(Capture capture);

    List<Capture> findCommonCaptureList(Capture capture);

    List<Capture> findCloudwalk(Capture capture);

    List<Capture> findRemoteCaptureList();

    String findCommonMaxTime();

    String findRemoteMaxTime();

    int updateCommonTime(@Param("updateTime") String updateTime);

    int updateRemoteTime(@Param("updateTime") String updateTime);

    int getPersonPermission(GuanpinRequest capture);
}
