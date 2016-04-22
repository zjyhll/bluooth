package com.keyhua.renameyourself.main.protocol;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.keyhua.renameyourself.main.le.Utile;

public class HwtxBluetoothTest {

    public HwtxBluetoothTest() {
    }

    private static HwtxDataGroupInfoAllBtApp groupInfoAllBtApp1 = null;

/** */
    /**
     * 读取源文件内容
     *
     * @param filename String 文件路径
     * @return byte[] 文件内容
     * @throws IOException
     */
    public static byte[] readFile(String filename) throws IOException {

        File file = new File(filename);
        if (filename == null || filename.equals("")) {
            throw new NullPointerException("无效的文件路径");
        }
        long len = file.length();
        byte[] bytes = new byte[(int) len];

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        int r = bufferedInputStream.read(bytes);
        if (r != len)
            throw new IOException("读取文件不正确");
        bufferedInputStream.close();

        return bytes;

    }

    /**
     * 算出所有数据大小
     */
    public static Short InformationTableAll() {
//		System.out.println("-------同步队员信息表CRC-------");
        byte[] temp = InformationTable2();
        System.out.println("InformationTableAll:\n"
                + HwtxCommandUtility.bytesToHexText(temp));
        Short crc = HwtxCommandUtility.crc16(HwtxCommandUtility
                        .extractBytesFromBytes(temp, 2, temp.length - 2),
                temp.length - 2);
        System.out.println("CRC值:"
                + crc);

        // 本数据结构，除了wCrcGrpInfoAllBtApp之外所有数据的CRC值。(CRC计算：包括从dwSizeInByte开始，到最后一个成员的HWTX_MemberInfo_BtAPP内容）
        return crc;
    }

    /**
     * 队员详细信息表 应该是该方法运行了多次导致数据变多
     */
    public static byte[] InformationTable() {
        System.out.println("-------同步队员信息表队员表-------");
        // 5循环加入以上多个队员
        groupInfoAllBtApp1 = new HwtxDataGroupInfoAllBtApp();
        // 整个有效表的实际总大小：包括wCrcGrpInfoAllBtApp,dwSizeInByte~到最后一个成员的HWTX_MemberInfo_BtAPP内容。
        // InformationTableAll();
        short tempShort = InformationTableAll();
        groupInfoAllBtApp1.setCrcGrpInfoAllBtAppShort(tempShort);
        // groupInfoAllBtApp1.setCrcGrpInfoAllBtAppShort((short) 0x1234);
        groupInfoAllBtApp1.setSizeInByteShort((short) InformationTableSise());
        // 本结构的数据类型. 应该是: HT_TYPE_GIA
        groupInfoAllBtApp1.setbDataType((byte) 0x63);
        // 时间戳。由领队APP每次修改本表时，写入当前系统时间。每次修改表元素，都需要更新为不同的时间戳。----重要，底层FW根据它来判断是否更新了新元素。
        // 时间戳。由领队APP每次修改本表时，写入当前系统时间。每次修改表元素，都需要更新为不同的时间戳。----重要，底层FW根据它来判断是否更新了新元素。
        time = (int) (305419896);
        groupInfoAllBtApp1.setRtcTimeInteger(time);
        // 小组领队宝同行宝总数。（设备总数：包含有领队宝设备、同行宝设备的所有成员。不含无设备的成员或未参加的成员）
        int grpCountDevice = 2;// 直接从数据库查询当前关联设备号个数
        groupInfoAllBtApp1.setGrpCountDeviceByte(Utile
                .int2OneByte(grpCountDevice));
        // 实际参加的总人数:含有设备的，无设备；即报道人数
        int grpCountAttend = 2;
        groupInfoAllBtApp1.setGrpCountAttendByte(Utile
                .int2OneByte(grpCountAttend));
        // 报名总人数。小组总人数：包括有领队宝设备的、有同行宝设备的、无同行宝但参加了本次活动的队员、报名但未参加的队员。
        int grpCountTotal = 6;
        groupInfoAllBtApp1.setGrpCountTotalByte(Utile
                .int2OneByte(grpCountTotal));
        // for循环遍历[1234]，加入5中
        for (int i = 0; i < 1; i++) {
            // if (signUpUserListGet.get(i).getIs_report() == 1
            // || !TextUtils.isEmpty(signUpUserListGet.get(i)
            // .getStrDeviceSN())) {
            /** 组内编号 */
            int devNumInGrou = 1;
            /** 设备编号 */
            String deviceSnString = "102036";
            /** 昵称，最大10字节字母或5个汉字 */
            String nickNameString = "heng";
            /** 本设备是否领队宝 */
            boolean tDeviceIsLdb = true;
            if (i == 0) {// 第一个为领队
                tDeviceIsLdb = true;
            } else {
                tDeviceIsLdb = false;
            }

            /** 是否有同行宝设备 */
            boolean deviceReady = false;
            /** 否参加了这次活动 */
            boolean isAttend = true;
            /** 是否启用APP图标 */
            boolean isUseAP = true;
            // 1
            HwtxDataMemberDeviceInfo deviceInfo1 = new HwtxDataMemberDeviceInfo();
            // 2
            HwtxDataMemberInfo memberInfo1 = new HwtxDataMemberInfo();
            try {
                if (devNumInGrou != 0) {
                    // 该设备队员的组内编号，范围[1,30]
                    deviceInfo1.setDevNumInGroup(devNumInGrou);
                }
                // 本设备是否领队宝（APP确保一个队伍只有一个领队）,0-TXB同行宝（队员）,1-LDB领队宝(领队)；
                deviceInfo1.setDeviceIsLdb(tDeviceIsLdb);
                // 是否有同行宝设备。由领队APP设定：根据strDeviceSN是否为非零来判断，0-无同行宝；1-有同行宝
                deviceInfo1.setDeviceReady(deviceReady);
                memberInfo1.setDevInfo(deviceInfo1);
                // 唯一的设备SN串号，固定6字节的数字，由设备量产时写入。有非0值，则表示该队员有同行宝；如果为0值，则表示该队员无同行宝硬件。
//				if (!TextUtils.isEmpty(deviceSnString)) {
                memberInfo1.setDeviceSnString(deviceSnString);
//				}

            } catch (HwtxCommandException e) {
                e.printStackTrace();
            }
            // 3
            HwtxDataMemberInfoApp memberInfoApp = new HwtxDataMemberInfoApp();
            // 该编号成员是否参加了这次活动。0-未参加，1-参加了本次活动
            memberInfoApp.setIsAttend(isAttend);
            // 该编号成员是否启用APP图标. 0-未使用队员APP，1-使用了队员APP
            memberInfoApp.setIsUseAP(isUseAP);
            HwtxDataMemberInfoBtApp memberInfoBtApp = new HwtxDataMemberInfoBtApp();
            memberInfoBtApp.gethWTXMemberInfoMap().setMemberInfo(memberInfo1);
            // 昵称，最大10字节字母或5个汉字
            memberInfoBtApp.setNickNameString(nickNameString);
            memberInfoBtApp.setMemberInfoApp(memberInfoApp);
            groupInfoAllBtApp1.addIndexMemberInfoArray(memberInfoBtApp);
            // }
        }
        System.out.println("发送的值:"
                + HwtxCommandUtility.bytesToHexText(groupInfoAllBtApp1
                .toBytes()));
        return groupInfoAllBtApp1.toBytes();
    }

    private static HwtxDataGroupInfoAllBtApp groupInfoAllBtApp2 = null;
    private static int time = 0;

    /**
     * 队员详细信息表
     */
    public static byte[] InformationTable2() {
        // 5循环加入以上多个队员
        groupInfoAllBtApp2 = new HwtxDataGroupInfoAllBtApp();
        // 整个有效表的实际总大小：包括wCrcGrpInfoAllBtApp,dwSizeInByte~到最后一个成员的HWTX_MemberInfo_BtAPP内容。
//		 groupInfoAllBtApp2.setCrcGrpInfoAllBtAppShort((short) 0x1234);
        groupInfoAllBtApp2.setSizeInByteShort((short) InformationTableSise());
        // 本结构的数据类型. 应该是: HT_TYPE_GIA
        groupInfoAllBtApp2.setbDataType((byte) 0x63);
        // 时间戳。由领队APP每次修改本表时，写入当前系统时间。每次修改表元素，都需要更新为不同的时间戳。----重要，底层FW根据它来判断是否更新了新元素。
        time = (int) (305419896);
        groupInfoAllBtApp2.setRtcTimeInteger(time);
        // 小组领队宝同行宝总数。（设备总数：包含有领队宝设备、同行宝设备的所有成员。不含无设备的成员或未参加的成员）
        int grpCountDevice = 2;// 直接从数据库查询当前关联设备号个数
        groupInfoAllBtApp2.setGrpCountDeviceByte(Utile
                .int2OneByte(grpCountDevice));
//		// 实际参加的总人数:含有设备的，无设备；即报道人数
        int grpCountAttend = 2;
        groupInfoAllBtApp2.setGrpCountAttendByte(Utile
                .int2OneByte(grpCountAttend));
//		// 报名总人数。小组总人数：包括有领队宝设备的、有同行宝设备的、无同行宝但参加了本次活动的队员、报名但未参加的队员。
        int grpCountTotal = 6;
        groupInfoAllBtApp2.setGrpCountTotalByte(Utile
                .int2OneByte(grpCountTotal));
        // for循环遍历[1234]，加入5中
        for (int i = 0; i < 1; i++) {
            // if (signUpUserListGet.get(i).getIs_report() == 1
            // || !TextUtils.isEmpty(signUpUserListGet.get(i)
            // .getStrDeviceSN())) {
            /** 组内编号 */
            int devNumInGrou = 1;
            /** 设备编号 */
            String deviceSnString = "102036";
            /** 昵称，最大10字节字母或5个汉字 */
            String nickNameString = "heng";
            /** 本设备是否领队宝 */
            boolean tDeviceIsLdb = false;
            if (i == 0) {// 第一个为领队
                tDeviceIsLdb = true;
            } else {
                tDeviceIsLdb = false;
            }

            /** 是否有同行宝设备 */
            boolean deviceReady = true;
            /** 否参加了这次活动 */
            boolean isAttend = true;
            /** 是否启用APP图标 */
            boolean isUseAP = true;
            // 1
            HwtxDataMemberDeviceInfo deviceInfo1 = new HwtxDataMemberDeviceInfo();
            // 2
            HwtxDataMemberInfo memberInfo1 = new HwtxDataMemberInfo();
            try {
                if (devNumInGrou != 0) {
                    // 该设备队员的组内编号，范围[1,30]
                    deviceInfo1.setDevNumInGroup(devNumInGrou);
                }
                // 本设备是否领队宝（APP确保一个队伍只有一个领队）,0-TXB同行宝（队员）,1-LDB领队宝(领队)；
                deviceInfo1.setDeviceIsLdb(tDeviceIsLdb);
                // 是否有同行宝设备。由领队APP设定：根据strDeviceSN是否为非零来判断，0-无同行宝；1-有同行宝
                deviceInfo1.setDeviceReady(deviceReady);
                memberInfo1.setDevInfo(deviceInfo1);
                // 唯一的设备SN串号，固定6字节的数字，由设备量产时写入。有非0值，则表示该队员有同行宝；如果为0值，则表示该队员无同行宝硬件。
//				if (!TextUtils.isEmpty(deviceSnString)) {
                memberInfo1.setDeviceSnString(deviceSnString);
//				}

            } catch (HwtxCommandException e) {
                e.printStackTrace();
            }
            // 3
            HwtxDataMemberInfoApp memberInfoApp = new HwtxDataMemberInfoApp();
            // 该编号成员是否参加了这次活动。0-未参加，1-参加了本次活动
            memberInfoApp.setIsAttend(isAttend);
            // 该编号成员是否启用APP图标. 0-未使用队员APP，1-使用了队员APP
            memberInfoApp.setIsUseAP(isUseAP);
            HwtxDataMemberInfoBtApp memberInfoBtApp = new HwtxDataMemberInfoBtApp();
            memberInfoBtApp.gethWTXMemberInfoMap().setMemberInfo(memberInfo1);
            // 昵称，最大10字节字母或5个汉字
            memberInfoBtApp.setNickNameString(nickNameString);
            memberInfoBtApp.setMemberInfoApp(memberInfoApp);
            groupInfoAllBtApp2.addIndexMemberInfoArray(memberInfoBtApp);
            // }
        }

        return groupInfoAllBtApp2.toBytes();
    }

    /**
     * @return 返回大小
     */
    public static int InformationTableSise() {
        HwtxDataGroupInfoAllBtApp groupInfoAllBtAppSize = new HwtxDataGroupInfoAllBtApp();
        HwtxDataMemberInfoBtApp memberInfoBtApp = new HwtxDataMemberInfoBtApp();

        return groupInfoAllBtAppSize.toBytes().length
                + memberInfoBtApp.toBytes().length * 1;

    }

    // --------------------------------------------------------------------------------

    /**
     * 算出所有数据大小
     */
    public static void equipmentParameterListAll() {
        System.out.println("-------设置表CRC-------");
        byte[] temp = equipmentParameterList2();
        System.out.println("CRC值:\n"
                + HwtxCommandUtility.crc16(temp, temp.length - 2));
        // 本数据结构，除了wCrcGrpInfoAllBtApp之外所有数据的CRC值。(CRC计算：包括从dwSizeInByte开始，到最后一个成员的HWTX_MemberInfo_BtAPP内容）
        // command.setConfigCRCInteger(HwtxCommandUtility.crc16(temp,
        // temp.length - 2));

    }

    /**
     * @return计算crc值
     */
    private static byte[] equipmentParameterList2() {
        HwtxDataFirmwareConfig command = new HwtxDataFirmwareConfig();
        command.setbDataType(0x62);
        command.setGpsIntervalInteger(0x05);
        command.setHktAtIntervalInteger(0x05);
        command.setLostContactNumInteger(0x05);
        command.setWarningDistance1Integer(0x1414);
        command.setWarningDistance2Integer(0x1515);
        command.setWarningDistance3Integer(0x1616);
        command.setWarningBatteryPercentInteger(0x18);
        return command.toBytes();
    }

    public static void main(String[] args) {
        try {
            byte[] b = readFile("E:\\outdoor\\硬件单机版\\独立APP联调数据\\BtAPP2.bin");
            String s=  HwtxCommandUtility.bytesToHexText(b);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
//		InformationTableAll();
        InformationTable();
//		equipmentParameterListAll();
//
//		// 查询模式
//		System.out.println("-------查询模式-------");
//		HwtxCommandQueryMode hwtxCommandQueryMode = new HwtxCommandQueryMode();
//		// System.out.println(hwtxCommandQueryMode.bytesToHexText());
//		//
//		// System.out.println(hwtxCommandQueryMode.toBytes());
//		// hwtxCommandQueryMode.fromBytes("0xB2 0xCA 0x3D 0x32 0x00".getBytes());
//		System.out.println(hwtxCommandQueryMode.bytesToHexText());
//		System.out.println("-------设置领队-------");
//		HwtxCommandLeaderSetting command1 = new HwtxCommandLeaderSetting();
//		command1.enableLeader();
//		System.out.println(command1.bytesToHexText());
//		System.out.println("-------取消领队-------");
//		command1.disableLeader();
//		System.out.println(command1.bytesToHexText());
//		System.out.println("Tag: "
//				+ String.format("0x%X", command1.getCommandTagInteger()));
//		System.out.println("Flag: " + command1.getCommandFlagString());
//		System.out.println("Type: " + command1.getCommandTypeInteger());
//
//		byte[] bytes = command1.toBytes();
//
//		System.out.println("--------");
//
//		// 测试还原命令内容
//		HwtxCommandLeaderSetting command2 = new HwtxCommandLeaderSetting();
//		command2.fromBytes(bytes);
//		System.out.println(command2.bytesToHexText());
//		System.out.println("Tag: "
//				+ String.format("0x%X", command2.getCommandTagInteger()));
//		System.out.println("Flag: " + command2.getCommandFlagString());
//		System.out.println("Type: " + command2.getCommandTypeInteger());
//		System.out.println("----设备参数表----");
//		HwtxDataFirmwareConfig data1 = new HwtxDataFirmwareConfig();
//
//		data1.setGpsIntervalInteger(0x6);
//		data1.setHktAtIntervalInteger(0x20);
//		data1.setLostContactNumInteger(0x1A);
//		data1.setWarningDistance1Integer(0x11);
//		data1.setWarningDistance2Integer(0x12);
//		data1.setWarningDistance3Integer(0x13);
//		data1.setWarningBatteryPercentInteger(0x36);
//		data1.setConfigCRCInteger((short) 0x87654321);
//		System.out.println(data1.bytesToHexText());
//		System.out.println("----队员信息表----");// TODO
//		// START需要同步的队员信息表------------------------------------------------------
//		HwtxDataMemberDeviceInfo deviceInfo1 = new HwtxDataMemberDeviceInfo();
//		HwtxDataMemberInfo memberInfo1 = new HwtxDataMemberInfo();
//
//		try {
//			deviceInfo1.setDevNumInGroup(31);
//			deviceInfo1.setDeviceIsLdb(true);
//			deviceInfo1.setDeviceReady(true);
//			memberInfo1.setDevInfo(deviceInfo1);
//			memberInfo1.setDeviceSnString("123456");
//			System.out.println("1 DevNumInGroup: "
//					+ memberInfo1.getDevInfo().getDevNumInGroup());
//			System.out.println("1 DeviceIsLDB: "
//					+ (memberInfo1.getDevInfo().getDeviceIsLdb() ? "是" : "否"));
//			System.out.println("1 DeviceReady: "
//					+ (memberInfo1.getDevInfo().getDeviceReady() ? "是" : "否"));
//			System.out
//					.println("1 DeviceSn: " + memberInfo1.getDeviceSnString());
//			System.out.println(Integer.toBinaryString(HwtxCommandUtility
//					.bytesToInt32(memberInfo1.getDevInfo().toBytes())));
//			System.out.println(memberInfo1.bytesToHexText());
//
//			HwtxDataMemberInfo memberInfo2 = new HwtxDataMemberInfo();
//			memberInfo2.fromBytes(memberInfo1.toBytes());
//			System.out.println("2 DevNumInGroup: "
//					+ memberInfo2.getDevInfo().getDevNumInGroup());
//			System.out.println("2 DeviceIsLDB: "
//					+ (memberInfo2.getDevInfo().getDeviceIsLdb() ? "是" : "否"));
//			System.out.println("2 DeviceReady: "
//					+ (memberInfo2.getDevInfo().getDeviceReady() ? "是" : "否"));
//			System.out
//					.println("2 DeviceSn: " + memberInfo2.getDeviceSnString());
//			System.out.println(Integer.toBinaryString(HwtxCommandUtility
//					.bytesToInt32(memberInfo2.getDevInfo().toBytes())));
//			System.out.println(memberInfo2.bytesToHexText());
//		} catch (HwtxCommandException e) {
//			e.printStackTrace();
//		}
//		System.out.println("-------- HwtxDataMemberInfoApp");
//		HwtxDataMemberInfoApp memberInfoApp = new HwtxDataMemberInfoApp();
//		memberInfoApp.setIsAttend(true);
//		memberInfoApp.setIsUseAP(true);
//		System.out.println(memberInfoApp.bytesToHexText());
//
//		System.out.println("-------- HwtxDataMemberInfoBtApp");
//		HwtxDataMemberInfoBtApp memberInfoBtApp = new HwtxDataMemberInfoBtApp();
//		memberInfoBtApp.setMemberInfo(memberInfo1);
//		memberInfoBtApp.setNickNameString("我的新昵称");
//		memberInfoBtApp.setMemberInfoApp(memberInfoApp);
//
//		System.out.println(memberInfoBtApp.bytesToHexText());
//		System.out.println("DeviceSn: " + memberInfoBtApp.getNickNameString());
//		System.out.println("-------- 同步队员信息（1126）  HwtxDataGroupInfoAllBtApp");
//		HwtxDataGroupInfoAllBtApp groupInfoAllBtApp1 = new HwtxDataGroupInfoAllBtApp();
//		groupInfoAllBtApp1.setCrcGrpInfoAllBtAppShort((short) 0x1234);
//		groupInfoAllBtApp1.setSizeInByteShort((short) 0x2234);
//		groupInfoAllBtApp1.setRtcTimeInteger(0x32345678);
//		groupInfoAllBtApp1.setGrpCountDeviceByte((byte) 0x42);
//		groupInfoAllBtApp1.setGrpCountAttendByte((byte) 0x52);
//		groupInfoAllBtApp1.setGrpCountTotalByte((byte) 0x62);
//		groupInfoAllBtApp1.addIndexMemberInfoArray(memberInfoBtApp);
//		System.out.println(groupInfoAllBtApp1.toBytes().length);
//		// END
//		// 需要同步的队员信息表------------------------------------------------------下面这个方法时打印出以上数据
//		HwtxDataGroupInfoAllBtApp groupInfoAllBtApp2 = new HwtxDataGroupInfoAllBtApp();
//		groupInfoAllBtApp2.fromBytes(groupInfoAllBtApp1.toBytes());
//		System.out.printf("CrcGrpInfoAllBtApp: 0x%X\r\n",
//				groupInfoAllBtApp2.getCrcGrpInfoAllBtAppShort());
//		System.out.printf("SizeInByte: 0x%X\r\n",
//				groupInfoAllBtApp2.getSizeInByteShort());
//		System.out.printf("RtcTime: 0x%X\r\n",
//				groupInfoAllBtApp2.getRtcTimeInteger());
//		System.out.printf("GrpCountDevice: 0x%X\r\n",
//				groupInfoAllBtApp2.getGrpCountDeviceByte());
//		System.out.printf("GrpCountAttend: 0x%X\r\n",
//				groupInfoAllBtApp2.getGrpCountAttendByte());
//		System.out.printf("GrpCountTotal: 0x%X\r\n",
//				groupInfoAllBtApp2.getGrpCountTotalByte());
//		for (int i = 0; i < groupInfoAllBtApp2.getIndexMemberInfoArray().size(); ++i) {
//			System.out.println(groupInfoAllBtApp2.getIndexMemberInfoArray()
//					.get(i).bytesToHexText());
//		}
//		// 队员
//		SignUpUser signUpUserGet = null;
//		List<SignUpUser> signUpUserListGet = null;
//		signUpUserGet = new SignUpUser();
//		signUpUserGet = new SignUpUser();
//		signUpUserGet.setIs_report(1);
//		signUpUserGet.setStrDeviceSN("123");
//		signUpUserGet.setU_nickname("1234");
//		signUpUserListGet = new ArrayList<SignUpUser>();
//		signUpUserListGet.add(signUpUserGet);
//		signUpUserGet = new SignUpUser();
//		signUpUserGet.setIs_report(2);
//		signUpUserGet.setU_nickname("1234");
//		signUpUserGet.setStrDeviceSN("123");
//		signUpUserListGet.add(signUpUserGet);
//		// END 需要同步的队员信息表------------------------------------------------------
//		// TODO
//		// ------------------------------测试（以下）-------------------------------------
//		/** 队员详细信息表 */
//		System.out
//				.println("------------------------------测试（以下）-------------------------------------");
//		// 5循环加入以上多个队员
//		groupInfoAllBtApp1 = new HwtxDataGroupInfoAllBtApp();
//		// 整个有效表的实际总大小：包括wCrcGrpInfoAllBtApp,dwSizeInByte~到最后一个成员的HWTX_MemberInfo_BtAPP内容。
//		groupInfoAllBtApp1.setCrcGrpInfoAllBtAppShort((short) -3308);
//		groupInfoAllBtApp1.setSizeInByteShort((short) (0));
//		// 本结构的数据类型. 应该是: HT_TYPE_GIA
//		groupInfoAllBtApp1.setbDataType((byte) 0x63);
//		// 时间戳。由领队APP每次修改本表时，写入当前系统时间。每次修改表元素，都需要更新为不同的时间戳。----重要，底层FW根据它来判断是否更新了新元素。
//		int time = (int) (System.currentTimeMillis());
//		groupInfoAllBtApp1.setRtcTimeInteger(time);
//		// TODO 小组领队宝同行宝总数。（设备总数：包含有领队宝设备、同行宝设备的所有成员。不含无设备的成员或未参加的成员）
//		int grpCountDevice = 0;// 直接从数据库查询当前关联设备号个数
//		groupInfoAllBtApp1.setGrpCountDeviceByte(Utile.int2OneByte(1));
//		// 实际参加的总人数:含有设备的，无设备；即报道人数
//		int grpCountAttend = 0;
//		groupInfoAllBtApp1.setGrpCountAttendByte(Utile.int2OneByte(1));
//		// 报名总人数。小组总人数：包括有领队宝设备的、有同行宝设备的、无同行宝但参加了本次活动的队员、报名但未参加的队员。
//		groupInfoAllBtApp1.setGrpCountTotalByte(Utile.int2OneByte(2));
//		// for循环遍历[1234]，加入5中
//		for (int i = 0; i < signUpUserListGet.size(); i++) {
//			if (signUpUserListGet.get(i).getIs_report() == 1) {
//				/** 组内编号 */
//				int devNumInGrou = i;
//				/** 设备编号 */
//				String deviceSnString = signUpUserListGet.get(i)
//						.getStrDeviceSN();
//				/** 昵称，最大10字节字母或5个汉字 */
//				String nickNameString = signUpUserListGet.get(i)
//						.getU_nickname();
//				/** 本设备是否领队宝 */
//				boolean tDeviceIsLdb = signUpUserListGet.get(i)
//						.getuDeviceIsLDB();
//				/** 是否有同行宝设备 */
//				boolean deviceReady = signUpUserListGet.get(i)
//						.getuDeviceReady();
//				/** 否参加了这次活动 */
//				boolean isAttend = signUpUserListGet.get(i).getuIsAttend();
//				/** 是否启用APP图标 */
//				boolean isUseAP = signUpUserListGet.get(i).getuIsUseAPP();
//				// 1
//				HwtxDataMemberDeviceInfo deviceInfo2 = new HwtxDataMemberDeviceInfo();
//				// 2
//				HwtxDataMemberInfo memberInfo2 = new HwtxDataMemberInfo();
//				try {
//					// if (devNumInGrou != 0 && deviceInfo1 != null
//					// && deviceSnString != null) {
//					// 该设备队员的组内编号，范围[1,30]
//					deviceInfo2.setDevNumInGroup(devNumInGrou);
//					// 本设备是否领队宝（APP确保一个队伍只有一个领队）,0-TXB同行宝（队员）,1-LDB领队宝(领队)；
//					deviceInfo2.setDeviceIsLdb(tDeviceIsLdb);
//					// 是否有同行宝设备。由领队APP设定：根据strDeviceSN是否为非零来判断，0-无同行宝；1-有同行宝
//					deviceInfo2.setDeviceReady(deviceReady);
//					memberInfo2.setDevInfo(deviceInfo2);
//					// 唯一的设备SN串号，固定6字节的数字，由设备量产时写入。有非0值，则表示该队员有同行宝；如果为0值，则表示该队员无同行宝硬件。
//					memberInfo2.setDeviceSnString(deviceSnString);
//					// ----------------------打印------------------------
//					System.out.println("1 DevNumInGroup: "
//							+ memberInfo2.getDevInfo().getDevNumInGroup());
//					System.out.println("1 DeviceIsLDB: "
//							+ (memberInfo2.getDevInfo().getDeviceIsLdb() ? "是"
//									: "否"));
//					System.out.println("1 DeviceReady: "
//							+ (memberInfo2.getDevInfo().getDeviceReady() ? "是"
//									: "否"));
//					System.out.println("1 DeviceSn: "
//							+ memberInfo1.getDeviceSnString());
//					System.out.println(Integer
//							.toBinaryString(HwtxCommandUtility
//									.bytesToInt32(memberInfo2.getDevInfo()
//											.toBytes())));
//					System.out.println(memberInfo2.bytesToHexText());
//
//					// } else {
//					// // showToast("有数据为空");
//					// }
//
//				} catch (HwtxCommandException e) {
//					e.printStackTrace();
//				}
//
//				// 3
//				HwtxDataMemberInfoApp memberInfoApp1 = new HwtxDataMemberInfoApp();
//				// 该编号成员是否参加了这次活动。0-未参加，1-参加了本次活动
//				memberInfoApp1.setIsAttend(isAttend);
//				// 该编号成员是否启用APP图标. 0-未使用队员APP，1-使用了队员APP
//				memberInfoApp1.setIsUseAP(isUseAP);
//				// ----------------------打印------------------------
//				System.out.println(memberInfoApp1.bytesToHexText());
//
//				HwtxDataMemberInfoBtApp memberInfoBtApp1 = new HwtxDataMemberInfoBtApp();
//				memberInfoBtApp1.setMemberInfo(memberInfo2);
//				// 昵称，最大10字节字母或5个汉字
//				memberInfoBtApp1.setNickNameString(nickNameString);
//				memberInfoBtApp1.setMemberInfoApp(memberInfoApp1);
//				// ----------------------打印------------------------
//				System.out.println(memberInfoBtApp1.bytesToHexText());
//				System.out.println("DeviceSn: "
//						+ memberInfoBtApp1.getNickNameString());
//				groupInfoAllBtApp1.addIndexMemberInfoArray(memberInfoBtApp1);
//				// ----------------------打印------------------------
//				System.out.println(groupInfoAllBtApp2.toBytes().length);
//				System.out
//						.println("------------------------------测试（以上）-------------------------------------");
//			}
//		}
//		// ------------------------------测试（以上）-------------------------------------
//		// 添加入组------------------------------------------------------
//		System.out.println("-----将设备号为XXXXXX的同行宝添加入组---");
//		try {
//			HwtxCommandAddDevice addDevice = new HwtxCommandAddDevice();
//			addDevice.setDeviceSn("123456");
//			System.out.println(addDevice.bytesToHexText());
//			System.out.println("DeviceSn: " + addDevice.getDeviceSn());
//		} catch (HwtxCommandException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("-------- HwtxDataMemberInfoMap");
//		HwtxDataMemberInfoMap memberInfoMap = new HwtxDataMemberInfoMap();
//		memberInfoMap.setAtMappingAddrByte((byte) 0x65);
//		memberInfoMap.setMemberInfo(memberInfo1);
//		System.out.println(memberInfoMap.bytesToHexText());
//
//		System.out.println("-------- HwtxDataGroupInfoAll");
//		HwtxDataGroupInfoAll groupInfoAll1 = new HwtxDataGroupInfoAll();
//		groupInfoAll1.setCrcGrpInfoAllBtAppShort((short) 0x1234);
//		groupInfoAll1.setSizeInByteShort((short) 0x2234);
//		groupInfoAll1.setRtcTimeInteger(0x32345678);
//		groupInfoAll1.setGroupIDShort((short) 0x1F34);
//		groupInfoAll1.setGrpCountDeviceByte((byte) 0x42);
//		groupInfoAll1.setGrpCountAttendByte((byte) 0x52);
//		groupInfoAll1.setGrpCountTotalByte((byte) 0x62);
//		groupInfoAll1.setGroupSeedInteger(0x72345678);
//		groupInfoAll1.addIndexMemberInfoMapArray(memberInfoMap);
//		System.out.println(groupInfoAll1.bytesToHexText());
//
//		HwtxDataGroupInfoAll groupInfoAll2 = new HwtxDataGroupInfoAll();
//		groupInfoAll2.fromBytes(groupInfoAll1.toBytes());
//		System.out.printf("CrcGrpInfoAllBtApp: 0x%X\r\n",
//				groupInfoAll2.getCrcGrpInfoAllBtAppShort());
//		System.out.printf("SizeInByte: 0x%X\r\n",
//				groupInfoAll2.getSizeInByteShort());
//		System.out.printf("RtcTime: 0x%X\r\n",
//				groupInfoAll2.getRtcTimeInteger());
//		System.out.printf("GroupID: 0x%X\r\n", groupInfoAll2.getGroupIDShort());
//		System.out.printf("GrpCountDevice: 0x%X\r\n",
//				groupInfoAll2.getGrpCountDeviceByte());
//		System.out.printf("GrpCountAttend: 0x%X\r\n",
//				groupInfoAll2.getGrpCountAttendByte());
//		System.out.printf("GrpCountTotal: 0x%X\r\n",
//				groupInfoAll2.getGrpCountTotalByte());
//		System.out.printf("GroupSeed: 0x%X\r\n",
//				groupInfoAll2.getGroupSeedInteger());
//		for (int i = 0; i < groupInfoAll2.getIndexMemberInfoMapArray().size(); ++i) {
//			System.out.println(groupInfoAll2.getIndexMemberInfoMapArray()
//					.get(i).bytesToHexText());
//		}
//		// gps表相关---------------------------------------------
//		System.out.println("----准备传输GPS数据表----");
//		try {
//			HwtxCommandSendGpsPrepare sendGpsPrepare = new HwtxCommandSendGpsPrepare();
//			sendGpsPrepare.setGpsDataLength(0x1F1A);
//			System.out.println(sendGpsPrepare.bytesToHexText());
//			System.out.printf("GpsDataLength: 0x%X",
//					sendGpsPrepare.getGpsDataLength());
//		} catch (HwtxCommandException e) {
//			e.printStackTrace();
//		}
//		System.out.println("-------- HwtxDataGpsInfoDataComp");
//		HwtxDataGpsInfoDataComp gpsInfoDataComp = new HwtxDataGpsInfoDataComp();
//		gpsInfoDataComp.setGpsMiscByte((byte) 0x12);
//		gpsInfoDataComp.setGpsTimeInteger(0x22345678);
//		gpsInfoDataComp.setGpsLatitudeInteger(0x32345678);
//		gpsInfoDataComp.setGpsLongitudeInteger(0x42345678);
//		System.out.println(gpsInfoDataComp.bytesToHexText());
//
//		System.out.println("-------- HwtxDataGrpGpsInfoItem");
//		HwtxDataGrpGpsInfoItem grpGpsInfoItem = new HwtxDataGrpGpsInfoItem();
//		grpGpsInfoItem.setAtMappingAddrByte((byte) 0x49);
//		grpGpsInfoItem.setDevNumInGroupByte((byte) 0x59);
//		grpGpsInfoItem.setGpsInfoDataComp(gpsInfoDataComp);
//		System.out.println(grpGpsInfoItem.bytesToHexText());
//
//		System.out.println("-------- HwtxDataGroupGPSInfoTable");
//		HwtxDataGroupGPSInfoTable groupGPSInfoTable1 = new HwtxDataGroupGPSInfoTable();
//		groupGPSInfoTable1.setGrpCountOfLogicNumByte((byte) 0x61);
//		groupGPSInfoTable1.addIndexGpsInfoArray(grpGpsInfoItem);
//		System.out.println(groupGPSInfoTable1.bytesToHexText());
//		HwtxDataGroupGPSInfoTable groupGPSInfoTable2 = new HwtxDataGroupGPSInfoTable();
//		groupGPSInfoTable2.fromBytes(groupGPSInfoTable1.toBytes());
//		System.out.printf("GrpCountOfLogicNum: 0x%X\r\n",
//				groupGPSInfoTable2.getGrpCountOfLogicNumByte());
    }

}
