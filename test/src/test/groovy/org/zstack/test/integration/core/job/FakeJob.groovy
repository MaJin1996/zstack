package org.zstack.test.integration.core.job;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.zstack.core.errorcode.ErrorFacade;
import org.zstack.core.job.Job;
import org.zstack.core.job.JobContext;
import org.zstack.test.integration.core.job.FakeJobConfig;
import org.zstack.header.core.ReturnValueCompletion;
import org.zstack.utils.Utils;
import org.zstack.utils.logging.CLogger;

/**
 * Created by Administrator on 2017-03-24.
 */

public class FakeJob implements Job{
    CLogger logger = Utils.getLogger(FakeJob.class);

    @JobContext
    private long index

    private FakeJobConfig fl

    public FakeJob(){
    }

    public FakeJob(long i, FakeJobConfig conf){
        fl = conf
        index = i
    }

    @Override
    public void run(ReturnValueCompletion<Object> completion){
        try {
            logger.debug(String.format("job %s is executing", index))
            fl.indexs.add(index)
        } finally {
            completion.success(null)
        }
    }
    public FakeJobConfig getConfig(){
        return fl
    }
}
