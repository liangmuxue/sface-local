package com.ss.sdk.pojo.terminal.model;

/**
 * 半结构化特征
 *
 * @author 李爽超 chao
 * @create 2020/03/19
 * @email lishuangchao@ss-cas.com
 **/
public class Feature {

    private String FeatureVersion;
    private String Feature;

    public String getFeatureVersion() {
        return FeatureVersion;
    }

    public void setFeatureVersion(String featureVersion) {
        FeatureVersion = featureVersion;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }
}
