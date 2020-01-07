package com.ss.sdk.mapper;

import com.ss.sdk.model.Capture;
import com.ss.sdk.model.Device;
import com.ss.sdk.model.Issue;
import com.ss.sdk.model.WhiteList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {

    List<Device> findAllDevice();

    Device findDevice(Issue issue);

    int updateDevice(List<Device> deviceList);

    List<WhiteList> findWhiteList();

    int insertIssue(Issue issue);

    int insertWhiteList(Issue issue);

    List<Issue> findIssueList();

    int updateIssue(List<Issue> issueList);

    int delWhiteList(Issue issue);

    int insertCapture(Capture capture);

    List<Capture> findCommonCaptureList();

    List<Capture> findRemoteCaptureList();

    String findCommonMaxTime();

    String findRemoteMaxTime();

    int updateCommonTime(@Param("updateTime") String updateTime);

    int updateRemoteTime(@Param("updateTime") String updateTime);
}
