package org.zstack.test.integration.kvm.vm

import groovy.transform.TypeChecked
import org.bouncycastle.jce.provider.symmetric.TEA
import org.zstack.core.db.DatabaseFacade
import org.zstack.sdk.*
import org.zstack.test.integration.kvm.Env
import org.zstack.test.integration.kvm.KvmTest
import org.zstack.testlib.EnvSpec
import org.zstack.testlib.SubCase
import org.zstack.testlib.VmSpec
import org.zstack.utils.data.SizeUnit
import org.zstack.utils.gson.JSONObjectUtil
/**
 * Created by MaJin on 2017-03-16.
 */

//base on TestNicQosMixed and TestVolumeQosMixed
class NicQosMixedCase extends SubCase{
    EnvSpec env

    def DOC = """
test a VM's start/stop/reboot/destroy/recover operations 
"""

    @Override
    void setup() {
        useSpring(KvmTest.springSpec)
    }

    @Override
    void environment() {
        env = Env.oneVmAndQosEnv()
    }

    @Override
    void test() {
        env.create {
            //TestSetNicQos()
            TestSetVolumeQos()
            //TestQosAfterClone()
        }
    }

    @Override
    void clean(){
        env.delete()
    }

    void TestSetNicQos(){
        VmInstanceInventory vmInv = env.inventoryByName("vm") as VmInstanceInventory
        DatabaseFacade dbf = bean(DatabaseFacade.class)

        assert vmInv != null
        assert !vmInv.getVmNics().isEmpty()

        for(VmNicInventory nicInv : vmInv.getVmNics()){
            if( nicInv.l3NetworkUuid == vmInv.getDefaultL3NetworkUuid()){ // pending fix bug 1996
                setNicQos {
                    sessionId = currentEnvSpec.session.uuid
                    uuid = nicInv.uuid

                    outboundBandwidth = SizeUnit.MEGABYTE.toByte(2)
                    inboundBandwidth = SizeUnit.MEGABYTE.toByte(1)
                }

                GetNicQosResult result = getNicQos {
                    uuid = nicInv.uuid
                    sessionId = currentEnvSpec.session.uuid
                } as GetNicQosResult

                assert result.inboundBandwidth == SizeUnit.MEGABYTE.toByte(1)
                assert result.outboundBandwidth == SizeUnit.MEGABYTE.toByte(2)
            }

        }
    }


    void TestSetVolumeQos(){
        VmInstanceInventory vmInv = env.inventoryByName("vm") as VmInstanceInventory
        DatabaseFacade dbf = bean(DatabaseFacade.class)

        assert vmInv

        String rootVolumeUuid = vmInv.getRootVolumeUuid()
        assert rootVolumeUuid
        setVolumeQos {
            sessionId = currentEnvSpec.session.uuid
            uuid = rootVolumeUuid
            volumeBandwidth = SizeUnit.MEGABYTE.toByte(30)
        }
        GetVolumeQosResult rootVmRes = getVolumeQos {
            uuid = rootVolumeUuid
            sessionId = currentEnvSpec.session.uuid
        } as GetVolumeQosResult
        assert rootVmRes.volumeBandwidth == SizeUnit.MEGABYTE.toByte(30)

        for( VolumeInventory diskInv : vmInv.getAllVolumes()){
            setVolumeQos {
                sessionId = currentEnvSpec.session.uuid
                uuid = diskInv.uuid
                volumeBandwidth = SizeUnit.MEGABYTE.toByte(20)
            }
            GetVolumeQosResult diskVmRes = getVolumeQos {
                uuid = diskInv.uuid
                sessionId = currentEnvSpec.session.uuid
            } as GetVolumeQosResult

            assert diskVmRes.volumeBandwidth == SizeUnit.MEGABYTE.toByte(20)
        }
    }

    void TestQosAfterClone(){
        VmInstanceInventory vmInv = env.inventoryByName("vm") as VmInstanceInventory
        CloneVmInstanceResult result = cloneVmInstance {
            vmInstanceUuid = vmInv.uuid
            sessionId = currentEnvSpec.session.uuid
            names = ["cloneVm"]
        } as CloneVmInstanceResult
        VmInstanceInventory cloneVmInv = result.result.inventories.get(0).inventory
        String cloneDefaultL3Uuid = result.result.inventories.get(0).inventory.defaultL3NetworkUuid

        assert result.result.numberOfClonedVm == 1
        assert cloneDefaultL3Uuid

        for(VmNicInventory nicInv : cloneVmInv.getVmNics()){
            if( nicInv.l3NetworkUuid == cloneDefaultL3Uuid){ // pending fix bug 1996
                GetNicQosResult qosRes = getNicQos {
                    uuid = nicInv.uuid
                    sessionId = currentEnvSpec.session.uuid
                } as GetNicQosResult

                assert qosRes.inboundBandwidth == SizeUnit.MEGABYTE.toByte(1)
                assert qosRes.outboundBandwidth == SizeUnit.MEGABYTE.toByte(2)
            }
        }

        for(VolumeInventory volumeInv : cloneVmInv.getAllVolumes()){
            GetVolumeQosResult diskVmRes = getVolumeQos {
                uuid = volumeInv.uuid
                sessionId = currentEnvSpec.session.uuid
            } as GetVolumeQosResult
            assert diskVmRes.volumeBandwidth == SizeUnit.MEGABYTE.toByte(20)
        }
        GetVolumeQosResult rootVmRes = getVolumeQos {
            uuid = cloneVmInv.getRootVolumeUuid()
            sessionId = currentEnvSpec.session.uuid
        } as GetVolumeQosResult
        assert rootVmRes.volumeBandwidth == SizeUnit.MEGABYTE.toByte(30)

    }




}
