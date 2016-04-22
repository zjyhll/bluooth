package com.keyhua.renameyourself.main.protocol;

import java.util.ArrayList;

/**
 * //T1:大表 //TODO: 蓝牙FW/APP使用 --蓝牙Flash中维护的大表 __packed typedef struct
 * _tag_HWTX_GroupInfo_BtAPP_ { unsigned short wCrcGrpInfoAllBtApp;
 * //本数据结构，除了wCrcGrpInfoAllBtApp之外所有数据的CRC值
 * 。(CRC计算：包括从dwSizeInByte开始，到最后一个成员的HWTX_MemberInfo_BtAPP内容） unsigned short
 * wSizeInByte; // 整个有效表的实际总大小：包括wCrcGrpInfoAllBtApp,
 * dwSizeInByte~到最后一个成员的HWTX_MemberInfo_BtAPP内容。 unsigned char bDataType;
 * //本结构的数据类型. 应该是: HT_TYPE_BTAPP
 * <p/>
 * unsigned long dwRtcTime;
 * //时间戳。由领队APP每次修改本表时，写入当前系统时间。每次修改表元素，都需要更新为不同的时间戳。----重要，底层FW根据它来判断是否更新了新元素。
 * //TODO: 应该bGrpCountDevice <= bGrpCountAttend <= bGrpCountTotal unsigned char
 * bGrpCountDevice; //小组领队宝同行宝总数。（设备总数：包含有领队宝设备、同行宝设备的所有成员。不含无设备的成员或未参加的成员）
 * unsigned char bGrpCountAttend; //实际参加的总人数:含有设备的，无设备； unsigned char
 * bGrpCountTotal; //报名总人数。小组总人数：包括有领队宝设备的、有同行宝设备的、无同行宝但参加了本次活动的队员、报名但未参加的队员。
 * HWTX_MemberInfo_BtAPP tIndexMemberInfoArray[MAX_GRPCOUNT_SIZE];
 * //暂定这个最大值：以实际配置的为准 }HWTX_GroupInfoAll_BtAPP;
 */
public class HwtxDataGroupInfoAllBtApp extends HwtxBluetoothSerializable {

    // unsigned short, 2字节
    private byte crcGrpInfoAllBtApp[] = {0x0, 0x0};

    // unsigned short, 2字节
    private byte sizeInByte[] = {0x0, 0x0};

    // unsigned char, 1字节
    private byte bDataType[] = {0x0};

    // unsigned long, 4字节
    private byte rtcTime[] = {0x0, 0x0, 0x0, 0x0};

    // unsigned short, 2字节
    private byte wGroupID[] = {0x0, 0x0};

    // unsigned char, 1字节
    private byte grpCountDevice[] = {0x0};

    // unsigned char, 1字节
    private byte grpCountAttend[] = {0x0};

    // unsigned char, 1字节
    private byte grpCountTotal[] = {0x0};

    // 可变长的数组，生成的字节流按数组中实际元素的个数来生成，不需要填充到数组最大长度
    // 元素个数最大不超过 MAX_GRPCOUNT_SIZE，生成字节流时，最多只取前 MAX_GRPCOUNT_SIZE 个数组元素
    private ArrayList<HwtxDataMemberInfoBtApp> indexMemberInfoArray = new ArrayList<HwtxDataMemberInfoBtApp>();

    public HwtxDataGroupInfoAllBtApp() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public byte[] toBytes() {

        byte[] bytes = {};

        // 注意生成的字节流的顺序必须严格保证，不能随意调换
        bytes = HwtxCommandUtility
                .appendBytesToBytes(bytes, crcGrpInfoAllBtApp);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, sizeInByte);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, bDataType);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, rtcTime);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, wGroupID);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, grpCountDevice);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, grpCountAttend);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, grpCountTotal);

        for (int i = 0; ((i < indexMemberInfoArray.size()) && (i < HwtxCommandConstant.MAX_GRPCOUNT_SIZE)); ++i) {
            bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
                    indexMemberInfoArray.get(i).toBytes());
        }

        return bytes;
    }

    @Override
    public void fromBytes(byte[] bytes) {

        Integer start = 0;

        crcGrpInfoAllBtApp = HwtxCommandUtility.extractBytesFromBytes(bytes,
                start, crcGrpInfoAllBtApp.length);
        start += crcGrpInfoAllBtApp.length;

        sizeInByte = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                sizeInByte.length);
        start += sizeInByte.length;

        bDataType = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                bDataType.length);
        start += bDataType.length;

        rtcTime = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                rtcTime.length);
        start += rtcTime.length;
        wGroupID = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                wGroupID.length);
        start += wGroupID.length;

        grpCountDevice = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                grpCountDevice.length);
        start += grpCountDevice.length;

        grpCountAttend = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                grpCountAttend.length);
        start += grpCountAttend.length;

        grpCountTotal = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                grpCountTotal.length);
        start += grpCountTotal.length;

        Integer remainLengthOfBytes = bytes.length - start;
        Integer arrayItemLength = (new HwtxDataMemberInfoBtApp()).toBytes().length;
        Integer arrayLength = remainLengthOfBytes / arrayItemLength;
        indexMemberInfoArray.clear();
        for (int i = 0; i < arrayLength; ++i) {
            HwtxDataMemberInfoBtApp memberInfoBtApp = new HwtxDataMemberInfoBtApp();
            byte memberInfoBtAppBytes[] = HwtxCommandUtility
                    .extractBytesFromBytes(bytes, start, arrayItemLength);
            memberInfoBtApp.fromBytes(memberInfoBtAppBytes);
            indexMemberInfoArray.add(memberInfoBtApp);
            start += arrayItemLength;
        }

    }

    public byte[] getbDataType() {
        return bDataType;
    }

    public void setbDataType(byte bDataType) {// TODO
        this.bDataType[0] = bDataType;
    }

    public byte[] getCrcGrpInfoAllBtAppRaw() {
        return crcGrpInfoAllBtApp;
    }

    public void setCrcGrpInfoAllBtAppRaw(byte crcGrpInfoAllBtApp[]) {
        this.crcGrpInfoAllBtApp = crcGrpInfoAllBtApp;
    }

    public Short getCrcGrpInfoAllBtAppShort() {
        return HwtxCommandUtility.bytesToInt16(crcGrpInfoAllBtApp);
    }

    public void setCrcGrpInfoAllBtAppShort(Short value) {
        this.crcGrpInfoAllBtApp = HwtxCommandUtility.int16ToBytes(value);
    }

    public byte[] getSizeInByteRaw() {
        return sizeInByte;
    }

    public void setSizeInByteRaw(byte sizeInByte[]) {
        this.sizeInByte = sizeInByte;
    }

    public Short getSizeInByteShort() {
        return HwtxCommandUtility.bytesToInt16(sizeInByte);
    }

    public void setSizeInByteShort(Short value) {
//        this.sizeInByte = HwtxCommandUtility.int16ToBytes((short)300002);
        this.sizeInByte = HwtxCommandUtility.int16ToBytes(value);
    }

    public byte[] getRtcTimeRaw() {
        return rtcTime;
    }

    public void setRtcTimeRaw(byte rtcTime[]) {
        this.rtcTime = rtcTime;
    }

    public Integer getRtcTimeInteger() {
        return HwtxCommandUtility.bytesToInt32(rtcTime);
    }

    public void setRtcTimeInteger(Integer value) {
        this.rtcTime = HwtxCommandUtility.int32ToBytes(value);
    }

    public byte[] getwGroupID() {
        return wGroupID;
    }

    public void setwGroupID(byte[] wGroupID) {
        this.wGroupID = wGroupID;
    }
    public Short getwGroupIDShort() {
        return HwtxCommandUtility.bytesToInt16(wGroupID);
    }

    public void setwGroupIDShort(Short value) {
        this.wGroupID = HwtxCommandUtility.int16ToBytes(value);
    }

    public byte[] getGrpCountDeviceRaw() {
        return grpCountDevice;
    }

    public void setGrpCountDeviceRaw(byte grpCountDevice[]) {
        this.grpCountDevice = grpCountDevice;
    }

    public byte getGrpCountDeviceByte() {
        return grpCountDevice[0];
    }

    public void setGrpCountDeviceByte(Byte value) {
        this.grpCountDevice[0] = value;
    }

    public byte[] getGrpCountAttendRaw() {
        return grpCountAttend;
    }

    public void setGrpCountAttendRaw(byte grpCountAttend[]) {
        this.grpCountAttend = grpCountAttend;
    }

    public byte getGrpCountAttendByte() {
        return grpCountAttend[0];
    }

    public void setGrpCountAttendByte(Byte value) {
        this.grpCountAttend[0] = value;
    }

    public byte[] getGrpCountTotalRaw() {
        return grpCountTotal;
    }

    public void setGrpCountTotalRaw(byte grpCountTotal[]) {
        this.grpCountTotal = grpCountTotal;
    }

    public byte getGrpCountTotalByte() {
        return grpCountTotal[0];
    }

    public void setGrpCountTotalByte(Byte value) {
        this.grpCountTotal[0] = value;
    }

    public ArrayList<HwtxDataMemberInfoBtApp> getIndexMemberInfoArray() {
        if (indexMemberInfoArray.size() > HwtxCommandConstant.MAX_GRPCOUNT_SIZE) {
            return (ArrayList<HwtxDataMemberInfoBtApp>) indexMemberInfoArray
                    .subList(0, HwtxCommandConstant.MAX_GRPCOUNT_SIZE);
        } else {
            return indexMemberInfoArray;
        }
    }

    public void setIndexMemberInfoArray(
            ArrayList<HwtxDataMemberInfoBtApp> indexMemberInfoArray) {
        if (this.indexMemberInfoArray.size() < HwtxCommandConstant.MAX_GRPCOUNT_SIZE) {
            this.indexMemberInfoArray = indexMemberInfoArray;
        } else {
            this.indexMemberInfoArray = (ArrayList<HwtxDataMemberInfoBtApp>) indexMemberInfoArray
                    .subList(0, HwtxCommandConstant.MAX_GRPCOUNT_SIZE);
        }
    }

    public void addIndexMemberInfoArray(HwtxDataMemberInfoBtApp memberInfoBtApp) {
        if (this.indexMemberInfoArray.size() < HwtxCommandConstant.MAX_GRPCOUNT_SIZE) {
            this.indexMemberInfoArray.add(memberInfoBtApp);
        }
    }

}
