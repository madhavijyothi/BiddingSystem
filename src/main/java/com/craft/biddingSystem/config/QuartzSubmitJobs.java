package com.craft.biddingSystem.config;

import com.craft.biddingSystem.jobs.LotCompletionCheckJob;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzSubmitJobs {
    private static final String CRON_EVERY_MINUTE = "0 0/1 * ? * * *";

    @Bean(name = "lotCompletionCheck")
    public JobDetailFactoryBean jobLotCompletionCheck(){
        return QuartzConfig.createJobDetail(LotCompletionCheckJob.class, "Lot Completion Check Job");
    }

    @Bean(name = "lotCompletionCheckTrigger")
    public CronTriggerFactoryBean triggerLotCompletionCheck(@Qualifier("lotCompletionCheck")JobDetail jobDetail){
        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_MINUTE, "Lot Completion Check Trigger");
    }

}
