package com.keyhua.renameyourself.main.protocol;

/**
 * //TODO：一台同行宝的最小信息 __packed typedef struct _tag_HWTX_MEMBERINFO_ {
 * Member_DevInfo_U tDevInfo; //成员的设备信息，1Byte unsigned char
 * strDeviceSN[MAX_DEVSN_SIZE];
 * //唯一的设备SN串号，固定6字节的数字，由设备量产时写入。有非0值，则表示该队员有同行宝；如果为0值，则表示该队员无同行宝硬件。
 * }HWTX_MemberInfo;
 */
public class HwtxDataMemberInfo extends HwtxBluetoothSerializable {

	// 可序列化的成员对象
	private HwtxDataMemberDeviceInfo devInfo = new HwtxDataMemberDeviceInfo();

	// 定长数组
	private byte deviceSn[] = new byte[HwtxCommandConstant.MAX_DEVSN_SIZE];

	public HwtxDataMemberInfo() {

	}

	@Override
	public byte[] toBytes() {

		byte[] bytes = {};

		// 设置 tDevInfo
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, devInfo.toBytes());

		// 设置 strDeviceSN
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, deviceSn);

		return bytes;
	}

	@Override
	public void fromBytes(byte[] bytes) {

		Integer start = 0;

		byte[] devInfoBytes = HwtxCommandUtility.extractBytesFromBytes(bytes,
				start, devInfo.toBytes().length);
		devInfo.fromBytes(devInfoBytes);
		start += devInfo.toBytes().length;

		deviceSn = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				deviceSn.length);
	}

	public HwtxDataMemberDeviceInfo getDevInfo() {
		return devInfo;
	}

	public void setDevInfo(HwtxDataMemberDeviceInfo devInfo) {
		this.devInfo = devInfo;
	}

	public byte[] getDeviceSnRaw() {
		return deviceSn;
	}

	public void setDeviceSnRaw(byte deviceSn[]) {
		this.deviceSn = deviceSn;
	}

	public String getDeviceSnString() {
		return new String(deviceSn);
	}

	public void setDeviceSnString(String value) {
		this.deviceSn = HwtxCommandUtility.copyBytes(value.getBytes(),
				this.deviceSn.length);
	}

}
