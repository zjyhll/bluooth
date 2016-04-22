package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 0x10, UUID = 0xFF13, 将设备号为XXXXXX的同行宝添加入组
 */
public class HwtxCommandTXB extends HwtxBaseCommand {

	public HwtxCommandTXB() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_TXB);
		// 该命令通过蓝牙接口发送的UUID
		super.setBluetoothPropertyUuidSend("0000ff19-0000-1000-8000-00805f9b34fb");
		super.setBluetoothPropertyUuidRead("0000ff1a-0000-1000-8000-00805f9b34fb");
	}

	public String getDeviceSn() {
		return new String(getCommandDataRaw());
	}

	public void setDeviceSn(String deviceSn) throws HwtxCommandException {
		setCommandDataRaw(HwtxCommandUtility.copyBytes(deviceSn.getBytes(),
				getCommandDataRaw().length));
	}

}
