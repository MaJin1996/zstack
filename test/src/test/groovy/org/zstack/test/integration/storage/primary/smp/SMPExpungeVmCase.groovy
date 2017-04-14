package org.zstack.test.integration.storage.primary.smp

import org.zstack.sdk.ClusterInventory
import org.zstack.sdk.PrimaryStorageInventory
import org.zstack.sdk.VmInstanceInventory
import org.zstack.test.integration.storage.SMPEnv
import org.zstack.test.integration.storage.StorageTest
import org.zstack.testlib.EnvSpec
import org.zstack.testlib.SubCase

/**
 * Created by Administrator on 2017-04-14.
 */
class SMPExpungeVmCase extends SubCase {
    EnvSpec env

    @Override
    void setup() {
        useSpring(StorageTest.springSpec)
    }

    @Override
    void environment() {
        env = SMPEnv.oneVmBasicEnv()
    }

    @Override
    void test() {
        env.create {
            testExpungeVm()
        }
    }

    @Override
    void clean(){
        env.delete()
    }

    void testExpungeVm(){
        PrimaryStorageInventory smpInv = env.inventoryByName("smp") as PrimaryStorageInventory
        VmInstanceInventory vmInv = env.inventoryByName("vm") as VmInstanceInventory
        ClusterInventory cluInv = env.inventoryByName("cluster") as ClusterInventory
        detachPrimaryStorageFromCluster {
            primaryStorageUuid = smpInv.uuid
            clusterUuid = cluInv.uuid
            sessionId = currentEnvSpec.session.uuid
        }
        expungeVmInstance {
            uuid = vmInv.uuid
            sessionId = currentEnvSpec.session.uuid
        }
    }

}