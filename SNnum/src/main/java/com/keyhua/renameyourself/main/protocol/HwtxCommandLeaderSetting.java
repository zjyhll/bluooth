package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 1, UUID = 0xFF11, 启动或者取消领队模式
 */
public class HwtxCommandLeaderSetting extends HwtxBaseCommand {

	public HwtxCommandLeaderSetting() {
		super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_LEADER_SETTING);
		// 该命令通过蓝牙接口发送的UUID
		super.setBluetoothPropertyUuidSend("0000ff11-0000-1000-8000-00805f9b34fb");
	}

	// 取消领队
	public void disableLeader() {
		try {
			super.setCommandDataRaw(HwtxCommandConstant.HETX_COMMAND_DATA_LEADER_SETTING_DISABLE);
		} catch (HwtxCommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 设置为领队模式
	public void enableLeader() {
		try {
			super.setCommandDataRaw(HwtxCommandConstant.HETX_COMMAND_DATA_LEADER_SETTING_ENABLE);
		} catch (HwtxCommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
