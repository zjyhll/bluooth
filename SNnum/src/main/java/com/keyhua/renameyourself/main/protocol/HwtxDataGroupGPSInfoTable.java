package com.keyhua.renameyourself.main.protocol;

import java.util.ArrayList;

/**
 * //T3:表3 __packed typedef struct _tag_HWTX_GroupGPSInfo_TABLE_ { unsigned char
 * bGrpCountOfLogicNum; //GPS信息表的逻辑号总数，为bGrpCountDevice HWTX_GrpGpsInfo_Item
 * tIndexGpsInfoArray[MAX_GRPCOUNT_DEV_SIZE]; }HWTX_GroupGPSInfo_Table;
 */
public class HwtxDataGroupGPSInfoTable extends HwtxBluetoothSerializable {
    // unsigned short, 2字节
    private byte wRev[] = {0x0, 0x0};
    // unsigned short, 2字节
    private byte wSizeInByte[] = {0x0, 0x0};
    // unsigned char, 1字节
    private byte bDataType[] = {0x0};
    private byte grpCountOfLogicNum[] = {0x0};

    // HWTX_GrpGpsInfo_Item 数组
    private ArrayList<HwtxDataGrpGpsInfoItem> indexGpsInfoArray = new ArrayList<HwtxDataGrpGpsInfoItem>();

    public HwtxDataGroupGPSInfoTable() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public byte[] toBytes() {

        byte[] bytes = {};

        // 注意生成的字节流的顺序必须严格保证，不能随意调换
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, wRev);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, wSizeInByte);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, bDataType);
        bytes = HwtxCommandUtility
                .appendBytesToBytes(bytes, grpCountOfLogicNum);

        for (int i = 0; ((i < indexGpsInfoArray.size()) && (i < HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE)); ++i) {
            bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
                    indexGpsInfoArray.get(i).toBytes());
        }

        return bytes;
    }

    @Override
    public void fromBytes(byte[] bytes) {

        Integer start = 0;

        wRev = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                wRev.length);
        start += wRev.length;
        wSizeInByte = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                wSizeInByte.length);
        start += wSizeInByte.length;
        bDataType = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                bDataType.length);
        start += bDataType.length;
        grpCountOfLogicNum = HwtxCommandUtility.extractBytesFromBytes(bytes,
                start, grpCountOfLogicNum.length);
        start += grpCountOfLogicNum.length;

        Integer remainLengthOfBytes = bytes.length - start;
        Integer arrayItemLength = (new HwtxDataGrpGpsInfoItem()).toBytes().length;
        Integer arrayLength = remainLengthOfBytes / arrayItemLength;
        indexGpsInfoArray.clear();
        for (int i = 0; i < arrayLength; ++i) {
            HwtxDataGrpGpsInfoItem grpGpsInfoItem = new HwtxDataGrpGpsInfoItem();
            byte grpGpsInfoItemBytes[] = HwtxCommandUtility
                    .extractBytesFromBytes(bytes, start, arrayItemLength);
            grpGpsInfoItem.fromBytes(grpGpsInfoItemBytes);
            indexGpsInfoArray.add(grpGpsInfoItem);
            start += arrayItemLength;
        }

    }

    public byte[] getwRev() {
        return wRev;
    }

    public void setwRev(byte[] wRev) {
        this.wRev = wRev;
    }

    public Short getwRevShort() {
        return HwtxCommandUtility.bytesToInt16(wRev);
    }

    public void setwRevShort(Short value) {
        this.wRev = HwtxCommandUtility.int16ToBytes(value);
    }

    public byte[] getwSizeInByte() {
        return wSizeInByte;
    }

    public void setwSizeInByte(byte[] wSizeInByte) {
        this.wSizeInByte = wSizeInByte;
    }

    public Short getwSizeInShort() {
        return HwtxCommandUtility.bytesToInt16(wSizeInByte);
    }

    public void setwSizeInShort(Short value) {
        this.wSizeInByte = HwtxCommandUtility.int16ToBytes(value);
    }

    public byte[] getGrpCountOfLogicNumRaw() {
        return grpCountOfLogicNum;
    }

    public void setGrpCountOfLogicNumRaw(byte grpCountOfLogicNum[]) {
        this.grpCountOfLogicNum = grpCountOfLogicNum;
    }

    public byte getGrpCountOfLogicNumByte() {
        return grpCountOfLogicNum[0];
    }

    public void setGrpCountOfLogicNumByte(Byte value) {
        this.grpCountOfLogicNum[0] = value;
    }

    public ArrayList<HwtxDataGrpGpsInfoItem> getIndexGpsInfoArray() {
        if (indexGpsInfoArray.size() > HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE) {
            return (ArrayList<HwtxDataGrpGpsInfoItem>) indexGpsInfoArray
                    .subList(0, HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE);
        } else {
            return indexGpsInfoArray;
        }
    }

    public void setIndexGpsInfoArray(
            ArrayList<HwtxDataGrpGpsInfoItem> indexGpsInfoArray) {
        if (this.indexGpsInfoArray.size() < HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE) {
            this.indexGpsInfoArray = indexGpsInfoArray;
        } else {
            this.indexGpsInfoArray = (ArrayList<HwtxDataGrpGpsInfoItem>) indexGpsInfoArray
                    .subList(0, HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE);
        }
    }

    public void addIndexGpsInfoArray(HwtxDataGrpGpsInfoItem grpGpsInfoItem) {
        if (this.indexGpsInfoArray.size() < HwtxCommandConstant.MAX_GRPCOUNT_DEV_SIZE) {
            this.indexGpsInfoArray.add(grpGpsInfoItem);
        }
    }

}
