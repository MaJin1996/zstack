package org.zstack.test.integration.plugin

/**
 * Created by Administrator on 2017-03-22.
 */
import org.zstack.testlib.SpringSpec
import org.zstack.testlib.Test

/**
 * Created by xing5 on 2017/2/27.
 */
class PluginTest  extends Test {
    static SpringSpec springSpec = makeSpring {
        virtualRouter()
        vyos()
        kvm()
        localStorage()
        sftpBackupStorage()
        //include("LdapManagerImpl.xml")
        //include("vip.xml")
        //securityGroup()
    }

    @Override
    void setup() {
        useSpring(springSpec)

    }

    @Override
    void environment() {

    }

    @Override
    void test() {
        runSubCases()
    }
}
