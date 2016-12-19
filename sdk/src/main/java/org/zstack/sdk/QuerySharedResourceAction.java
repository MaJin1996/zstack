package org.zstack.sdk;

import java.util.HashMap;
import java.util.Map;

public class QuerySharedResourceAction extends QueryAction {

    private static final HashMap<String, Parameter> parameterMap = new HashMap<>();

    public static class Result {
        public ErrorCode error;
        public QuerySharedResourceResult value;
    }



    public Result call() {
        ApiResult res = ZSClient.call(this);
        Result ret = new Result();
        if (res.error != null) {
            ret.error = res.error;
            return ret;
        }
        
        QuerySharedResourceResult value = res.getResult(QuerySharedResourceResult.class);
        ret.value = value == null ? new QuerySharedResourceResult() : value;
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
                
                QuerySharedResourceResult value = res.getResult(QuerySharedResourceResult.class);
                ret.value = value == null ? new QuerySharedResourceResult() : value;
                completion.complete(ret);
            }
        });
    }

    Map<String, Parameter> getParameterMap() {
        return parameterMap;
    }

    RestInfo getRestInfo() {
        RestInfo info = new RestInfo();
        info.httpMethod = "GET";
        info.path = "/accounts/resources";
        info.needSession = true;
        info.needPoll = false;
        info.parameterName = "";
        return info;
    }

}