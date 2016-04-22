package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 0x0c, UUID = 0xFF27, 准备同步告警/失联表给设备
 */
public class HwtxCommandSendWarningPrepare extends HwtxBaseCommand {

	public HwtxCommandSendWarningPrepare() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_SEND_WARNING_PREPARE);
		// 该命令通过蓝牙接口发送的UUID
		super.setBluetoothPropertyUuidSend("0000ff27-0000-1000-8000-00805f9b34fb");
		super.setBluetoothPropertyUuidRead("0000ff28-0000-1000-8000-00805f9b34fb");
	}

	public Integer getWarningDataLength() {
		return HwtxCommandUtility.bytesToInt32(getCommandDataRaw());
	}

	public void setWarningDataLength(Integer warningDataLength)
			throws HwtxCommandException {
		// Int32 转换的 byte数组只有4字节。data为6字节，需要填充
		Integer dataLength = getCommandDataRaw().length;
		byte[] bytes = HwtxCommandUtility.int32ToBytes(warningDataLength);
		setCommandDataRaw(HwtxCommandUtility.setBitsOfBytes(
				new byte[dataLength], 0, bytes.length * 8, bytes));
	}
}
