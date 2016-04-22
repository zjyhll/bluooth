package com.keyhua.renameyourself.main.protocol;

/**
 * _packed typedef union _tag_NewFlag_uDevNumInGroup_U_
 * {
 * unsigned char v;
 * __packed struct __tag_newFlag_uDevNumInGroup_T_
 * {
 * unsigned uDevNumInGroup : 5;    	//5-bits,该设备队员的组内编号，范围[1,31]	;
 * unsigned uHasLeaveGroup : 1;			//离队标志：0-正常，在队伍中； 1-离队
 * unsigned uHasNewConfig  : 1;      //T0配置表更新标志：本机的T0参数配置表有更新，供APK判断来读取新表. 0-无新config表；1-有新config表(1.策略：当APK同步一次config表后，本bit被设置；当APK来读取config表后，本bit被清除)
 * unsigned uHasNewBtApp2  : 1;      //T2大表2更新标志：本机的HWTX_GroupInfoAll_BtAPP2大表有更新,供APK判断来读取新表；0-无新BtApp2大表2；1-有新BtApp2大表2 (1.策略：当APK同步一次BtApp2大表2后，本bit被设置；当APK来读取BtApp2大表2后，本bit被清除)
 * }bit_info;
 * }NewFlag_uDevNumInGroup_U;
 */

public class NewFlaguDevNumInGroupU extends HwtxBluetoothSerializable {

    private byte data[] = {0x0};

    public Integer getDevNumInGroup() {
        byte[] bytes = HwtxCommandUtility.extractBitsFromBytes(data, 0, 5);
        return HwtxCommandUtility.bytesToInt32(bytes);
    }

    // 	//离队标志：0-正常，在队伍中； 1-离队
    public Boolean getuHasLeaveGroup() {
        Integer bitValue = HwtxCommandUtility.extractBitFromBytes(data, 5);
        if (bitValue != 0) {
            return true;
        }
        return false;
    }    //T0配置表更新标志：本机的T0参数配置表有更新，供APK判断来读取新表. 0-无新config表；1-有新config表(1.策略：当APK同步一次config表后，本bit被设置；当APK来读取config表后，本bit被清除)

    public Boolean getuHasNewConfig() {
        Integer bitValue = HwtxCommandUtility.extractBitFromBytes(data, 6);
        if (bitValue != 0) {
            return true;
        }
        return false;
    }


    // //T2大表2更新标志：本机的HWTX_GroupInfoAll_BtAPP2大表有更新,供APK判断来读取新表；0-无新BtApp2大表2；1-有新BtApp2大表2 (1.策略：当APK同步一次BtApp2大表2后，本bit被设置；当APK来读取BtApp2大表2后，本bit被清除)
    public Boolean getuHasNewBtApp2() {
        Integer bitValue = HwtxCommandUtility.extractBitFromBytes(data, 7);
        if (bitValue != 0) {
            return true;
        }
        return false;
    }


    public NewFlaguDevNumInGroupU() {
    }

    @Override
    public byte[] toBytes() {
        return data;
    }

    @Override
    public void fromBytes(byte[] bytes) {
        data[0] = bytes[0];
    }

}
