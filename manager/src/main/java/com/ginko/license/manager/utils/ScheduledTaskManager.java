package com.ginko.license.manager.utils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ginko
 * @date 8/21/19
 */
public class ScheduledTaskManager {

    /**
     * 操作延迟10毫秒
     */
    private static final int DEFAULT_DELAY_TIME = 10;

    /**
     * 异步操作任务调度线程池
     */
    private final ScheduledExecutorService executorService = initService();

    /**
     * 单例模式
     */
    private static final ScheduledTaskManager INSTANCE = new ScheduledTaskManager();

    public static ScheduledTaskManager getInstance() {
        return INSTANCE;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task) {
        executorService.schedule(task, DEFAULT_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    public void execute(TimerTask task, long timeInSeconds, TimeUnit timeUnit) {
        executorService.schedule(task, timeInSeconds, timeUnit);
    }

    private static ScheduledExecutorService initService() {
        return SpringBeanUtil.getBean("scheduledExecutorService");
    }
}
