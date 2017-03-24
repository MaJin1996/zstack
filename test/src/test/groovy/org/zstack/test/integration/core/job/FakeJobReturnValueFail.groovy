package org.zstack.test.integration.core.job;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.zstack.core.errorcode.ErrorFacade;
import org.zstack.core.job.Job;
import org.zstack.core.job.JobContext;
import org.zstack.header.core.ReturnValueCompletion;
import org.zstack.utils.Utils;
import org.zstack.utils.logging.CLogger;


public class FakeJobReturnValueFail implements Job {
    CLogger logger = Utils.getLogger(FakeJobReturnValueFail.class)

    @JobContext
    private long index

    private FakeJobConfig fl

    private ErrorFacade errf

    private FakeJobReturnValueFail() {
    }

    public FakeJobReturnValueFail(long index, FakeJobConfig conf) {
        this.index = index
        fl = conf
    }

    @Override
    public void run(ReturnValueCompletion<Object> complete) {
        logger.debug(String.format("job %s is executing", index))
        complete.fail(errf.stringToOperationError("fail on purpose"))
    }
    public FakeJobConfig getConfig(){
        return fl
    }
}
