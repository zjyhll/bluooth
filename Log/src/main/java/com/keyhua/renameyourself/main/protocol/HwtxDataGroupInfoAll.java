package com.keyhua.renameyourself.main.protocol;

import java.util.ArrayList;

/**
 * //T2:表2 //TODO: 领队宝/同行宝MCU使用 --MCU维护的核心小表 __packed typedef struct
 * _tag_HWTX_GroupInfoAll_ { unsigned short wCrcGrpInfoAll;
 * //本数据结构，除了wCrcGrpInfoAll之外所有数据的CRC值
 * 。(CRC计算：包括从dwSizeInByte开始，到最后一个成员的HWTX_MemberInfo内容） unsigned short
 * wSizeInByte; // 整个有效表的实际总大小：包括wCrcGrpInfoAll,
 * dwSizeInByte~到最后一个成员的HWTX_MemberInfo内容。 unsigned long dwRtcTime;
 * //时间戳，来自BtApp大表解析 unsigned short wGroupID; //组ID，由领队宝MCU生成，以领队宝的SN号作为随机种子。
 * //TODO: bGrpTxbCount <=bGrpTotalMember unsigned char bGrpCountDevice;
 * //小组领队宝同行宝总数。（设备总数：包含有领队宝设备、同行宝设备的所有成员。不含无设备的成员或未参加的成员） unsigned char
 * bGrpCountAttend; //(unused)：实际参加的总人数:含有设备的，无设备； unsigned char bGrpCountTotal;
 * //(unused)：报名总人数。小组总人数：包括有领队宝设备的、有同行宝设备的、无同行宝但参加了本次活动的队员、报名但未参加的队员。 unsigned
 * long dwGroupSeed; //生成组ID的种子，由领队宝SN相关 HWTX_MemberInfo_Map
 * tIndexMemberInfoMapArray[MAX_GRPCOUNT_DEV_SIZE]; }HWTX_GroupInfoAll;
 */
public class HwtxDataGroupInfoAll extends HwtxBluetoothSerializable {

	// unsigned short, 2字节
	private byte crcGrpInfoAllBtApp[] = { 0x0, 0x0 };

	// unsigned short, 2字节
	private byte sizeInByte[] = { 0x0, 0x0 };

	// unsigned long, 4字节
	private byte rtcTime[] = { 0x0, 0x0, 0x0, 0x0 };

	// unsigned short, 2字节
	private byte groupID[] = { 0x0, 0x0 };

	// unsigned char, 1字节
	private byte grpCountDevice[] = { 0x0 };

	// unsigned char, 1字节
	private byte grpCountAttend[] = { 0x0 };

	// unsigned char, 1字节
	private byte grpCountTotal[] = { 0x0 };

	// unsigned long, 4字节
	private byte groupSeed[] = { 0x0, 0x0, 0x0, 0x0 };

	// 可变长的数组，生成的字节流按数组中实际元素的个数来生成，不需要填充到数组最大长度
	// 元素个数最大不超过 MAX_GRPCOUNT_DEV_SIZE，生成字节流时，最多只取前 MAX_GRPCOUNT_DEV_SIZE 个数组元素
	private ArrayList<HwtxDataMemberInfoMap> indexMemberInfoMapArray = new ArrayList<HwtxDataMemberInfoMap>();

	public HwtxDataGroupInfoAll() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte[] toBytes() {

		byte[] bytes = {};

		// 注意生成的字节流的顺序必须严格保证，不能随意调换
		bytes = HwtxCommandUtility
				.appendBytesToBytes(bytes, crcGrpInfoAllBtApp);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, sizeInByte);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, rtcTime);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, groupID);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, grpCountDevice);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, grpCountAttend);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, grpCountTotal);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, groupSeed);

		for (int i = 0; ((i < indexMemberInfoMapArray.size()) && (i < HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE)); ++i) {
			bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
					indexMemberInfoMapArray.get(i).toBytes());
		}

		return bytes;
	}

	@Override
	public void fromBytes(byte[] bytes) {

		Integer start = 0;

		crcGrpInfoAllBtApp = HwtxCommandUtility.extractBytesFromBytes(bytes,
				start, crcGrpInfoAllBtApp.length);
		start += crcGrpInfoAllBtApp.length;

		sizeInByte = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				sizeInByte.length);
		start += sizeInByte.length;

		rtcTime = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				rtcTime.length);
		start += rtcTime.length;

		groupID = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				groupID.length);
		start += groupID.length;

		grpCountDevice = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				grpCountDevice.length);
		start += grpCountDevice.length;

		grpCountAttend = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				grpCountAttend.length);
		start += grpCountAttend.length;

		grpCountTotal = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				grpCountTotal.length);
		start += grpCountTotal.length;

		groupSeed = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				groupSeed.length);
		start += groupSeed.length;

		Integer remainLengthOfBytes = bytes.length - start;
		Integer arrayItemLength = (new HwtxDataMemberInfoMap()).toBytes().length;
		Integer arrayLength = remainLengthOfBytes / arrayItemLength;
		indexMemberInfoMapArray.clear();
		for (int i = 0; i < arrayLength; ++i) {
			HwtxDataMemberInfoMap memberInfoMap = new HwtxDataMemberInfoMap();
			byte memberInfoMapBytes[] = HwtxCommandUtility
					.extractBytesFromBytes(bytes, start, arrayItemLength);
			memberInfoMap.fromBytes(memberInfoMapBytes);
			indexMemberInfoMapArray.add(memberInfoMap);
			start += arrayItemLength;
		}

	}

	public byte[] getCrcGrpInfoAllBtAppRaw() {
		return crcGrpInfoAllBtApp;
	}

	public void setCrcGrpInfoAllBtAppRaw(byte crcGrpInfoAllBtApp[]) {
		this.crcGrpInfoAllBtApp = crcGrpInfoAllBtApp;
	}

	public Short getCrcGrpInfoAllBtAppShort() {
		return HwtxCommandUtility.bytesToInt16(crcGrpInfoAllBtApp);
	}

	public void setCrcGrpInfoAllBtAppShort(Short value) {
		this.crcGrpInfoAllBtApp = HwtxCommandUtility.int16ToBytes(value);
	}

	public byte[] getSizeInByteRaw() {
		return sizeInByte;
	}

	public void setSizeInByteRaw(byte sizeInByte[]) {
		this.sizeInByte = sizeInByte;
	}

	public Short getSizeInByteShort() {
		return HwtxCommandUtility.bytesToInt16(sizeInByte);
	}

	public void setSizeInByteShort(Short value) {
		this.sizeInByte = HwtxCommandUtility.int16ToBytes(value);
	}

	public byte[] getRtcTimeRaw() {
		return rtcTime;
	}

	public void setRtcTimeRaw(byte rtcTime[]) {
		this.rtcTime = rtcTime;
	}

	public Integer getRtcTimeInteger() {
		return HwtxCommandUtility.bytesToInt32(rtcTime);
	}

	public void setRtcTimeInteger(Integer value) {
		this.rtcTime = HwtxCommandUtility.int32ToBytes(value);
	}

	public byte[] getGroupIDRaw() {
		return groupID;
	}

	public void setGroupIDRaw(byte groupID[]) {
		this.groupID = groupID;
	}

	public Short getGroupIDShort() {
		return HwtxCommandUtility.bytesToInt16(groupID);
	}

	public void setGroupIDShort(Short value) {
		this.groupID = HwtxCommandUtility.int16ToBytes(value);
	}

	public byte[] getGrpCountDeviceRaw() {
		return grpCountDevice;
	}

	public void setGrpCountDeviceRaw(byte grpCountDevice[]) {
		this.grpCountDevice = grpCountDevice;
	}

	public byte getGrpCountDeviceByte() {
		return grpCountDevice[0];
	}

	public void setGrpCountDeviceByte(Byte value) {
		this.grpCountDevice[0] = value;
	}

	public byte[] getGrpCountAttendRaw() {
		return grpCountAttend;
	}

	public void setGrpCountAttendRaw(byte grpCountAttend[]) {
		this.grpCountAttend = grpCountAttend;
	}

	public byte getGrpCountAttendByte() {
		return grpCountAttend[0];
	}

	public void setGrpCountAttendByte(Byte value) {
		this.grpCountAttend[0] = value;
	}

	public byte[] getGrpCountTotalRaw() {
		return grpCountTotal;
	}

	public void setGrpCountTotalRaw(byte grpCountTotal[]) {
		this.grpCountTotal = grpCountTotal;
	}

	public byte getGrpCountTotalByte() {
		return grpCountTotal[0];
	}

	public void setGrpCountTotalByte(Byte value) {
		this.grpCountTotal[0] = value;
	}

	public byte[] getGroupSeedRaw() {
		return groupSeed;
	}

	public void setGroupSeedRaw(byte groupSeed[]) {
		this.groupSeed = groupSeed;
	}

	public Integer getGroupSeedInteger() {
		return HwtxCommandUtility.bytesToInt32(groupSeed);
	}

	public void setGroupSeedInteger(Integer value) {
		this.groupSeed = HwtxCommandUtility.int32ToBytes(value);
	}

	public ArrayList<HwtxDataMemberInfoMap> getIndexMemberInfoMapArray() {
		if (indexMemberInfoMapArray.size() > HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE) {
			return (ArrayList<HwtxDataMemberInfoMap>) indexMemberInfoMapArray
					.subList(0, HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE);
		} else {
			return indexMemberInfoMapArray;
		}
	}

	public void setIndexMemberInfoMapArray(
			ArrayList<HwtxDataMemberInfoMap> indexMemberInfoMapArray) {
		if (this.indexMemberInfoMapArray.size() < HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE) {
			this.indexMemberInfoMapArray = indexMemberInfoMapArray;
		} else {
			this.indexMemberInfoMapArray = (ArrayList<HwtxDataMemberInfoMap>) indexMemberInfoMapArray
					.subList(0, HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE);
		}
	}

	public void addIndexMemberInfoMapArray(HwtxDataMemberInfoMap memberInfoMap) {
		if (this.indexMemberInfoMapArray.size() < HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE) {
			this.indexMemberInfoMapArray.add(memberInfoMap);
		}
	}

}
