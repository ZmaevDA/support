package ru.zmaev.config;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zmaev.job.RatingCalculationJob;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(RatingCalculationJob.class)
                .withIdentity("ratingCalculationJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("ratingCalculationTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
                .build();
    }

    @Bean
    public Scheduler scheduler(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        return scheduler;
    }
}
