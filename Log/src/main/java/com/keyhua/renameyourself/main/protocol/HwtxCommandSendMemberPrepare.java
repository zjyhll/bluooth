package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 6, UUID = 0xFF23, 准备同步队员信息表给设备
 */
public class HwtxCommandSendMemberPrepare extends HwtxBaseCommand {

	public HwtxCommandSendMemberPrepare() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_SEND_MEMBER_PREPARE);
		// 该命令通过蓝牙接口发送的UUID
		super.setBluetoothPropertyUuidSend("0000ff23-0000-1000-8000-00805f9b34fb");
		super.setBluetoothPropertyUuidRead("0000ff24-0000-1000-8000-00805f9b34fb");
	}

	public Integer getMemberDataLength() {
		return HwtxCommandUtility.bytesToInt32(getCommandDataRaw());
	}

	public void setMemberDataLength(Integer memberDataLength)
			throws HwtxCommandException {
		// Int32 转换的 byte数组只有4字节。data为6字节，需要填充
		Integer dataLength = getCommandDataRaw().length;
		byte[] bytes = HwtxCommandUtility.int32ToBytes(memberDataLength);
		setCommandDataRaw(HwtxCommandUtility.setBitsOfBytes(
				new byte[dataLength], 0, bytes.length * 8, bytes));
	}
}
