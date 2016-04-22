package com.keyhua.renameyourself.main.protocol;

public class HwtxDataGpsInfoDataComp extends HwtxBluetoothSerializable {

	// 1字节
	private byte gpsMisc[] = { 0x0 };

	// 4字节
	private byte gpsTime[] = { 0x0, 0x0, 0x0, 0x0 };

	// 4字节
	private byte gpsLatitude[] = { 0x0, 0x0, 0x0, 0x0 };

	// 4字节
	private byte gpsLongitude[] = { 0x0, 0x0, 0x0, 0x0 };

	public HwtxDataGpsInfoDataComp() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte[] toBytes() {

		byte[] bytes = {};

		// 注意生成的字节流的顺序必须严格保证，不能随意调换

		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, gpsMisc);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, gpsTime);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, gpsLatitude);
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, gpsLongitude);

		return bytes;
	}

	@Override
	public void fromBytes(byte[] bytes) {

		Integer start = 0;

		gpsMisc = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				gpsMisc.length);
		start += gpsMisc.length;

		gpsTime = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				gpsTime.length);
		start += gpsTime.length;

		gpsLatitude = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				gpsLatitude.length);
		start += gpsLatitude.length;

		gpsLongitude = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				gpsLongitude.length);
	}

	public byte[] getGpsMiscRaw() {
		return gpsMisc;
	}

	public void setGpsMiscRaw(byte gpsMisc[]) {
		this.gpsMisc = gpsMisc;
	}

	public byte getGpsMiscByte() {
		return gpsMisc[0];
	}

	public void setGpsMiscByte(Byte value) {
		this.gpsMisc[0] = value;
	}

	public byte[] getGpsTimeRaw() {
		return gpsTime;
	}

	public void setGpsTimeRaw(byte gpsTime[]) {
		this.gpsTime = gpsTime;
	}

	public Integer getGpsTimeInteger() {
		return HwtxCommandUtility.bytesToInt32(gpsTime);
	}

	public void setGpsTimeInteger(Integer value) {
		this.gpsTime = HwtxCommandUtility.int32ToBytes(value);
	}

	public byte[] getGpsLatitudeRaw() {
		return gpsLatitude;
	}

	public void setGpsLatitudeRaw(byte gpsLatitude[]) {
		this.gpsLatitude = gpsLatitude;
	}

	public Integer getGpsLatitudeInteger() {
		return HwtxCommandUtility.bytesToInt32(gpsLatitude);
	}

	public void setGpsLatitudeInteger(Integer value) {
		this.gpsLatitude = HwtxCommandUtility.int32ToBytes(value);
	}

	public byte[] getGpsLongitudeRaw() {
		return gpsLongitude;
	}

	public void setGpsLongitudeRaw(byte gpsLongitude[]) {
		this.gpsLongitude = gpsLongitude;
	}

	public Integer getGpsLongitudeInteger() {
		return HwtxCommandUtility.bytesToInt32(gpsLongitude);
	}

	public void setGpsLongitudeInteger(Integer value) {
		this.gpsLongitude = HwtxCommandUtility.int32ToBytes(value);
	}
}
