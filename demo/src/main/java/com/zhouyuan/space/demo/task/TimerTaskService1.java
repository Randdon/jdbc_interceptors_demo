//package com.zhouyuan.space.demo.task;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.Trigger;
//import org.springframework.scheduling.TriggerContext;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
///**
// * kafka健康检查定时任务
// *
// * @author yuanhao
// * @since 2020/9/22
// */
//@Service
//@EnableScheduling
//public class TimerTaskService1 implements SchedulingConfigurer {
//
//    private static Logger log = LoggerFactory.getLogger(TimerTaskService1.class);
//
//    /**
//     * 定时任务cron 每隔5秒执行一次
//     */
//    private static final String CRON_5S = "*/5 * * * * ?";
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//
//        scheduledTaskRegistrar.addTriggerTask(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Titanhhhhhhh " + Thread.currentThread().getName() + new Date());
//                try {
//                    // 休眠10s
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Trigger() {
//            @Override
//            public Date nextExecutionTime(TriggerContext triggerContext) {
//                // 每隔5秒给kafka发送一次消息，如果连续三次失败，则停止kafka日志收集
//                // 然后改为每隔1分钟发送一次，如果成功则启动kafka日志收集
//                String cron = CRON_5S;
//
//                CronTrigger trigger = new CronTrigger(cron);
//                return trigger.nextExecutionTime(triggerContext);
//            }
//        });
//
//    }
//
//}
