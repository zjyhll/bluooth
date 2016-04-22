package com.keyhua.renameyourself.main.protocol;

public interface HwtxCommandConstant {

    // 户外同行命令的flag
    public static final byte HETX_COMMAND_FLAG[] = {'H', 'W', 'T', 'X'};

    // 户外同行命令的类型定义
    // 未知命令的type，通常是未初始化的命令
    public static final byte HETX_COMMAND_TYPE_UNKNOWN[] = {0x00};
    // type = 0x01。领队设置
    public static final byte HETX_COMMAND_TYPE_LEADER_SETTING[] = {0x01};
    // type = 0x02，查询模式
    public static final byte HETX_COMMAND_TYPE_QUERY_MODE[] = {0x13};
    // type = 0x04，从设备获取GPS数据表
    public static final byte HETX_COMMAND_TYPE_RECEIVE_GPS[] = {0x04};
    // type = 0x05，准备传输GPS数据表给设备
    public static final byte HETX_COMMAND_TYPE_SEND_GPS_PREPARE[] = {0x05};
    // type = 0x09，准备同步设备参数表
    public static final byte HETX_COMMAND_TYPE_RECEIVE_SETTING[] = {0x09};
    // type = 0x0c，准备同步告警/失联表给设备
    public static final byte HETX_COMMAND_TYPE_SEND_WARNING_PREPARE[] = {0x0C};
    // type = 0x0e，从设备获取告警/失联表
    public static final byte HETX_COMMAND_TYPE_RECEIVE_WARNING[] = {0x0E};
    // type = 0x10，将设备号为XXXXXX的同行宝添加入组，
    public static final byte HETX_COMMAND_TYPE_ADD_DEVICE[] = {0x10};
    /**
     * ---------------------------《修改》----------------------------------
     */
    // type =0x23，准备同步队员信息表给设备
    public static final byte HETX_COMMAND_TYPE_SEND_MEMBER_PREPARE[] = {0x23};
    // type = 0x25 ，从设备获取队员信息表
    public static final byte HETX_COMMAND_TYPE_RECEIVE_MEMBER[] = {0x25};
    /**
     * ---------------------------《新增》----------------------------------
     */
    // type = 0x0b，获取设备参数表
    public static final byte HETX_COMMAND_TYPE_RECEIVE_SETTING_GET[] = {0x0b};
    // type = 0x17，获取测试log
    public static final byte HETX_COMMAND_TYPE_LOGO[] = {0x17};
    // type = 0x19，获取MCU FW版本号
    public static final byte HETX_COMMAND_TYPE_MCUFW[] = {0x19};
    // type = 0x1b， 获取BT FW版本号
    public static final byte HETX_COMMAND_TYPE_BTFW[] = {0x1b};
    // type = 0x20 ，写TXB设备号
    public static final byte HETX_COMMAND_TYPE_TXB[] = {0x20};
    // type = 0x26，设备号为XXXXXX的同行宝离队
    public static final byte HETX_COMMAND_TYPE_LIDUI[] = {0x26};
    // type = 0x29，设备号为XXXXXX的同行宝离队
    public static final byte HETX_COMMAND_TYPE_GUIDUI[] = {0x29};

    // 户外同行命令的固定的DATA内容
    // type = 0x01，取消领队模式，即设置为队员模式
    public static final byte HETX_COMMAND_DATA_LEADER_SETTING_DISABLE[] = {
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
    // type = 0x01，设置为领队模式
    public static final byte HETX_COMMAND_DATA_LEADER_SETTING_ENABLE[] = {0x1,
            0x0, 0x0, 0x0, 0x0, 0x0};
    // type = 0x02
    public static final byte HETX_COMMAND_DATA_QUERY_MODE[] = {0x0, 0x0, 0x0,
            0x0, 0x0, 0x0};
    // type = 0x04
    public static final byte HETX_COMMAND_DATA_RECEIVE_GPS[] = {0x0, 0x0, 0x0,
            0x0, 0x0, 0x0};
    // type = 0x08
    public static final byte HETX_COMMAND_DATA_RECEIVE_MEMBER[] = {0x0, 0x0,
            0x0, 0x0, 0x0, 0x0};
    // type = 0x0e
    public static final byte HETX_COMMAND_DATA_RECEIVE_WARNING[] = {0x0, 0x0,
            0x0, 0x0, 0x0, 0x0};

    // 设备SN串号，6位数字
    public static final Integer MAX_DEVSN_SIZE = 6;

    // 队员昵称，10个字节：可存放最多10个字母或5个汉字
    public static final Integer MAX_NICKNAME_SIZE = 10;

    // 小组成员有设备的最大人数，暂定30
    public static final Integer MAX_GRPCOUNT_DEV_SIZE = 30;

    // 小组成员最大人数（含无设备、未参加），暂定30. (MAX_GRPCOUNT_SIZE>=MAX_GRPCOUNT_DEV_SIZE)
    public static final Integer MAX_GRPCOUNT_SIZE = 40;

}
