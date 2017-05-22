package org.zstack.test.integration.image

import org.springframework.http.HttpEntity
import org.zstack.header.image.ImagePlatform
import org.zstack.header.network.service.NetworkServiceType
import org.zstack.header.volume.VolumeConstant
import org.zstack.header.volume.VolumeStatus
import org.zstack.header.volume.VolumeVO
import org.zstack.kvm.KVMAgentCommands
import org.zstack.kvm.KVMConstant
import org.zstack.network.service.eip.EipConstant
import org.zstack.network.service.flat.FlatNetworkServiceConstant
import org.zstack.network.service.userdata.UserdataConstant
import org.zstack.sdk.DiskOfferingInventory
import org.zstack.sdk.VmInstanceInventory
import org.zstack.sdk.VolumeInventory
import org.zstack.testlib.EnvSpec
import org.zstack.testlib.SubCase
import org.zstack.utils.data.SizeUnit

/**
 * Created by heathhose on 17-5-22.
 */
class LinuxImagePlatformOtherCase extends SubCase{
    def DOC = """
The vm doesn't support to attach vm, which use image-1 that platform type is other
"""

    EnvSpec env
    VolumeInventory volume
    @Override
    void setup() {
        useSpring(ImageTest.springSpec)
    }

    @Override
    void environment() {
        env = env {
            instanceOffering {
                name = "instanceOffering"
                memory = SizeUnit.GIGABYTE.toByte(8)
                cpu = 4
            }

            diskOffering {
                name = "diskOffering"
                diskSize =  SizeUnit.GIGABYTE.toByte(10)
            }

            sftpBackupStorage {
                name = "sftp"
                url = "/sftp"
                username = "root"
                password = "password"
                hostname = "localhost"

                image {
                    name = "image-1"
                    url = "http://zstack.org/download/test.qcow2"
                    platform = ImagePlatform.Other.toString()
                }

                image {
                    name = "image-2"
                    url = "http://zstack.org/download/test.qcow2"
                }

                image   {
                    name = "vr"
                    url = "http://zstack.org/download/vr.qcow2"
                }
            }

            zone {
                name = "zone"
                description = "test"

                cluster {
                    name = "cluster"
                    hypervisorType = "KVM"

                    kvm {
                        name = "kvm"
                        managementIp = "localhost"
                        username = "root"
                        password = "password"
                    }

                    attachPrimaryStorage("local")
                    attachL2Network("l2")
                }

                localPrimaryStorage {
                    name = "local"
                    url = "/local_ps"
                }

                l2NoVlanNetwork {
                    name = "l2"
                    physicalInterface = "eth0"

                    l3Network {
                        name = "l3"

                        service {
                            provider = FlatNetworkServiceConstant.FLAT_NETWORK_SERVICE_TYPE_STRING
                            types = [NetworkServiceType.DHCP.toString(), EipConstant.EIP_NETWORK_SERVICE_TYPE, UserdataConstant.USERDATA_TYPE_STRING]
                        }

                        ip {
                            startIp = "192.168.100.10"
                            endIp = "192.168.100.100"
                            netmask = "255.255.255.0"
                            gateway = "192.168.100.1"
                        }
                    }

                    l3Network {
                        name = "pubL3"

                        ip {
                            startIp = "11.168.100.10"
                            endIp = "11.168.100.100"
                            netmask = "255.255.255.0"
                            gateway = "11.168.100.1"
                        }
                    }
                }

                attachBackupStorage("sftp")
            }

            vm {
                name = "vm-1"
                useImage("image-1")
                useL3Networks("l3")
                useInstanceOffering("instanceOffering")
            }

            vm {
                name = "vm-2"
                useImage("image-2")
                useL3Networks("l3")
                useInstanceOffering("instanceOffering")
            }

        }
    }

    @Override
    void test() {
        env.create {
            testGetCandidateVmForAttaching()
            testAttachVolumeToVm()
        }
    }

    void testGetCandidateVmForAttaching(){
        def offer = env.inventoryByName("diskOffering") as DiskOfferingInventory
        def vm  = env.inventoryByName("vm-2") as VmInstanceInventory

        volume = createDataVolume {
            name = "test-1"
            diskOfferingUuid =  offer.uuid
        } as VolumeInventory

        def vms = getDataVolumeAttachableVm {
            volumeUuid = volume.uuid
        } as List<VmInstanceInventory>

        assert vms.size() == 1
        assert vms.get(0).uuid == vm.uuid
    }

    void testAttachVolumeToVm(){
        def vm = env.inventoryByName("vm-1") as VmInstanceInventory
        def vm2 = env.inventoryByName("vm-2") as VmInstanceInventory

        def cmd = null
        env.afterSimulator(KVMConstant.KVM_ATTACH_VOLUME){rsp, HttpEntity<String> entity ->
            cmd = json(entity.getBody(), KVMAgentCommands.AttachDataVolumeCmd.class)
            return rsp
        }

        expect(AssertionError.class){
            attachDataVolumeToVm {
                volumeUuid = volume.uuid
                vmInstanceUuid = vm.uuid
            }
        }
        assert dbFindByUuid(volume.uuid,VolumeVO.class).status == VolumeStatus.NotInstantiated
        assert cmd == null

        attachDataVolumeToVm {
            volumeUuid = volume.uuid
            vmInstanceUuid = vm2.uuid
        }
        assert dbFindByUuid(volume.uuid,VolumeVO.class).status == VolumeStatus.Ready
        assert cmd != null

    }
    @Override
    void clean() {
        env.delete()
    }
}
