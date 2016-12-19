package org.zstack.sdk;

import java.util.HashMap;
import java.util.Map;

public class AddMonToCephBackupStorageAction extends AbstractAction {

    private static final HashMap<String, Parameter> parameterMap = new HashMap<>();

    public static class Result {
        public ErrorCode error;
        public AddMonToCephBackupStorageResult value;
    }

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String uuid;

    @Param(required = true, nonempty = true, nullElements = false, emptyString = true, noTrim = false)
    public java.util.List monUrls;

    @Param(required = false)
    public java.util.List systemTags;

    @Param(required = false)
    public java.util.List userTags;

    @Param(required = true)
    public String sessionId;

    public long timeout;
    
    public long pollingInterval;


    public Result call() {
        ApiResult res = ZSClient.call(this);
        Result ret = new Result();
        if (res.error != null) {
            ret.error = res.error;
            return ret;
        }
        
        AddMonToCephBackupStorageResult value = res.getResult(AddMonToCephBackupStorageResult.class);
        ret.value = value == null ? new AddMonToCephBackupStorageResult() : value;
        return ret;
    }

    public void call(final Completion<Result> completion) {
        ZSClient.call(this, new InternalCompletion() {
            @Override
            public void complete(ApiResult res) {
                Result ret = new Result();
                if (res.error != null) {
                    ret.error = res.error;
                    completion.complete(ret);
                    return;
                }
                
                AddMonToCephBackupStorageResult value = res.getResult(AddMonToCephBackupStorageResult.class);
                ret.value = value == null ? new AddMonToCephBackupStorageResult() : value;
                completion.complete(ret);
            }
        });
    }

    Map<String, Parameter> getParameterMap() {
        return parameterMap;
    }

    RestInfo getRestInfo() {
        RestInfo info = new RestInfo();
        info.httpMethod = "POST";
        info.path = "/backup-storage/ceph/{uuid}/mons";
        info.needSession = true;
        info.needPoll = true;
        info.parameterName = "params";
        return info;
    }

}