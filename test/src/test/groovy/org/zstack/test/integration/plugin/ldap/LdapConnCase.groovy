package org.zstack.test.integration.plugin.ldap

import com.unboundid.ldap.sdk.LDAPInterface
import com.unboundid.ldap.sdk.SearchResult
import com.unboundid.ldap.sdk.SearchScope
import org.junit.Assert
import org.junit.Rule
import org.zapodot.junit.ldap.EmbeddedLdapRule
import org.zapodot.junit.ldap.EmbeddedLdapRuleBuilder
import org.zstack.sdk.AddLdapServerAction
import org.zstack.sdk.AddLdapServerResult
import org.zstack.sdk.LdapServerInventory
import org.zstack.test.integration.plugin.Env
import org.zstack.test.integration.plugin.PluginTest
import org.zstack.testlib.EnvSpec
import org.zstack.testlib.SubCase
import org.zstack.utils.data.SizeUnit

import javax.swing.Spring
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2017-03-22.
 */


//base on TestLdapConn
class LdapConnCase extends SubCase {
    EnvSpec env
    public static final String DOMAIN_DSN = "dc=example,dc=com"
    //@Rule
    //public EmbeddedLdapRule embeddedLdapRule = EmbeddedLdapRuleBuilder.newInstance().bindingToPort(1888).
    //        usingDomainDsn(DOMAIN_DSN).importingLdifs("users-import.ldif").build()

    @Override
    void setup() {
        spring {
            kvm()
            localStorage()
            sftpBackupStorage()
            include("LdapManagerImpl.xml")
        }
    }

    @Override
    void environment() {
        /*
        env = env{
            instanceOffering {
                name = "instanceOffering"
                memory = SizeUnit.GIGABYTE.toByte(8)
                cpu = 4
            }

            diskOffering {
                name = "diskOffering"
                diskSize = SizeUnit.GIGABYTE.toByte(20)
            }

            sftpBackupStorage {
                name = "sftp"
                url = "/sftp"
                username = "root"
                password = "password"
                hostname = "localhost"

                image {
                    name = "image"
                    url  = "http://zstack.org/download/test.qcow2"
                }
            }

            zone {
                name = "zone"
                description = "test"

                cluster {
                    name = "cluster"
                    hypervisorType = "KVM"

                    kvm {
                        name = "host"
                        managementIp = "localhost"
                        username = "root"
                        password = "password"
                    }

                    kvm {
                        name = "host2"
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

                        ip {
                            startIp = "192.168.100.10"
                            endIp = "192.168.100.100"
                            netmask = "255.255.255.0"
                            gateway = "192.168.100.1"
                        }
                    }

                }


                attachBackupStorage("sftp")
            }

            vm {
                name = "vm"
                useInstanceOffering("instanceOffering")
                useImage("image")
                useL3Networks("l3")
                useRootDiskOffering("diskOffering")
                useHost("host")
            }
        }
        */
        env = Env.localStorageOneVmEnv()
    }

    @Override
    void test() {
        env.create {
            testLdapConn()
        }

    }

    @Override
    void clean() {

        env.delete()
    }

    void testLdapConn(){

        //final LDAPInterface ldapConnection = embeddedLdapRule.ldapConnection()
        //final SearchResult searchResult = ldapConnection.search(DOMAIN_DSN, SearchScope.SUB, "(objectClass=person)")
        //Assert.assertEquals(3, searchResult.getEntryCount())


        def result = addLdapServer {
            name = "ldap0"
            description = "test-ldap0"
            base = DOMAIN_DSN
            url = "ldap://localhost:1888"
            username = ""
            password = ""
            encryption = "None"
            sessionId = currentEnvSpec.session.uuid
        } as LdapServerInventory
        String LdapUuid = result.uuid

        deleteLdapServer {
            uuid = LdapUuid
            sessionId = currentEnvSpec.session.uuid
        }
        result = addLdapServer {
            name = "ldap1"
            description = "test-ldap1"
            base = "dc=mevoco,dc=com"
            url = "ldap://172.20.11.200:389"
            username = "uid=admin,cn=users,cn=accounts,dc=mevoco,dc=com"
            password = "password"
            encryption = "TLS"
            sessionId = currentEnvSpec.session.uuid
        } as LdapServerInventory
        LdapUuid = result.uuid
        deleteLdapServer {
            uuid = LdapUuid
            sessionId = currentEnvSpec.session.uuid
        }

        result = addLdapServer {
            name = "ldap2"
            description = "test-ldap2"
            base = "dc=mevoco,dc=com"
            url = "ldap://172.20.11.200:389"
            username = "uid=admin,cn=users,cn=accounts,dc=mevoco,dc=com"
            password = "password"
            encryption = "None"
            sessionId = currentEnvSpec.session.uuid
        } as LdapServerInventory
        LdapUuid = result.uuid
        deleteLdapServer {
            uuid = LdapUuid
            sessionId = currentEnvSpec.session.uuid
        }

        result = addLdapServer {
            name = "ldap3"
            description = "test-ldap3"
            base = "dc=learnitguide,dc=net"
            url = "ldap://172.20.12.176:389"
            username = "cn=Manager,dc=learnitguide,dc=net"
            password = "password"
            encryption = "None"
            sessionId = currentEnvSpec.session.uuid
        } as LdapServerInventory
        LdapUuid = result.uuid
        deleteLdapServer {
            uuid = LdapUuid
            sessionId = currentEnvSpec.session.uuid
        }


        result = addLdapServer {
            name = "ldap4"
            description = "test-ldap4"
            base = "dc=mevoco,dc=com"
            url = "ldap://172.20.11.200:389"
            username = "uid=admin,cn=users,cn=accounts,dc=mevoco,dc=com"
            password = "password"
            encryption = "None"
            sessionId = currentEnvSpec.session.uuid
        } as LdapServerInventory
        LdapUuid = result.uuid
        deleteLdapServer {
            uuid = LdapUuid
            sessionId = currentEnvSpec.session.uuid
        }
    }
}
