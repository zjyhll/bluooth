package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 4, UUID = 0xFF21, 发送获取GPS数据表的命令
 */
public class HwtxCommandReceiveGps extends HwtxBaseCommand {

	public HwtxCommandReceiveGps() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_RECEIVE_GPS);
		// 该命令通过蓝牙接口发送的UUID
		super.setBluetoothPropertyUuidSend("0000ff21-0000-1000-8000-00805f9b34fb");
		super.setBluetoothPropertyUuidRead("0000ff22-0000-1000-8000-00805f9b34fb");
	}

}
