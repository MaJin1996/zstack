package org.zstack.network.securitygroup;

import java.util.List;

/**
 * Created by MaJin on 2017-06-08.
 */
public class SecurityGroupBaseRuleTO {
    private String securityGroupUuid;
    private String remoteGroupUuid;
    private List<String> remoteGroupVmIps;
    private int startPort;
    private int endPort;

    public int getStartPort() {
        return startPort;
    }

    public int getEndPort() {
        return endPort;
    }

    public List<String> getRemoteGroupVmIps() {
        return remoteGroupVmIps;
    }

    public String getRemoteGroupUuid() {
        return remoteGroupUuid;
    }

    public String getSecurityGroupUuid() {
        return securityGroupUuid;
    }

    public void setEndPort(int endPort) {
        this.endPort = endPort;
    }

    public void setRemoteGroupUuid(String remoteGroupUuid) {
        this.remoteGroupUuid = remoteGroupUuid;
    }

    public void setRemoteGroupVmIps(List<String> remoteGroupVmIps) {
        this.remoteGroupVmIps = remoteGroupVmIps;
    }

    public void setSecurityGroupUuid(String securityGroupUuid) {
        this.securityGroupUuid = securityGroupUuid;
    }

    public void setStartPort(int startPort) {
        this.startPort = startPort;
    }
}
