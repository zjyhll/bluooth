package com.keyhua.renameyourself.main.protocol;

import java.io.UnsupportedEncodingException;

/**
 * //户外同行： // 同行队伍每个信息 (蓝牙FW与手机APP之间互享） //
 * 是否有设备、LDB/TXB模式、设备编号、设备SN串号、昵称、是否参加这次活动、是否启动APP图标、 __packed typedef struct
 * _tag_HWTX_MemberInfo_BtAPP_ { HWTX_MemberInfo tMemberInfo; char
 * strNickName[MAX_NICKNAME_SIZE]; //昵称，最大10字节字母或5个汉字 Member_MemberInfoApp_U
 * tMemberInfoAPP; //成员的APP额外信息，1Byte }HWTX_MemberInfo_BtAPP;
 */

public class HWTXMemberInfoMap extends HwtxBluetoothSerializable {
    // unsigned char, 1字节
    private byte atMappingAddr[] = {0x0};

    private HwtxDataMemberInfo memberInfo = new HwtxDataMemberInfo();

    @Override
    public byte[] toBytes() {

        byte[] bytes = {};

        bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
                atMappingAddr);
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
                memberInfo.toBytes());

        return bytes;
    }

    @Override
    public void fromBytes(byte[] bytes) {

        Integer start = 0;

        atMappingAddr = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                atMappingAddr.length);
        start += atMappingAddr.length;

        byte[] memberInfoBytes = HwtxCommandUtility.extractBytesFromBytes(
                bytes, start, memberInfo.toBytes().length);
        memberInfo.fromBytes(memberInfoBytes);
    }

    public HwtxDataMemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(HwtxDataMemberInfo memberInfo) {
        this.memberInfo = memberInfo;
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

}
