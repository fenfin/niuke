package com.nowcoder.community.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Desc:描述
 * @Author：yyf
 * @Filename：ThreadPoolTests
 * @Date：2023/12/14 9:34
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ThreadPoolTests {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTests.class);

    //jdk普通线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    //可执行定时任务线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    private void sleep(long m){
        try {
            Thread.sleep(m);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello executorsServcie");
            }
        };
        for (int i = 0; i < 10; i++) {
           executorService.submit(task);
        }
        sleep(10000);
    }

    //jdk定时任务线程池
    @Test
    public void testScheduledService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("schedule ....");
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(task,10000,1000, TimeUnit.MILLISECONDS);

        sleep(30000);
    }
}
