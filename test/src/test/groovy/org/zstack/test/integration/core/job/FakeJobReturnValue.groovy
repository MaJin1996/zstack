package org.zstack.test.integration.core.job;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.zstack.core.job.Job;
import org.zstack.core.job.JobContext;
import org.zstack.header.core.ReturnValueCompletion;
import org.zstack.utils.Utils;
import org.zstack.utils.logging.CLogger;

public class FakeJobReturnValue implements Job {
    CLogger logger = Utils.getLogger(FakeJobReturnValue.class)

    @JobContext
    private long index

    private FakeJobConfig fl

    private FakeJobReturnValue() {
    }

    public FakeJobReturnValue(long index, FakeJobConfig conf) {
        this.index = index
        fl = conf
    }

    @Override
    public void run(ReturnValueCompletion<Object> complete) {
        try {
            logger.debug(String.format("job %s is executing", index))
        } finally {
            complete.success(index)
        }
    }
    public FakeJobConfig getConfig(){
        return fl
    }
}
