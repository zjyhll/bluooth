package com.keyhua.renameyourself.main.protocol;

/**
 * //户外同行(HWTX)：硬件设备的配置，供Firmware使用 //
 * 设备flash中有默认出厂值；也可从手机领队APP中配置下来，保存在flash另一处，优先使用APP配置下来的有效配置 __packed typedef
 * struct _tag_HWTX_FW_CONFIG_ { unsigned char bGpsInterval;
 * //GPS工作间隔，单位分钟，范围[1,60],default:5 unsigned char bHktAtInterval;
 * //HktAt数传每轮间隔，单位分钟,范围[5,60]，default:5 unsigned char bLostContactNum;
 * //失联次数,范围[1-10],default:3 //TODO: wWarningDistanceX,告警距离，单位：米。范围[10, 5000] //
 * 上层APP需确保三个值依次增大: wWarningDistance1 <= wWarningDistance2 <= wWarningDistance3
 * unsigned short wWarningDistance1; //告警距离1,最大告警距离--大于此距离黄色警告 unsigned short
 * wWarningDistance2; //告警距离2,最大告警距离--大于此距离红色警告 unsigned short
 * wWarningDistance3; //告警距离3,最大告警距离--大于此距离失联警告 unsigned char
 * bWarningBatteryPercent; //电量百分比告警：电量少于该百分比，设备报警。 范围[0,100],default:15
 * unsigned long myConfigCRC; //本数据结构，除了myConfigCRC之外所有数据的CRC值。 }HWTX_FW_Config;
 */
public class HwtxDataFirmwareConfig extends HwtxBluetoothSerializable {

	// unsigned char, 1字节
	private byte bDataType[] = { 0x0 };

	// unsigned char, 1字节
	private byte gpsInterval[] = { 0x0 };

	// unsigned char, 1字节
	private byte hktAtInterval[] = { 0x0 };

	// unsigned char, 1字节
	private byte lostContactNum[] = { 0x0 };

	// unsigned short, 2字节
	private byte warningDistance1[] = { 0x0, 0x0 };

	// unsigned short, 2字节
	private byte warningDistance2[] = { 0x0, 0x0 };

	// unsigned short, 2字节
	private byte warningDistance3[] = { 0x0, 0x0 };

	// unsigned char, 1字节
	private byte warningBatteryPercent[] = { 0x0 };

	// unsigned short, 2字节
	private byte configCRC[] = { 0x0, 0x0 };

	public HwtxDataFirmwareConfig() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte[] toBytes() {

		byte[] bytes = {};

		// 注意生成的字节流的顺序必须严格保证，不能随意调换

		// 设置bDataType内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, bDataType);
		// 设置gpsInterval内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, gpsInterval);

		// 设置hktAtInterval内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, hktAtInterval);

		// 设置lostContactNum内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, lostContactNum);

		// 设置warningDistance1内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, warningDistance1);

		// 设置warningDistance2内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, warningDistance2);

		// 设置warningDistance3内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, warningDistance3);

		// 设置warningBatteryPercent内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
				warningBatteryPercent);

		// 设置warningBatteryPercent内容
		bytes = HwtxCommandUtility.appendBytesToBytes(bytes, configCRC);

		return bytes;
	}

	@Override
	public void fromBytes(byte[] bytes) {

		Integer start = 0;

		bDataType = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				bDataType.length);
		start += bDataType.length;

		gpsInterval = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				gpsInterval.length);
		start += gpsInterval.length;

		hktAtInterval = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				hktAtInterval.length);
		start += hktAtInterval.length;

		lostContactNum = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				lostContactNum.length);
		start += lostContactNum.length;

		warningDistance1 = HwtxCommandUtility.extractBytesFromBytes(bytes,
				start, warningDistance1.length);
		start += warningDistance1.length;

		warningDistance2 = HwtxCommandUtility.extractBytesFromBytes(bytes,
				start, warningDistance2.length);
		start += warningDistance2.length;

		warningDistance3 = HwtxCommandUtility.extractBytesFromBytes(bytes,
				start, warningDistance3.length);
		start += warningDistance3.length;

		warningBatteryPercent = HwtxCommandUtility.extractBytesFromBytes(bytes,
				start, warningBatteryPercent.length);
		start += warningBatteryPercent.length;

		configCRC = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
				configCRC.length);
		start += configCRC.length;

	}

	public byte[] getbDataType() {
		return bDataType;
	}

	public void setbDataType(Integer value) {
		this.bDataType = HwtxCommandUtility.int8ToBytes(value.byteValue());
	}

	public byte[] getGpsIntervalRaw() {
		return gpsInterval;
	}

	public void setGpsIntervalRaw(byte gpsInterval[]) {
		this.gpsInterval = gpsInterval;
	}

	public Integer getGpsIntervalInteger() {
		return Integer.valueOf(HwtxCommandUtility.bytesToInt8(gpsInterval));
	}

	public void setGpsIntervalInteger(Integer value) {
		this.gpsInterval = HwtxCommandUtility.int8ToBytes(value.byteValue());
	}

	public byte[] getHktAtIntervalRaw() {
		return hktAtInterval;
	}

	public void setHktAtIntervalRaw(byte hktAtInterval[]) {
		this.hktAtInterval = hktAtInterval;
	}

	public Integer getHktAtIntervalInteger() {
		return Integer.valueOf(HwtxCommandUtility.bytesToInt8(hktAtInterval));
	}

	public void setHktAtIntervalInteger(Integer value) {
		this.hktAtInterval = HwtxCommandUtility.int8ToBytes(value.byteValue());
	}

	public byte[] getLostContactNumRaw() {
		return lostContactNum;
	}

	public void setLostContactNumRaw(byte lostContactNum[]) {
		this.lostContactNum = lostContactNum;
	}

	public Integer getLostContactNumInteger() {
		return Integer.valueOf(HwtxCommandUtility.bytesToInt8(lostContactNum));
	}

	public void setLostContactNumInteger(Integer value) {
		this.lostContactNum = HwtxCommandUtility.int8ToBytes(value.byteValue());
	}

	public byte[] getWarningDistance1Raw() {
		return warningDistance1;
	}

	public void setWarningDistance1Raw(byte warningDistance1[]) {
		this.warningDistance1 = warningDistance1;
	}

	public Integer getWarningDistance1Integer() {
		return Integer.valueOf(HwtxCommandUtility
				.bytesToInt16(warningDistance1));
	}

	public void setWarningDistance1Integer(Integer value) {
		this.warningDistance1 = HwtxCommandUtility.int16ToBytes(value
				.shortValue());
	}

	public byte[] getWarningDistance2Raw() {
		return warningDistance2;
	}

	public void setWarningDistance2Raw(byte warningDistance2[]) {
		this.warningDistance2 = warningDistance2;
	}

	public Integer getWarningDistance2Integer() {
		return Integer.valueOf(HwtxCommandUtility
				.bytesToInt16(warningDistance2));
	}

	public void setWarningDistance2Integer(Integer value) {
		this.warningDistance2 = HwtxCommandUtility.int16ToBytes(value
				.shortValue());
	}

	public byte[] getWarningDistance3Raw() {
		return warningDistance3;
	}

	public void setWarningDistance3Raw(byte warningDistance3[]) {
		this.warningDistance3 = warningDistance3;
	}

	public Integer getWarningDistance3Integer() {
		return Integer.valueOf(HwtxCommandUtility
				.bytesToInt16(warningDistance3));
	}

	public void setWarningDistance3Integer(Integer value) {
		this.warningDistance3 = HwtxCommandUtility.int16ToBytes(value
				.shortValue());
	}

	public byte[] getWarningBatteryPercentRaw() {
		return warningBatteryPercent;
	}

	public void setWarningBatteryPercentRaw(byte warningBatteryPercent[]) {
		this.warningBatteryPercent = warningBatteryPercent;
	}

	public Integer getWarningBatteryPercentInteger() {
		return Integer.valueOf(HwtxCommandUtility
				.bytesToInt8(warningBatteryPercent));
	}

	public void setWarningBatteryPercentInteger(Integer value) {
		this.warningBatteryPercent = HwtxCommandUtility.int8ToBytes(value
				.byteValue());
	}

	public byte[] getConfigCRCRaw() {
		return configCRC;
	}

	public void setConfigCRCRaw(byte configCRC[]) {
		this.configCRC = configCRC;
	}

	public Short getConfigCRCInteger() {
		return Short.valueOf(HwtxCommandUtility.bytesToInt16(configCRC));
	}

	public void setConfigCRCInteger(Short value) {
		this.configCRC = HwtxCommandUtility.int16ToBytes(value.shortValue());
	}
}
