package com.basic.task.controller;


import com.basic.task.core.CommonHandler;
import com.basic.task.model.ScheduleJob;
import com.basic.task.service.QuartzService;
import com.basic.common.dto.Result;
import com.basic.task.model.FilterType;

import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gersy
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/jobDetails")
@Api(value = "任务调度", tags = {"任务调度相关接口"})
public class JobController {
    @Autowired
    QuartzService mQuartzService;
    @Autowired
    CommonHandler commonHandler;

    @PostMapping("/addJob")
    @ApiOperation(value = "添加任务")
    Result addJob(@RequestBody ScheduleJob scheduleJob) {
        JobDataMap b = mQuartzService.addJob(scheduleJob);
        if (b != null) {
            return Result.success(b);
        } else {
            return Result.error("添加失败");
        }
    }

    @GetMapping("/getAllJob")
    @ApiOperation(value = "获取所有的任务")
    Result getAllJob() {
        List<JobDataMap> allJob = mQuartzService.getAllJob();
        if (allJob==null){
            return Result.error("获取任务列表出错");
        }
        return Result.success(allJob);
    }

    @GetMapping("/getRunningJob")
    @ApiOperation(value = "获取所有运行中的任务")
    Result getRunningJob() {
        List<ScheduleJob> allJob = mQuartzService.getRunningJob();
        if (allJob==null){
            return Result.error("获取任务列表出错");
        }
        return Result.success(allJob);
    }

    @PostMapping("/pauseJob")
    @ApiOperation(value = "暂停任务")
    Result pauseJob(@RequestBody ScheduleJob scheduleJob) {
        JobDataMap jobDataMap = mQuartzService.pauseJob(scheduleJob);
        if (jobDataMap != null) {
            return Result.success(jobDataMap);
        } else {
            return Result.error("暂停任务失败");
        }
    }

    @PostMapping("/resumeJob")
    @ApiOperation(value = "恢复任务")
    Result resumeJob(@RequestBody ScheduleJob scheduleJob) {
        JobDataMap jobDataMap = mQuartzService.resumeJob(scheduleJob);
        if (jobDataMap != null) {
            return Result.success(jobDataMap);
        } else {
            return Result.error("恢复任务失败");
        }
    }

    @PostMapping("/deleteJob")
    @ApiOperation(value = "删除任务")
    Result deleteJob(@RequestBody ScheduleJob scheduleJob) {
        boolean success = mQuartzService.deleteJob(scheduleJob);
        if (success) {
            return Result.success("删除任务成功");
        } else {
            return Result.error("删除任务失败");
        }
    }

    @PostMapping("/runJobNow")
    @ApiOperation(value = "立即执行任务")
    Result runJobNow(@RequestBody ScheduleJob scheduleJob) {
        JobDataMap jobDataMap = mQuartzService.runJobNow(scheduleJob);
        if (jobDataMap != null) {
            return Result.success(jobDataMap);
        } else {
            return Result.error("立即执行任务失败");
        }
    }

    @PostMapping("/updateJob")
    @ApiOperation(value = "更新任务")
    Result updateJob(@RequestBody ScheduleJob scheduleJob) {
        JobDataMap jobDataMap = mQuartzService.updateJob(scheduleJob);
        if (jobDataMap != null) {
            return Result.success(jobDataMap);
        } else {
            return Result.error("更新任务失败");
        }
    }

    @PostMapping("/interruptJob")
    @ApiOperation(value = "中断任务")
    Result interruptJob(@RequestBody ScheduleJob scheduleJob) {
        JobDataMap jobDataMap = mQuartzService.interruptJob(scheduleJob);
        if (jobDataMap != null) {
            return Result.success(jobDataMap);
        } else {
            return Result.error("中断任务失败");
        }
    }

    @PostMapping("/updateJobCron")
    @ApiOperation(value = "更新任务时间表达式")
    Result updateJobCron(@RequestBody ScheduleJob scheduleJob) {
        boolean success = mQuartzService.updateJobCron(scheduleJob);
        if (success) {
            return Result.success("更新corn表达式成功");
        } else {
            return Result.error("更新corn表达式失败");
        }
    }

    @GetMapping("/getGroups")
    @ApiOperation(value = "获取所有任务组")
    public Result getGroups() {
        return Result.success(commonHandler.getGroups());
    }

    @GetMapping("/getPublicTasks")
    @ApiOperation(value = "获取所有公共任务")
    public Result getPublicTasks() {
        return Result.success(commonHandler.getPublicTasks());
    }

    @GetMapping("/getGroupTasks")
    @ApiOperation(value = "获取所有组内任务")
    public Result getGroupTasks(@PathVariable String group) {
        return Result.success(commonHandler.getGroupTasks(group));
    }

    @GetMapping("/getTasks")
    @ApiOperation(value = "获取所有分组任务")
    public Result getTasks() {
        return Result.success(commonHandler.getTasks());
    }

    @GetMapping("/getFilterTypes")
    @ApiOperation(value = "获取所有分组任务")
    public Result getFilterTypes() {
        return Result.success(FilterType.getFilterTypes());
    }

}

