package com.keyhua.renameyourself.main.protocol;

/**
 * //TODO：HktAT与系统交换所用的 小组位置信息表。 //逻辑编号（index>=1) 组内编号 GPS数据 __packed typedef
 * struct _tag_HWTX_GPSINFO_ITEM_ { unsigned char ATMappingAddr; //AT编组映射号码
 * unsigned char uDevNumInGroup; //5-bits,该设备队员的组内编号，范围[1,30] ;
 * GPSINFO_DATA_COMP myGpsInfo; //GPS信息（时间、经度、纬度） }HWTX_GrpGpsInfo_Item;
 */
public class HwtxDataGrpGpsInfoItem extends HwtxBluetoothSerializable {

    // unsigned char, 1字节
    private byte atMappingAddr[] = {0x0};

    private NewFlaguDevNumInGroupU newFlaguDevNumInGroupU = new NewFlaguDevNumInGroupU();
    // GPSINFO_DATA_COMP 对象
    private HwtxDataGpsInfoDataComp gpsInfoDataComp = new HwtxDataGpsInfoDataComp();

    public HwtxDataGrpGpsInfoItem() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public byte[] toBytes() {

        byte[] bytes = {};

        // 注意生成的字节流的顺序必须严格保证，不能随意调换

        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, atMappingAddr);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, newFlaguDevNumInGroupU.toBytes());
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
                gpsInfoDataComp.toBytes());

        return bytes;
    }

    @Override
    public void fromBytes(byte[] bytes) {

        Integer start = 0;

        atMappingAddr = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                atMappingAddr.length);
        start += atMappingAddr.length;

        byte[] devInfoBytes = HwtxCommandUtility.extractBytesFromBytes(bytes,
                start, newFlaguDevNumInGroupU.toBytes().length);
        newFlaguDevNumInGroupU.fromBytes(devInfoBytes);
        start += devInfoBytes.length;
        byte[] gpsInfoDataCompBytes = HwtxCommandUtility.extractBytesFromBytes(
                bytes, start, gpsInfoDataComp.toBytes().length);
        gpsInfoDataComp.fromBytes(gpsInfoDataCompBytes);

    }

    public byte[] getAtMappingAddrRaw() {
        return atMappingAddr;
    }

    public void setAtMappingAddrRaw(byte atMappingAddr[]) {
        this.atMappingAddr = atMappingAddr;
    }

    public byte getAtMappingAddrByte() {
        return atMappingAddr[0];
    }

    public void setAtMappingAddrByte(Byte value) {
        this.atMappingAddr[0] = value;
    }

    public NewFlaguDevNumInGroupU getNewFlaguDevNumInGroupU() {
        return newFlaguDevNumInGroupU;
    }

    public void setNewFlaguDevNumInGroupU(NewFlaguDevNumInGroupU newFlaguDevNumInGroupU) {
        this.newFlaguDevNumInGroupU = newFlaguDevNumInGroupU;
    }

    public HwtxDataGpsInfoDataComp getGpsInfoDataComp() {
        return gpsInfoDataComp;
    }

    public void setGpsInfoDataComp(HwtxDataGpsInfoDataComp gpsInfoDataComp) {
        this.gpsInfoDataComp = gpsInfoDataComp;
    }

}
