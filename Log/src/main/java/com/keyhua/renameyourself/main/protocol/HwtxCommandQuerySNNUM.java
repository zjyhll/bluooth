package com.keyhua.renameyourself.main.protocol;

/**
 * Type = 2, UUID = 0xFF11, 查询模式
 */
public class HwtxCommandQuerySNNUM extends HwtxBaseCommand {

    public HwtxCommandQuerySNNUM() {
        super.setCommandTypeRaw(HwtxCommandConstant.HETX_COMMAND_TYPE_SNNUM);
        // 该命令通过蓝牙接口发送的UUID app->蓝牙设备
        super.setBluetoothPropertyUuidSend("0000ff19-0000-1000-8000-00805f9b34fb");
        super.setBluetoothPropertyUuidRead("0000ff1a-0000-1000-8000-00805f9b34fb");
    }
}
