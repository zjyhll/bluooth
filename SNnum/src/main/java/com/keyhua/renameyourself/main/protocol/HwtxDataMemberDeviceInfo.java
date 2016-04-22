package com.keyhua.renameyourself.main.protocol;

/**
 * __packed typedef union _tag_MEMBER_DEVINFO_U_ { unsigned char v; __packed
 * struct __tag_DEVINFO_T_ { unsigned uDevNumInGroup : 5; //该设备队员的组内编号，范围[1,30]
 * ; unsigned uRev:1; //Reserved bit，暂未定义 unsigned uDeviceIsLDB : 1;
 * //本设备是否领队宝（APP确保一个队伍只有一个领队）,0-TXB同行宝（队员）,1-LDB领队宝(领队)； unsigned uDeviceReady
 * : 1; //是否有同行宝设备。 由领队APP设定：根据strDeviceSN是否为非零来判断，0-无同行宝；1-有同行宝 }bit_info;
 * }Member_DevInfo_U;
 */

public class HwtxDataMemberDeviceInfo extends HwtxBluetoothSerializable {

	private byte data[] = { 0x0 };

	public Integer getDevNumInGroup() {
		byte[] bytes = HwtxCommandUtility.extractBitsFromBytes(data, 0, 5);
		return HwtxCommandUtility.bytesToInt32(bytes);
	}

	public void setDevNumInGroup(Integer value) throws HwtxCommandException {

		// setDevNumInGroup 只占 5 bit，不能大于 0x1F
		if (value > 0x1F) {
			throw new HwtxCommandException("输入参数的值不能大于31(0x1F)");
		}
		byte[] bytes = HwtxCommandUtility.int32ToBytes(value);
		data = HwtxCommandUtility.setBitsOfBytes(data, 0, 5, bytes);
	}

	// 返回值:true : LDB领队宝(领队)；false : TXB同行宝（队员）
	public Boolean getDeviceIsLdb() {
		Integer bitValue = HwtxCommandUtility.extractBitFromBytes(data, 6);
		if (bitValue != 0) {
			return true;
		}
		return false;
	}

	public void setDeviceIsLdb(Boolean isLdb) {
		Integer bitValue = 0;
		if (isLdb) {
			bitValue = 1;
		}
		data = HwtxCommandUtility.setBitOfBytes(data, 6, bitValue);
	}

	// 返回值:true :有同行宝；false : 无同行宝
	public Boolean getDeviceReady() {
		Integer bitValue = HwtxCommandUtility.extractBitFromBytes(data, 7);
		if (bitValue != 0) {
			return true;
		}
		return false;
	}

	public void setDeviceReady(Boolean isReady) {
		Integer bitValue = 0;
		if (isReady) {
			bitValue = 1;
		}
		data = HwtxCommandUtility.setBitOfBytes(data, 7, bitValue);
	}

	public HwtxDataMemberDeviceInfo() {
	}

	@Override
	public byte[] toBytes() {
		return data;
	}

	@Override
	public void fromBytes(byte[] bytes) {
		data[0] = bytes[0];
	}

}
