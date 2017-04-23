package org.zstack.storage.primary.nfs;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.core.db.Q;
import org.zstack.core.errorcode.ErrorFacade;
import org.zstack.header.apimediator.ApiMessageInterceptionException;
import org.zstack.header.apimediator.ApiMessageInterceptor;
import org.zstack.header.errorcode.ErrorCode;
import org.zstack.header.errorcode.OperationFailureException;
import org.zstack.header.message.APIMessage;
import org.zstack.header.storage.primary.APIUpdatePrimaryStorageMsg;
import org.zstack.header.storage.primary.PrimaryStorageVO;
import org.zstack.header.storage.primary.PrimaryStorageVO_;
import org.zstack.header.vm.VmInstanceState;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

import static org.zstack.core.Platform.operr;

/**
 */
public class NfsPrimaryStorageApiInterceptor implements ApiMessageInterceptor {
    @Autowired
    private DatabaseFacade dbf;
    @Autowired
    private ErrorFacade errf;

    @Override
    public APIMessage intercept(APIMessage msg) throws ApiMessageInterceptionException {
        if (msg instanceof APIAddNfsPrimaryStorageMsg) {
            validate((APIAddNfsPrimaryStorageMsg) msg);
        }else if(msg instanceof APIUpdatePrimaryStorageMsg){
            validate((APIUpdatePrimaryStorageMsg) msg);
        }

        return msg;
    }

    private void validate(APIAddNfsPrimaryStorageMsg msg) {
        new NfsApiParamChecker().checkUrl(msg.getZoneUuid(), msg.getUrl());
    }

    private void validate(APIUpdatePrimaryStorageMsg msg){
        if (msg.getUrl() != null && !msg.getUrl().equals(Q.New(PrimaryStorageVO.class)
                .select(PrimaryStorageVO_.url)
                .eq(PrimaryStorageVO_.uuid, msg.getUuid())
                .findValue())
                ){
            String zoneUuid = Q.New(PrimaryStorageVO.class)
                    .select(PrimaryStorageVO_.zoneUuid)
                    .eq(PrimaryStorageVO_.uuid, msg.getUuid())
                    .findValue();

            NfsApiParamChecker checker = new NfsApiParamChecker();
            checker.checkUrl(zoneUuid, msg.getUrl());
            checker.checkRunningVmForUpdateUrl(msg);
        }
    }



}
