package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 8, UUID = 0xFF23, 从设备获取队员信息表
 */
public class HwtxCommandReceiveMember extends HwtxBaseCommand {

	public HwtxCommandReceiveMember() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_RECEIVE_MEMBER);
		// 该命令通过蓝牙接口发送的UUID
		super.setBluetoothPropertyUuidSend("0000ff23-0000-1000-8000-00805f9b34fb");
		super.setBluetoothPropertyUuidRead("0000ff24-0000-1000-8000-00805f9b34fb");
	}

}
