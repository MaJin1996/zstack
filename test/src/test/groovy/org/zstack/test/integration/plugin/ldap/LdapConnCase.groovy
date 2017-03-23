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
        assert false
        env.delete()
    }

    void testLdapConn(){
        assert false
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
