package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 0x0E, UUID = 0xFF27, 从设备获取告警/失联表
 */
public class HwtxCommandReceiveWarning extends HwtxBaseCommand {

	public HwtxCommandReceiveWarning() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_RECEIVE_WARNING);
		// 该命令通过蓝牙接口发送的UUID
		super.setBluetoothPropertyUuidSend("0000ff27-0000-1000-8000-00805f9b34fb");
		super.setBluetoothPropertyUuidRead("0000ff28-0000-1000-8000-00805f9b34fb");
	}

}
