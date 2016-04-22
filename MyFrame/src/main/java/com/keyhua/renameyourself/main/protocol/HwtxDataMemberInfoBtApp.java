package com.keyhua.renameyourself.main.protocol;

import java.io.UnsupportedEncodingException;

/**
 * //户外同行： // 同行队伍每个信息 (蓝牙FW与手机APP之间互享） //
 * 是否有设备、LDB/TXB模式、设备编号、设备SN串号、昵称、是否参加这次活动、是否启动APP图标、 __packed typedef struct
 * _tag_HWTX_MemberInfo_BtAPP_ { HWTX_MemberInfo tMemberInfo; char
 * strNickName[MAX_NICKNAME_SIZE]; //昵称，最大10字节字母或5个汉字 Member_MemberInfoApp_U
 * tMemberInfoAPP; //成员的APP额外信息，1Byte }HWTX_MemberInfo_BtAPP;
 */

public class HwtxDataMemberInfoBtApp extends HwtxBluetoothSerializable {

    private HWTXMemberInfoMap hWTXMemberInfoMap = new HWTXMemberInfoMap();

    private byte nickName[] = new byte[HwtxCommandConstant.MAX_NICKNAME_SIZE];

    private HwtxDataMemberInfoApp memberInfoApp = new HwtxDataMemberInfoApp();

    public HwtxDataMemberInfoBtApp() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public byte[] toBytes() {

        byte[] bytes = {};

        // 设置 tMemberInfo
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
                hWTXMemberInfoMap.toBytes());

        // 设置 strNickName
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes, nickName);

        // 设置tMemberInfoAPP
        bytes = HwtxCommandUtility.appendBytesToBytes(bytes,
                memberInfoApp.toBytes());

        return bytes;
    }

    @Override
    public void fromBytes(byte[] bytes) {

        Integer start = 0;

        byte[] memberInfoBytes = HwtxCommandUtility.extractBytesFromBytes(
                bytes, start, hWTXMemberInfoMap.toBytes().length);
        hWTXMemberInfoMap.fromBytes(memberInfoBytes);
        start += hWTXMemberInfoMap.toBytes().length;

        nickName = HwtxCommandUtility.extractBytesFromBytes(bytes, start,
                nickName.length);
        start += nickName.length;

        byte[] memberInfoAppBytes = HwtxCommandUtility.extractBytesFromBytes(
                bytes, start, memberInfoApp.toBytes().length);
        memberInfoApp.fromBytes(memberInfoAppBytes);

    }

    public HWTXMemberInfoMap gethWTXMemberInfoMap() {
        return hWTXMemberInfoMap;
    }

    public void sethWTXMemberInfoMap(HWTXMemberInfoMap hWTXMemberInfoMap) {
        this.hWTXMemberInfoMap = hWTXMemberInfoMap;
    }

    public byte[] getNickNameRaw() {
        return nickName;
    }

    public void setNickNameRaw(byte nickName[]) {
        this.nickName = nickName;
    }

    // 内部保存的是2字节一个字符的GB2312的字节流，需要转换为UTF-8供应用层使用
    public String getNickNameString() {
        String utf8String = "  ";
        try {
            utf8String = new String(
                    new String(nickName, "GB2312").getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return utf8String;
    }

    // 内部保存的是2字节一个字符的GB2312的字节流，需要转换为UTF-8保存到字节数组
    public void setNickNameString(String value) {
        String gb2312String = null;
        try {
            gb2312String = new String(value.getBytes("UTF-8"));
            this.nickName = HwtxCommandUtility.copyBytes(
                    gb2312String.getBytes("GB2312"), this.nickName.length);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public HwtxDataMemberInfoApp getMemberInfoApp() {
        return memberInfoApp;
    }

    public void setMemberInfoApp(HwtxDataMemberInfoApp memberInfoApp) {
        this.memberInfoApp = memberInfoApp;
    }

}
