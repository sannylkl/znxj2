package com.jiarui.znxj.threadpool;

import java.util.concurrent.ExecutorService;


import java.util.concurrent.Executors;

/**
 * 线程池处理
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月10日
 */
public class TaskManager {

    // app任务处理线程池,根据实际情况选择,优先级别分别为低中高.

    private static ExecutorService appMinPriorExec = Executors.newCachedThreadPool(new MinPriorityThreadFactory());

    private static ExecutorService appNormalPriorExec =
            Executors.newCachedThreadPool(new NormalPriorityThreadFactory());

    private static ExecutorService appMaxPriorExec = Executors.newCachedThreadPool(new MaxPriorityThreadFactory());

    private static ExecutorService singleSequenceExec = Executors.newSingleThreadExecutor();

    /**
     * 处理优先级高的应用任务
     *
     * @param task
     */
    public static void doMaxPriorityAppTask(Runnable task) {
        appMaxPriorExec.execute(task);
    }

    /**
     * 处理优先级一般的应用任务
     *
     * @param task
     */
    public static void doNormalPriorityAppTask(Runnable task) {
        appNormalPriorExec.execute(task);
    }

    /**
     * 处理优先级低的应用任务
     *
     * @param task
     */
    public static void doMinPriorityAppTask(Runnable task) {
        appMinPriorExec.execute(task);
    }

    /**
     * 用于执行要求保证顺序性的任务,按提交先后顺序执行
     *
     * @param task
     */
    public static void doSequenceTask(Runnable task) {
        singleSequenceExec.execute(task);
    }
}
