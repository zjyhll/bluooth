package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 2, UUID = 0xFF11, 查询模式
 */
public class HwtxCommandQueryMCUFW extends HwtxBaseCommand {

	public HwtxCommandQueryMCUFW() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_MCUFW);
		// 该命令通过蓝牙接口发送的UUID app->蓝牙设备
		super.setBluetoothPropertyUuidSend("0000ff15-0000-1000-8000-00805f9b34fb");
		// super.setBluetoothPropertyUuidSend("91cc0002-a393-3087-7d75-da3752217337");
		// 蓝牙设备->app
		super.setBluetoothPropertyUuidRead("0000ff16-0000-1000-8000-00805f9b34fb");
		// super.setBluetoothPropertyUuidRead("91cc0003-a393-3087-7d75-da3752217337");
	}
}
