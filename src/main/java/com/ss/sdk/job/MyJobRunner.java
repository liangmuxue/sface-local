package com.ss.sdk.job;

import com.ss.sdk.utils.JobProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
* 定时任务启动类
* @author chao
* @create 2019/12/12
* @email lishuangchao@ss-cas.com
**/
@Component
public class MyJobRunner implements ApplicationRunner {

    @Autowired
    private StockSimpleJob stockSimpleJob;
    @Autowired
    private JobProperties jobProperties;

    @Override
    public void run(ApplicationArguments var1) throws Exception {
        int tc = jobProperties.getShardingTotalCount();
        String sp = jobProperties.getShardingItemParameters();

        this.stockSimpleJob.addSimpleJobScheduler(GetIssueDataJob.class, jobProperties.getGetIssueDataJob(), tc, sp, true);

        this.stockSimpleJob.addSimpleJobScheduler(CaptureDataJob.class, jobProperties.getCaptureDataJob(), tc, sp, true);

    }

}
