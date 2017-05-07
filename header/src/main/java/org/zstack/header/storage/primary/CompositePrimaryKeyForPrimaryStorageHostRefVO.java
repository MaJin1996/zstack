package org.zstack.header.storage.primary;

/**
 * Created by Administrator on 2017-05-08.
 */
public class CompositePrimaryKeyForPrimaryStorageHostRefVO {
    private String hostUuid;
    private String primaryStorageUuid;

    public String getHostUuid() {
        return hostUuid;
    }

    public void setHostUuid(String hostUuid) {
        this.hostUuid = hostUuid;
    }

    public String getPrimaryStorageUuid() {
        return primaryStorageUuid;
    }

    public void setPrimaryStorageUuid(String primaryStorageUuid) {
        this.primaryStorageUuid = primaryStorageUuid;
    }
}
