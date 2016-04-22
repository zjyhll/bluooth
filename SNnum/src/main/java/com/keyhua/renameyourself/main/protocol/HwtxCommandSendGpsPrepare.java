package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 5, UUID = 0xFF21, 准备传输GPS数据表给设备
 */
public class HwtxCommandSendGpsPrepare extends HwtxBaseCommand {

	public HwtxCommandSendGpsPrepare() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_SEND_GPS_PREPARE);
		// 该命令通过蓝牙接口发送的UUID
		super.setBluetoothPropertyUuidSend("0000ff21-0000-1000-8000-00805f9b34fb");
		super.setBluetoothPropertyUuidRead("0000ff22-0000-1000-8000-00805f9b34fb");
	}

	public Integer getGpsDataLength() {
		return HwtxCommandUtility.bytesToInt32(getCommandDataRaw());
	}

	public void setGpsDataLength(Integer gpsDataLength)
			throws HwtxCommandException {
		// Int32 转换的 byte数组只有4字节。data为6字节，需要填充
		Integer dataLength = getCommandDataRaw().length;
		byte[] bytes = HwtxCommandUtility.int32ToBytes(gpsDataLength);
		setCommandDataRaw(HwtxCommandUtility.setBitsOfBytes(
				new byte[dataLength], 0, bytes.length * 8, bytes));
	}

}
