-- ----------------------------
--  Table structure for `SnapshotUsageVO`
-- ----------------------------
CREATE TABLE `SnapShotUsageVO` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `volumeUuid` varchar(32) NOT NULL,
  `SnapshotUuid` varchar(32) NOT NULL,
  `SnapshotStatus` varchar(64) NOT NULL,
  `SnapshotName` varchar(255) NOT NULL,
  `accountUuid` varchar(32) NOT NULL,
  `SnapshotSize` bigint unsigned NOT NULL,
  `dateInLong` bigint unsigned NOT NULL,
  `inventory` text,
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE SecurityGroupRuleVO ADD COLUMN `remoteSecurityGroupUuid` varchar(255) DEFAULT NULL;
ALTER TABLE SecurityGroupRuleVO ADD CONSTRAINT fkSecurityGroupRuleVOSecurityGroupVO FOREIGN KEY (remoteSecurityGroupUuid) REFERENCES SecurityGroupVO (uuid) ON DELETE CASCADE ;
