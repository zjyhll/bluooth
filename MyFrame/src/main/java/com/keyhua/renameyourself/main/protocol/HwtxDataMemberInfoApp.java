package com.keyhua.renameyourself.main.protocol;

/**
 * __packed typedef union _tag_MEMBER_MEMBERINFOAPP_U_ { unsigned char v;
 * __packed struct __tag_MEMBERINFOAPP_T_ { unsigned uIsAttend : 1;
 * //该编号成员是否参加了这次活动。0-未参加，1-参加了本次活动 unsigned uIsUseAPP : 1; //该编号成员是否启用APP图标.
 * 0-未使用队员APP，1-使用了队员APP unsigned uRev:6; //Reserved bit，暂未定义 }bit_info;
 * }Member_MemberInfoApp_U;
 */
public class HwtxDataMemberInfoApp extends HwtxBluetoothSerializable {

	private byte data[] = { 0x0 };

	public Boolean getIsAttend() {
		Integer bitValue = HwtxCommandUtility.extractBitFromBytes(data, 0);
		if (bitValue != 0) {
			return true;
		}
		return false;
	}

	public void setIsAttend(Boolean isAttend) {
		Integer bitValue = 0;
		if (isAttend) {
			bitValue = 1;
		}
		data = HwtxCommandUtility.setBitOfBytes(data, 0, bitValue);
	}

	public Boolean getIsUseAPP() {
		Integer bitValue = HwtxCommandUtility.extractBitFromBytes(data, 1);
		if (bitValue != 0) {
			return true;
		}
		return false;
	}

	public void setIsUseAP(Boolean isUseAP) {
		Integer bitValue = 0;
		if (isUseAP) {
			bitValue = 1;
		}
		data = HwtxCommandUtility.setBitOfBytes(data, 1, bitValue);
	}

	public HwtxDataMemberInfoApp() {
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
