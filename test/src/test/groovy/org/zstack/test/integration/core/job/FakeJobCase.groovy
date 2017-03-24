package org.zstack.test.integration.core.job


import org.zstack.core.job.Job
import org.zstack.core.job.JobQueueFacade
import org.zstack.header.core.Completion
import org.zstack.header.core.NopeCompletion
import org.zstack.header.core.ReturnValueCompletion
import org.zstack.header.errorcode.ErrorCode
import org.zstack.test.integration.core.CoreLibraryTest
import org.zstack.test.integration.core.job.FakeJob
import org.zstack.testlib.SubCase

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2017-03-24.
 */
//base on TestJob
class FakeJobCase extends SubCase{
    FakeJobConfig conf
    JobQueueFacade jobf
    long num1 = 50
    long num2 = 10
    int num3 = 100
    long num4 = 10

    @Override
    void setup() {
        INCLUDE_CORE_SERVICES = false

        spring {
            include("JobForUnitTest.xml")
        }
    }

    @Override
    void environment() {
        conf = bean(FakeJobConfig.class)
        jobf = bean(JobQueueFacade.class)
    }

    @Override
    void test() {

        TestJob()
        TestJob2()
        TestJobReturnValue()
        TestJobReturnValueFail()
    }
    @Override
    void clean() {

    }

    private void startJob(Job job){
        startJob(job, new NopeCompletion())
    }

    private void startJob(Job job, Completion completion){

        startJob(job, new ReturnValueCompletion<Object>(completion) {
            @Override
            public void success(Object returnValue) {
                completion.success()
            }

            @Override
            public void fail(ErrorCode errorCode) {
                completion.fail(errorCode)
            }
        },null)
    }

    private <T> void startJob(Job job,  ReturnValueCompletion<T> complete, Class<? extends T> returnType){
        jobf.execute("fake-job", "TestJob", job, complete, returnType)
    }


    void TestJob(){
        FakeJobConfig conf = new FakeJobConfig()
        for (long i = 0; i < num1; i++) {
            startJob(new FakeJob(i,conf))
        }

        TimeUnit.SECONDS.sleep(15)
        long count = 0
        for (Long l : conf.indexs) {
            if (l < count) {
                assert false
            }
            count = l
        }
    }

    void TestJob2(){
        conf.success = true
        for (long i = 0; i < num2; i++) {
            startJob(new FakeJob2(conf))
        }

        TimeUnit.SECONDS.sleep(15)
        assert conf.success
    }

    void TestJobReturnValue(){
        boolean success = true
        int retGot = 0
        CountDownLatch latch = new CountDownLatch(num3)

        for (long i = 0; i < num3; i++) {
            startJob(new FakeJobReturnValue(i, conf), new ReturnValueCompletion<Long>(null) {
                @Override
                public void success(Long returnValue) {
                    logger.debug(String.format("get return value[%s]", returnValue));
                    retGot++
                    if (returnValue != i) {
                        logger.debug(String.format("expect %s but %s", index, returnValue));
                        success = false
                    }
                    latch.countDown()
                }

                @Override
                public void fail(ErrorCode errorCode) {
                    logger.debug(errorCode.toString())
                    success = false
                    latch.countDown()
                }
            }, Long.class)
        }

    }

    void TestJobReturnValueFail(){
        boolean success = true
        int retGot = 0

        for (long i = 0; i < num4; i++) {
            startJob(new FakeJobReturnValueFail(i, conf), new ReturnValueCompletion<Long>(null) {
                @Override
                public void success(Long returnValue) {
                    logger.debug(String.format("job[%s] unwanted success", returnValue))
                    success = false
                }

                @Override
                public void fail(ErrorCode errorCode) {
                    logger.debug(errorCode.toString())
                    retGot++
                }
            }, Long.class)
        }
    }

}
