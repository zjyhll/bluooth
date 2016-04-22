package com.keyhua.renameyourself.main.protocol;

public abstract class HwtxBluetoothSerializable {

	public HwtxBluetoothSerializable() {
	}

	// 将命令内容生成为实际发送的字节流
	public abstract byte[] toBytes();

	// 根据字节流的内容来生成化命令的内容
	public abstract void fromBytes(byte[] bytes);

	public String bytesToHexText() {
		return HwtxCommandUtility.bytesToHexText(toBytes());
	}

}
