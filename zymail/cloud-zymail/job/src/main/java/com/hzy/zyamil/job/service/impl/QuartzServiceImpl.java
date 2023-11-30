package com.hzy.zyamil.job.service.impl;

import com.hzy.zyamil.common.constant.SystemConstant;
import com.hzy.zyamil.common.exception.SystemException;
import com.hzy.zyamil.common.model.dto.JobDto;
import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.model.vo.JobVo;
import com.hzy.zyamil.common.utils.CodeEnum;
import com.hzy.zyamil.common.utils.IpUtils;
import com.hzy.zyamil.common.utils.LogTemplate;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.job.service.LogService;
import com.hzy.zyamil.job.service.QuartzService;
import com.hzy.zyamil.job.utils.RedisCache;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @title: QuartzServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/11/29 16:10
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Service
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    @Lazy
    private LogService logService;
    @Value("${server.port}")
    private int port;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void addJob(String jobName, String jobGroupName, // 工作名 ， 工作组名(调度源)
                       String triggerName, String triggerGroupName, // 触发器名 ， 触发器组名(调度源)
                       Class jobClass, String cron,  // 自定义工作类 ，
                       Date startTime, Date endTime,
                       Map<String, Mail> params) { // 参数(邮件)
        try {
            // 创建作业对象
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 创建触发器
            if (params != null) {
                // 将参数传递给作业
                jobDetail.getJobDataMap().putAll(params);
            }
            // 构建触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            if (startTime != null){
                triggerBuilder.startAt(startTime);
            }else {
                triggerBuilder.startNow();
            }
            if (endTime != null){
                triggerBuilder.endAt(endTime);
            }
            // 触发器时间设定   cron 为空就只执行一次
            if (cron != null && !cron.isEmpty()) {
                // 使用cron表达式创建触发器
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            } else {
                // 创建一个只执行一次的触发器
                triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(0).withRepeatCount(0));
            }
            // 创建Trigger对象
            Trigger trigger = triggerBuilder.build();
            // 启动
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
            // 日志管理  添加启动日志
            logService.info(jobGroupName, LogTemplate.startJobTemplate(
                    jobName,jobGroupName,jobClass,cron,startTime,endTime
            ));
            // 创建任务缓存
            cacheJob(trigger,params.get("mail"),jobName,jobGroupName,
                    scheduler.getTriggerState(trigger.getKey()).ordinal(),cron);
        }catch (Exception e){
            e.printStackTrace();
            throw new SystemException(CodeEnum.QUARTZ_ERROR);
        }

    }

    private void cacheJob(Trigger trigger,Mail mail,String jobName,String jobGroupName,Integer state,
                          String cron){
        JobVo jobVO = new JobVo();
        jobVO.setJobName(jobName);
        jobVO.setJobGroupName(jobGroupName);
        jobVO.setState(state);
        jobVO.setNextFireTime(trigger.getNextFireTime());
        jobVO.setPreviousFireTime(trigger.getPreviousFireTime());
        jobVO.setStartTime(trigger.getStartTime());
        jobVO.setEndTime(trigger.getEndTime());
        jobVO.setMail(mail);
        jobVO.setIpaddr(IpUtils.getIpaddr()+":"+port);
        jobVO.setCron(cron);
        redisCache.setCacheMapValue(SystemConstant.CACHE_JOBS,jobName,jobVO);
    }

    @Override
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
            // 日志管理  添加删除日志
            logService.error(jobGroupName,LogTemplate.delJobTemplate(jobName,jobGroupName));

            // 删除任务缓存
            redisCache.delCacheMapValue(SystemConstant.CACHE_JOBS,jobName);

        } catch (Exception e) {
            throw new SystemException(CodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public void pauseJob(String jobName, String jobGroupName) {
        // 获取缓存
        JobVo jobVo = redisCache.getCacheMapValue(SystemConstant.CACHE_JOBS, jobName);

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        // 停止触发器
        try {
            scheduler.pauseTrigger(triggerKey);
            // 更新缓存状态
            jobVo.setState(scheduler.getTriggerState(triggerKey).ordinal());
            // 更新缓存
            redisCache.setCacheMapValue(SystemConstant.CACHE_JOBS, jobName,jobVo);

            // 日志管理  添加暂停日志
            logService.warning(jobGroupName,LogTemplate.pauseJobTemplate(jobName,jobGroupName));


        } catch (SchedulerException e) {
            throw new SystemException(CodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public void resumeJob(String jobName, String jobGroupName) {
        JobVo jobVo = redisCache.getCacheMapValue(SystemConstant.CACHE_JOBS, jobName);


        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        // 恢复触发器
        try {
            scheduler.resumeTrigger(triggerKey);
            // 更新缓存状态
            jobVo.setState(scheduler.getTriggerState(triggerKey).ordinal());
            redisCache.setCacheMapValue(SystemConstant.CACHE_JOBS, jobName,jobVo);
            // 日志管理  添加恢复日志
            logService.info(jobGroupName,LogTemplate.resumeJobTemplate(jobName,jobGroupName));

        } catch (SchedulerException e) {
            throw new SystemException(CodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public int getSchedulerStatus() {
        try {
            if (scheduler.isInStandbyMode()) {
                return 1;
            } else if (scheduler.isStarted()) {
                return 0;
            } else if (scheduler.isShutdown()) {
                return 2;
            } else {
                return 3;
            }
        } catch (Exception e) {
            throw new SystemException(CodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public void startScheduler() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new SystemException(CodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public void pauseScheduler() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.standby();
            }
        } catch (Exception e) {
            throw new SystemException(CodeEnum.QUARTZ_ERROR);
        }
    }

    // region 任务修改
    private void modifyCache(Trigger trigger,String jobName,Integer state){
        // 更新缓存
        JobVo jobVo = redisCache.getCacheMapValue(SystemConstant.CACHE_JOBS, jobName);
        jobVo.setStartTime(trigger.getStartTime());
        jobVo.setEndTime(trigger.getEndTime());
        jobVo.setNextFireTime(trigger.getNextFireTime());
        jobVo.setPreviousFireTime(trigger.getPreviousFireTime());

        redisCache.setCacheMapValue(SystemConstant.CACHE_JOBS, jobName,jobVo);

    }
    @Override
    public Result modifyJob(Integer radio, JobDto job) {
        try {
            if (radio == 1){
                Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(job.getStartTime());
                return   modifyStartTime(job.getJobName(), job.getJobGroupName(),startTime);
            }else if (radio == 2){
                Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(job.getEndTime());
                return   modifyEndTime(job.getJobName(), job.getJobGroupName(), endTime);
            }else if (radio == 3){
                return   modifyCron(job.getJobName(), job.getJobGroupName(), job.getCron());
            }
        }catch (Exception e){
            throw new SystemException(CodeEnum.JOB_MODIFY_ERROR,e.getMessage());
        }
        return Result.errorResult(CodeEnum.JOB_MODIFY_PARAM_ERROR);
    }
    private Result modifyStartTime(String jobName, String jobGroupName, Date newStartTime) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey( jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return Result.errorResult(CodeEnum.JOB_MODIFY_ERROR);
            }
            // 获取旧触发器的结束时间
            Date oldTime = trigger.getStartTime();
            if (oldTime.compareTo(newStartTime) != 0) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(jobName, jobGroupName);
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(trigger.getCronExpression()));
                triggerBuilder.startAt(newStartTime);
                triggerBuilder.endAt(trigger.getEndTime());
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
                // 日志
                logService.info(jobGroupName,LogTemplate.modifyJobTemplate(jobName,jobGroupName,
                        "修改开始时间为: "+oldTime+" -> "+newStartTime));

            }
            // 更新缓存
            modifyCache(trigger,jobName,scheduler.getTriggerState(triggerKey).ordinal());
        } catch (Exception e) {
            // 处理异常
            throw new SystemException(CodeEnum.JOB_MODIFY_ERROR,e.getMessage());
        }
        return Result.okResult();
    }
    private Result modifyEndTime(String jobName, String jobGroupName, Date newEndTime) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey( jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return Result.errorResult(CodeEnum.JOB_MODIFY_ERROR);
            }
            // 获取旧触发器的结束时间
            Date oldTime = trigger.getEndTime();
            if (oldTime.compareTo(newEndTime) != 0) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(jobName, jobGroupName);
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(trigger.getCronExpression()));
                triggerBuilder.startAt(trigger.getStartTime());
                triggerBuilder.endAt(newEndTime);
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
                // 日志
                logService.info(jobGroupName,LogTemplate.modifyJobTemplate(jobName,jobGroupName,
                        "修改结束时间为: "+oldTime+" -> "+ newEndTime));
            }
            // 更新缓存
            modifyCache(trigger,jobName,scheduler.getTriggerState(triggerKey).ordinal());
        } catch (Exception e) {
            // 处理异常
            throw new SystemException(CodeEnum.JOB_MODIFY_ERROR,e.getMessage());
        }
        return Result.okResult();
    }
    private Result modifyCron( String jobName, String jobGroupName, String param){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey( jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return Result.errorResult(CodeEnum.JOB_MODIFY_ERROR);
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(param)) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(jobName, jobGroupName);

                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(param));
                triggerBuilder.startAt(trigger.getStartTime());
                triggerBuilder.endAt(trigger.getEndTime());
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
                // 日志
                logService.info(jobGroupName,LogTemplate.modifyJobTemplate(jobName,jobGroupName,
                        "修改定时规则为: "+oldTime+" -> "+ param));

            }
            // 更新缓存
            modifyCache(trigger,jobName,scheduler.getTriggerState(triggerKey).ordinal());
        } catch (Exception e) {
            // 处理异常
            throw new SystemException(CodeEnum.JOB_MODIFY_ERROR,e.getMessage());
        }
        return Result.okResult();
    }
    // endregion
}
