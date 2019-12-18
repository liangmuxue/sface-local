package com.ss.sdk.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "elaticjob")
@PropertySource(value = "classpath:application.properties")
public class JobProperties {

    private int shardingTotalCount;
    private String shardingItemParameters;
    private String getIssueDataJob;
    private String captureDataJob;

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(int shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }

    public String getShardingItemParameters() {
        return shardingItemParameters;
    }

    public void setShardingItemParameters(String shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }

    public String getGetIssueDataJob() {
        return getIssueDataJob;
    }

    public void setGetIssueDataJob(String getIssueDataJob) {
        this.getIssueDataJob = getIssueDataJob;
    }

    public String getCaptureDataJob() {
        return captureDataJob;
    }

    public void setCaptureDataJob(String captureDataJob) {
        this.captureDataJob = captureDataJob;
    }
}
