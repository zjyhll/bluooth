package com.keyhua.renameyourself.main.protocol;

public abstract class HwtxBaseCommand extends HwtxBluetoothSerializable {

	// 4字节（32位），命令tag号，由APP生成
	private byte commandTag[] = { 0x0, 0x0, 0x0, 0x0 };

	// 4字节（32位），命令的标志位，固定值0x48 0x57 0x54 0x58 "HWTX"
	private byte commandFlag[] = HwtxCommandConstant.HETX_COMMAND_FLAG;

	// 1字节
	private byte commandType[] = HwtxCommandConstant.HETX_COMMAND_TYPE_UNKNOWN;

	// 6字节
	private byte[] commandData = { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };

	// 蓝牙命令发送或接受的特性的UUID
	private String bluetoothPropertyUuidSend = null;
	private String bluetoothPropertyUuidRead = null;

	public HwtxBaseCommand() {
		// 自动生成一个TAG
		commandTag = HwtxTagGenerator.getInstance().generate();
	}

	public byte[] getCommandTagRaw() {
		return commandTag;
	}

	public void setCommandTagRaw(byte commandTag[]) throws HwtxCommandException {
		if (commandTag.length != this.commandTag.length) {
			throw new HwtxCommandException("命令tag号必须为" + this.commandTag.length
					+ "字节");
		}
		this.commandTag = commandTag;
	}

	public Integer getCommandTagInteger() {
		return HwtxCommandUtility.bytesToInt32(commandTag);
	}

	public void setCommandTagInteger(Integer value) {
		this.commandTag = HwtxCommandUtility.int32ToBytes(value);
	}

	public byte[] getCommandFlagRaw() {
		return commandFlag;
	}

	// 命令的 Flag不允许修改
	protected void setCommandFlagRaw(byte commandFlag[])
			throws HwtxCommandException {
		if (commandFlag.length != this.commandFlag.length) {
			throw new HwtxCommandException("命令Flag必须为"
					+ this.commandFlag.length + "字节");
		}
		this.commandFlag = commandFlag;
	}

	public String getCommandFlagString() {
		return new String(commandFlag);
	}

	public void setCommandFlagString(String value) {

		byte[] bytes = value.getBytes();

		// 只取前4个字节
		for (int i = 0; i < this.commandFlag.length; ++i) {
			if (value.length() > i) {
				this.commandFlag[i] = bytes[i];
			} else {
				this.commandFlag[i] = 0x0;
			}
		}
	}

	public byte[] getCommandTypeRaw() {
		return commandType;
	}

	public void setCommandTypeRaw(byte[] commandType) {
		this.commandType = commandType;
	}

	public Integer getCommandTypeInteger() {
		return Integer.valueOf(HwtxCommandUtility.bytesToInt8(commandType));
	}

	public void setCommandTypeInteger(Integer value) {
		this.commandType = HwtxCommandUtility.int8ToBytes(value.byteValue());
	}

	public byte[] getCommandDataRaw() {
		return commandData;
	}

	public void setCommandDataRaw(byte[] commandData)
			throws HwtxCommandException {
		if (commandData.length != this.commandData.length) {
			throw new HwtxCommandException("命令Data必须为"
					+ this.commandData.length + "字节");
		}
		this.commandData = commandData;
	}

	public String getBluetoothPropertyUuidRead() {
		return bluetoothPropertyUuidRead;
	}

	public void setBluetoothPropertyUuidRead(String bluetoothPropertyUuidRead) {
		this.bluetoothPropertyUuidRead = bluetoothPropertyUuidRead;
	}

	public String getBluetoothPropertyUuidSend() {
		return bluetoothPropertyUuidSend;
	}

	public void setBluetoothPropertyUuidSend(String bluetoothPropertyUuidSend) {
		this.bluetoothPropertyUuidSend = bluetoothPropertyUuidSend;
	}

	@Override
	public byte[] toBytes() {

		byte[] bytes = {};

		// 设置tag内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, commandTag);

		// 设置flag内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, commandFlag);

		// 设置type内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, commandType);

		// 设置data内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, commandData);

		return bytes;
	}

	@Override
	public void fromBytes(byte[] bytes) {

		Integer start = 0;

		commandTag = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				commandTag.length);
		start += commandTag.length;

		commandFlag = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				commandFlag.length);
		start += commandFlag.length;

		commandType = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				commandType.length);
		start += commandType.length;

		commandData = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				commandData.length);
	}

}
