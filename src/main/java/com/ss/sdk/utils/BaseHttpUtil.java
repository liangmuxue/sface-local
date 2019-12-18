package com.ss.sdk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;

/**
* cpalt HTTP请求
* @author chao
* @create 2019/12/12
* @email lishuangchao@ss-cas.com
**/
@Component
public class BaseHttpUtil {

    @Resource
    private PropertiesUtil propertiesUtil;

    public static final Log LOG = LogFactory.getLog(BaseHttpUtil.class);

    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTENT_CODE_UTF_8 = "UTF-8";
    public static String token = null;
    public static long tokenTime = 0L;

    /**
     * POST请求
     * @param parmJson
     * @param requestUrl
     * @return
     */
    public String httpPost(String parmJson, String requestUrl) {
        String result = null;
        try {
            if (!requestUrl.equals(propertiesUtil.getCplatHttp() + HttpConstant.TOKEN)) {
                if (System.currentTimeMillis() > tokenTime) {
                    login();
                }
            }
            // 获取默认的请求客户端
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 通过HttpPost来发送post请求
            HttpPost httpPost = new HttpPost(requestUrl);
            // 设置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new StringEntity(parmJson, ContentType.APPLICATION_JSON));
            httpPost.addHeader("Content-Type", APPLICATION_JSON);
            httpPost.addHeader("X-Authorization", token);
            httpPost.addHeader("Tenant-Id", propertiesUtil.getTenantId());
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, CONTENT_CODE_UTF_8);
            }
            httpClient.close();
            if (StringUtils.isBlank(result)) {
                LOG.info("cplat接口请求失败:" + requestUrl);
            }
        } catch (Exception e) {
            LOG.error("cplat POST请求异常" + e.toString(), e);
        }
        return result;
    }

    public String login() {
        Map<String, Object> parm = new HashMap<>();
        parm.put("username", propertiesUtil.getUserName());
        parm.put("password", propertiesUtil.getPassword());
        // 发起鉴权请求并获取请求结果
        Object jsonObject = JSONObject.toJSON(parm);
        String resultString = httpPost(jsonObject.toString(), propertiesUtil.getCplatHttp() + HttpConstant.TOKEN);
        String code = null;
        JSONObject jsobj = null;
        if (null != resultString) {
            jsobj = JSONObject.parseObject(resultString);
            code = jsobj.get("code").toString();
        }
        // 获取token并设置token超时时间
        if (null != jsobj && "00000000".equals(code)) {
            JSONObject tk = (JSONObject) jsobj.get("data");
            token = "Bearer " + tk.getString("token");
            tokenTime = System.currentTimeMillis() + 1500000L;
            return "Bearer " + tk.getString("token");
        }
        return null;
    }
}
