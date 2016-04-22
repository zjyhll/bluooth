package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 9, UUID = 0xFF25, 从app同步参数到设备
 */
public class HwtxCommandReceiveSetting extends HwtxBaseCommand {

	public HwtxCommandReceiveSetting() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_RECEIVE_SETTING);
		// 该命令通过蓝牙接口发送的UUID
		super.setBluetoothPropertyUuidSend("0000ff25-0000-1000-8000-00805f9b34fb");
		super.setBluetoothPropertyUuidRead("0000ff26-0000-1000-8000-00805f9b34fb");
	}

	public void setSettingDataLength(Integer settingDataLength)
			throws HwtxCommandException {
		// Int32 转换的 byte数组只有4字节。data为6字节，需要填充
		Integer dataLength = getCommandDataRaw().length;
		byte[] bytes = HwtxCommandUtility.int32ToBytes(settingDataLength);
		setCommandDataRaw(HwtxCommandUtility.setBitsOfBytes(
				new byte[dataLength], 0, bytes.length * 8, bytes));
	}
}
