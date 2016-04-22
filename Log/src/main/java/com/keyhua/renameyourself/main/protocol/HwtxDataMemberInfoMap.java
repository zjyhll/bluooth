package com.keyhua.renameyourself.main.protocol;

/**
 * //带AT编组映射： // 包括 AT编组映射号码 __packed typedef struct _tag_HWTX_MemberInfo_Map_ {
 * unsigned char ATMappingAddr; //AT编组映射号码 HWTX_MemberInfo tMemberInfo;
 * }HWTX_MemberInfo_Map;
 */
public class HwtxDataMemberInfoMap extends HwtxBluetoothSerializable {

	// unsigned char, 1字节
	private byte atMappingAddr[] = { 0x0 };

	// HWTX_MemberInfo 对象
	private HwtxDataMemberInfo memberInfo = new HwtxDataMemberInfo();

	public HwtxDataMemberInfoMap() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte[] toBytes() {

		byte[] bytes = {};

		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, atMappingAddr);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
				memberInfo.toBytes());

		return bytes;
	}

	@Override
	public void fromBytes(byte[] bytes) {

		Integer start = 0;

		atMappingAddr = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				atMappingAddr.length);
		start += atMappingAddr.length;

		byte[] memberInfoBytes = HwtxCommandUtility.extractBytesFromBytes(
				bytes, start, memberInfo.toBytes().length);
		memberInfo.fromBytes(memberInfoBytes);

	}

	public byte[] getAtMappingAddrRaw() {
		return atMappingAddr;
	}

	public void setAtMappingAddrRaw(byte atMappingAddr[]) {
		this.atMappingAddr = atMappingAddr;
	}

	public byte getAtMappingAddrByte() {
		return atMappingAddr[0];
	}

	public void setAtMappingAddrByte(Byte value) {
		this.atMappingAddr[0] = value;
	}

	public HwtxDataMemberInfo getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(HwtxDataMemberInfo memberInfo) {
		this.memberInfo = memberInfo;
	}

}
