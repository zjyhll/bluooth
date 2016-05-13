package com.keyhua.renameyourself.main.le;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.importotherlib.R;
import com.keyhua.litepal.LitepalUtil;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.main.eventBusBean.ActivitySetBean;
import com.keyhua.renameyourself.main.eventBusBean.AddInBean;
import com.keyhua.renameyourself.main.eventBusBean.ConnectBean;
import com.keyhua.renameyourself.main.eventBusBean.GetMemberInfoBean;
import com.keyhua.renameyourself.main.eventBusBean.GpsBean;
import com.keyhua.renameyourself.main.eventBusBean.GuiDuiBean;
import com.keyhua.renameyourself.main.eventBusBean.LengthZero;
import com.keyhua.renameyourself.main.eventBusBean.LidDUIBean;
import com.keyhua.renameyourself.main.eventBusBean.MemberInfoBean;
import com.keyhua.renameyourself.main.eventBusBean.QueryModeBean;
import com.keyhua.renameyourself.main.eventBusBean.SetBean;
import com.keyhua.renameyourself.main.eventBusBean.TestGps;
import com.keyhua.renameyourself.main.protocol.HwtxCommandAddDevice;
import com.keyhua.renameyourself.main.protocol.HwtxCommandException;
import com.keyhua.renameyourself.main.protocol.HwtxCommandGUIDUI;
import com.keyhua.renameyourself.main.protocol.HwtxCommandLIDUI;
import com.keyhua.renameyourself.main.protocol.HwtxCommandLeaderSetting;
import com.keyhua.renameyourself.main.protocol.HwtxCommandQueryMode;
import com.keyhua.renameyourself.main.protocol.HwtxCommandReceiveGps;
import com.keyhua.renameyourself.main.protocol.HwtxCommandReceiveMember;
import com.keyhua.renameyourself.main.protocol.HwtxCommandReceiveSetingGet;
import com.keyhua.renameyourself.main.protocol.HwtxCommandReceiveSetting;
import com.keyhua.renameyourself.main.protocol.HwtxCommandSendMemberPrepare;
import com.keyhua.renameyourself.main.protocol.HwtxCommandUtility;
import com.keyhua.renameyourself.main.protocol.HwtxDataFirmwareConfig;
import com.keyhua.renameyourself.main.protocol.HwtxDataGpsInfoDataComp;
import com.keyhua.renameyourself.main.protocol.HwtxDataGroupGPSInfoTable;
import com.keyhua.renameyourself.main.protocol.HwtxDataGroupInfoAllBtApp;
import com.keyhua.renameyourself.main.protocol.HwtxDataGrpGpsInfoItem;
import com.keyhua.renameyourself.main.protocol.HwtxDataMemberDeviceInfo;
import com.keyhua.renameyourself.main.protocol.HwtxDataMemberInfo;
import com.keyhua.renameyourself.main.protocol.HwtxDataMemberInfoApp;
import com.keyhua.renameyourself.main.protocol.HwtxDataMemberInfoBtApp;
import com.keyhua.renameyourself.util.CommonUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleCommon {
    // 北斗设备
    public static final String UUID_KEY_DATA_WRITE_BEIDOU = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_KEY_DATA_READ_BEIDOU = "0000ffe1-0000-1000-8000-00805f9b34fb";
    // 同行卫士，两值相同
    // public static final String UUID_KEY_DATA_WRITE_TONGXING =
    // "91cc0002-a393-3087-7d75-da3752217337";
    // public static final String UUID_KEY_DATA_READ_TONGXING =
    // "91cc0003-a393-3087-7d75-da3752217337";
    // 服务
    public BluetoothLeService mBluetoothLeService;
    // 读写特性
    private BluetoothGattCharacteristic mNotifyCharacteristicWrite;
    private BluetoothGattCharacteristic mNotifyCharacteristicRead;
    // 同行卫士或北斗设备的UUID，根据具体的动作动态更改
    private String UUID_KEY_DATA_WRITE = null;
    private String UUID_KEY_DATA_READ = null;
    private String mDeviceAddress = null;
    public boolean mConnected = false;
    //判断是同步队员信息表还是同步设备参数表
    private boolean setB00lean = false;
//    private boolean isSendContinue = false;//是否继续发送（或接收），当手机端判断连接已断开时，取消数据发送

    /**
     * @param mDeviceAddress 蓝牙设备地址
     */
    public void setCharacteristic(String mDeviceAddress) {
        this.mDeviceAddress = mDeviceAddress;
//        this.isSendContinue = true;
    }

    /**
     * @param isSendContinue 点返回键时取消发送
     */
    public void setCharacteristic(boolean isSendContinue) {
//        this.isSendContinue = isSendContinue;
    }

    /**
     * @param mDeviceAddress      蓝牙设备地址
     * @param uUID_KEY_DATA_WRITE 从蓝牙设备读取返回数据
     * @param uUID_KEY_DATA_READ  从app往蓝牙设备写数据
     */
    public void setCharacteristic(String mDeviceAddress,
                                  String uUID_KEY_DATA_WRITE, String uUID_KEY_DATA_READ) {
        this.mDeviceAddress = mDeviceAddress;
        UUID_KEY_DATA_WRITE = uUID_KEY_DATA_WRITE;
        UUID_KEY_DATA_READ = uUID_KEY_DATA_READ;
//        this.isSendContinue = true;
        if (mBluetoothLeService != null) {
            displayGattServices(mBluetoothLeService.getSupportedGattServices());
        }
    }

    /**
     * @param uUID_KEY_DATA_WRITE 蓝牙设备地址
     */
    public void setCharacteristic(String uUID_KEY_DATA_WRITE,
                                  String uUID_KEY_DATA_READ) {
        System.out.println("21----------------------------"
                + mBluetoothLeService);


        UUID_KEY_DATA_WRITE = uUID_KEY_DATA_WRITE;
        UUID_KEY_DATA_READ = uUID_KEY_DATA_READ;
        if (mBluetoothLeService != null) {
//            isSendContinue = true;
            System.out.println("211----------------------------");
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    //
                    // queryMode();
                    displayGattServices(mBluetoothLeService.getSupportedGattServices());
                }
            }, 3000);// 无论什么情况都延迟3秒

        } else {
//            isSendContinue = true;
            //未连接，重连
            mConnected = false;
            EventBus.getDefault().post(
                    new ConnectBean(mConnected));
        }
    }


    private static BleCommon mInstance = null;

    public static BleCommon getInstance() {

        if (mInstance == null) {
            mInstance = new BleCommon();
        }
        return mInstance;
    }

    public final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
                    .getService();
            if (mBluetoothLeService == null) {
                return;
            }
            if (!mBluetoothLeService.initialize()) {
                // Log.e(TAG, "Unable to initialize Bluetooth");
                // finish();
            }
            if (!TextUtils.isEmpty(mDeviceAddress)) {
                mBluetoothLeService.connect(mDeviceAddress);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (isSendContinue) {
            Message message = Message.obtain();
            message.obj = intent;
            handler.sendMessageDelayed(message, 3000);// 经过多次测试，需要在广播接收器中做个延时操作就能保证数据返回成功
            // 2015/12/10/0：50
//            }
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = (Intent) msg.obj;
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {// 连接成功，步骤1连接2获取读写特性，所以这步不能做发送数据操作

                /**
                 * 3) 查询模式：向UUID为0x0011特性写数据0xYYYYYYYY 48575458 02
                 * 000000000000发送查询模式命令
                 */
                mConnected = true;
                EventBus.getDefault().post(
                        new ConnectBean(mConnected));


            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED// 未关联成功
                    .equals(action)) {
                mConnected = false;
                EventBus.getDefault().post(
                        new ConnectBean(mConnected));
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {// 返回的值
                displayData(intent
                        .getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
            } else if (BluetoothLeService.ACTION_GATT_CONNECTED_AGIAN
                    .equals(action)) {// 已连接，试图重新连接
                /**
                 * 3) 查询模式：向UUID为0x0011特性写数据0xYYYYYYYY 48575458 02
                 * 000000000000发送查询模式命令
                 */
                mConnected = true;
                EventBus.getDefault().post(
                        new ConnectBean(mConnected));
                // queryMode();
            } else if (BluetoothLeService.SENDOVER.equals(action)) {// 每一个请求对应一个参数，来区分完成的是哪个
                // 不需要再继续返回这里
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // 不需要再继续返回这里
                        Utile.needContinue = false;
//                        isSendContinue = true;
                        //需要判断当前进行的是哪个操作
                        if (setB00lean) {
                            sendData(commandSynchronizationInformationTable
                                            .getBluetoothPropertyUuidSend(),
                                    commandSynchronizationInformationTable
                                            .getBluetoothPropertyUuidRead(),
                                    InformationTable());
                        } else {
                            sendData(commandHwtxCommandSendGpsPrepare
                                            .getBluetoothPropertyUuidSend(),
                                    commandHwtxCommandSendGpsPrepare
                                            .getBluetoothPropertyUuidRead(),
                                    equipmentParameterList());
                        }
                    }
                }, 500);
            }
        }
    };


    /**
     * @param gattServices 找到收与发数据的特性
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        // -----Service的字段信息-----//
        for (BluetoothGattService gattService : gattServices) {
            // -----Characteristics的字段信息-----//
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();
            // 获取接收数据的特性
            for (final BluetoothGattCharacteristic gattCharacteristic2 : gattCharacteristics) {
                if (gattCharacteristic2.getUuid().toString()
                        .equals(UUID_KEY_DATA_READ)) {
                    mNotifyCharacteristicRead = gattCharacteristic2;
                }
            }
            // 获取写入蓝牙设备的特性
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_KEY_DATA_WRITE)) {
                    mNotifyCharacteristicWrite = gattCharacteristic;
                }
            }
        }
    }

    /**
     * @param strWrite 写的特性
     * @param strRead  读的特性
     * @param strSend  发送的字符串
     */
    private void sendData(String strWrite, String strRead, final byte[] strSend) {
        // 对应很多不同的读写特性
        if (tempByte != null) {
            tempByte.clear();
            tempByte = null;
        }
        tempByte = new ArrayList<>();
        setCharacteristic(strWrite, strRead);// TODO
        new Handler().postDelayed(new Runnable() {
            public void run() {
                System.out.println("2----------------------------");
//                if (isSendContinue) {
                bleSendDataTongXingBao(strSend);
//                    isSendContinue = false;
//                }
            }
        }, 5000);// 需要延时操作

    }

    /**
     * 发送大于20字节或小于20字节数据方法
     */
    public void bleSendDataTongXingBao(final byte[] writeByte) {
        if (mBluetoothLeService != null) {
            /** 返回数据的特性监听，必须放在往设备写数据之前 */
            if (mNotifyCharacteristicRead != null) {
                mBluetoothLeService.setCharacteristicNotification(
                        mNotifyCharacteristicRead, true);
                System.out.println("4----------------------------");
            }

            /** 发送数据命令 */
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    if (mBluetoothLeService != null) {
                        System.out.println("5----------------------------");
                        if (mNotifyCharacteristicWrite != null) {
                            mBluetoothLeService.write(
                                    mNotifyCharacteristicWrite, writeByte);
                        }

                    }
                }
            }, 500);// 需要延时操作
        }
    }

    public IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        // 保留
        intentFilter
                .addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED_AGIAN);
        intentFilter.addAction(BluetoothLeService.SENDOVER);
        return intentFilter;
    }

    /**
     * 查询模式命令--------------------------------------《领队工具》----------------------------------------------------------------
     */
    private HwtxCommandQueryMode commandqueryMode = null;
    private byte[] tagQueryMode = null;

    public void queryMode() {
        if (mConnected) {
            System.out
                    .println("查询模式:\n");
            if (commandqueryMode == null) {
                commandqueryMode = new HwtxCommandQueryMode();
            }
            if (tagQueryMode == null) {
                tagQueryMode = commandqueryMode.getCommandTagRaw();
            }

            sendData(commandqueryMode.getBluetoothPropertyUuidSend(),
                    commandqueryMode.getBluetoothPropertyUuidRead(),
                    commandqueryMode.toBytes());
            System.out.println("1----------------------------");
        } else {
            EventBus.getDefault().post(
                    new ConnectBean(mConnected));
        }
        // top_tv_right_wode.setText(tagQueryMode+"");
    }

    /**
     * 设置领队
     */
    private HwtxCommandLeaderSetting commandsetTeamLeader = null;
    private byte[] tagSetTeamLeader = null;

    public void setTeamLeader() {

        System.out
                .println("设置领队:\n");
        commandsetTeamLeader = new HwtxCommandLeaderSetting();
        tagSetTeamLeader = commandsetTeamLeader.getCommandTagRaw();
        commandsetTeamLeader.enableLeader();
        sendData(commandsetTeamLeader.getBluetoothPropertyUuidSend(),
                commandsetTeamLeader.getBluetoothPropertyUuidRead(),
                commandsetTeamLeader.toBytes());
    }

    /**
     * 取消领队
     */
    private HwtxCommandLeaderSetting commandCancelTeamLeader = null;
    private byte[] tagCancelTeamLeader = null;

    public void cancelTeamLeader() {
        commandCancelTeamLeader = new HwtxCommandLeaderSetting();
        tagCancelTeamLeader = commandCancelTeamLeader.getCommandTagRaw();
        commandCancelTeamLeader.disableLeader();
        sendData(commandCancelTeamLeader.getBluetoothPropertyUuidSend(),
                commandCancelTeamLeader.getBluetoothPropertyUuidRead(),
                commandCancelTeamLeader.toBytes());
    }

    /**
     * 准备同步队员信息表--------------------------------------《修改队员信息》----------------------------------------------------------------
     */
    HwtxCommandSendMemberPrepare commandSynchronizationInformationTable = null;
    private byte[] tagSynchronizationInformationTable = null;

    // private byte[] informationTableByte = null;

    public void synchronizationInformationTable() {
        System.out
                .println("准备同步队员信息表:\n");
        setB00lean = true;
        if (commandSynchronizationInformationTable == null) {
            commandSynchronizationInformationTable = new HwtxCommandSendMemberPrepare();
        }
        if (tagSynchronizationInformationTable == null) {
            tagSynchronizationInformationTable = commandSynchronizationInformationTable
                    .getCommandTagRaw();
        }

        try {
            commandSynchronizationInformationTable
                    .setMemberDataLength(InformationTableSise());
        } catch (HwtxCommandException e) {
            //
            e.printStackTrace();
        }
        // 正在同步数据
        Utile.needContinue = true;
        sendData(
                commandSynchronizationInformationTable
                        .getBluetoothPropertyUuidSend(),
                commandSynchronizationInformationTable
                        .getBluetoothPropertyUuidRead(),
                commandSynchronizationInformationTable.toBytes());
    }

    // 5循环加入以上多个队员
    private HwtxDataGroupInfoAllBtApp groupInfoAllBtApp1 = null;

    /**
     * 算出所有数据大小
     */
    public Short InformationTableAll() {
        System.out
                .println("InformationTableAll:\n");
        byte[] temp = InformationTable2();
        // 本数据结构，除了wCrcGrpInfoAllBtApp之外所有数据的CRC值。(CRC计算：包括从dwSizeInByte开始，到最后一个成员的HWTX_MemberInfo_BtAPP内容）
        return HwtxCommandUtility.crc16(HwtxCommandUtility
                        .extractBytesFromBytes(temp, 2, temp.length - 2),
                temp.length - 2);
    }

    /**
     * 队员详细信息表 应该是该方法运行了多次导致数据变多
     */
    public byte[] InformationTable() {
        System.out
                .println("同步队员详细信息表:\n");
        List<SignUpUser> signUpUserListGet = LitepalUtil.getAllUser();
        SignUpUser leader = LitepalUtil.getLeader();
        // 5循环加入以上多个队员
        groupInfoAllBtApp1 = new HwtxDataGroupInfoAllBtApp();
        // 整个有效表的实际总大小：包括wCrcGrpInfoAllBtApp,dwSizeInByte~到最后一个成员的HWTX_MemberInfo_BtAPP内容。
        // InformationTableAll();
        short tempShort = InformationTableAll();
        groupInfoAllBtApp1.setCrcGrpInfoAllBtAppShort(tempShort);
        // groupInfoAllBtApp1.setCrcGrpInfoAllBtAppShort((short) 0x1234);
        int InformationTableSiseInt = InformationTableSise();
        groupInfoAllBtApp1.setSizeInByteShort((short) InformationTableSiseInt);
        // 本结构的数据类型. 应该是: HT_TYPE_GIA
        groupInfoAllBtApp1.setbDataType((byte) 0x6C);
        // 时间戳。由领队APP每次修改本表时，写入当前系统时间。每次修改表元素，都需要更新为不同的时间戳。----重要，底层FW根据它来判断是否更新了新元素。
        // int time = (int) (System.currentTimeMillis());
        groupInfoAllBtApp1.setRtcTimeInteger(time);
        //组ID，由APP生成唯一值。
        groupInfoAllBtApp1.setwGroupIDShort(GroupIDShort);
        // 小组领队宝同行宝总数。（设备总数：包含有领队宝设备、同行宝设备的所有成员。不含无设备的成员或未参加的成员）
        int grpCountDevice = 0;// 直接从数据库查询当前关联设备号个数
        for (int i = 0; i < signUpUserListGet.size(); i++) {
            if (!TextUtils.isEmpty(signUpUserListGet.get(i).getStrDeviceSN())) {
                grpCountDevice++;
            }
        }
        groupInfoAllBtApp1.setGrpCountDeviceByte(Utile
                .int2OneByte(grpCountDevice));
        // 实际参加的总人数:含有设备的，无设备；即报道人数
        int grpCountAttend = 0;
        for (int i = 0; i < signUpUserListGet.size(); i++) {
//            if (signUpUserListGet.get(i).getIs_report() == 1
//                    || !TextUtils.isEmpty(signUpUserListGet.get(i)
//                    .getStrDeviceSN())) {
            grpCountAttend++;
//            }
        }
        groupInfoAllBtApp1.setGrpCountAttendByte(Utile
                .int2OneByte(grpCountAttend));
        // 报名总人数。小组总人数：包括有领队宝设备的、有同行宝设备的、无同行宝但参加了本次活动的队员、报名但未参加的队员。
        int grpCountTotal = signUpUserListGet.size();
        groupInfoAllBtApp1.setGrpCountTotalByte(Utile
                .int2OneByte(grpCountTotal));
        // for循环遍历[1234]，加入5中
        for (int i = 0; i < signUpUserListGet.size(); i++) {//
            // if (signUpUserListGet.get(i).getIs_report() == 1
            // || !TextUtils.isEmpty(signUpUserListGet.get(i)
            // .getStrDeviceSN())) {
            /** 组内编号 */
            int devNumInGrou = signUpUserListGet.get(i).getTps_id();
            /** 设备编号 */
            String deviceSnString = signUpUserListGet.get(i).getStrDeviceSN();
            /** 昵称，最大10字节字母或5个汉字 */
            String nickNameString = signUpUserListGet.get(i).getU_nickname();
            /** 本设备是否领队宝 */
            boolean tDeviceIsLdb = false;
            if (i == 0) {// 第一个为领队
                tDeviceIsLdb = true;
            } else {
                tDeviceIsLdb = false;
            }

            /** 是否有同行宝设备 */
            boolean deviceReady = false;
            if (!TextUtils.isEmpty(signUpUserListGet.get(i).getStrDeviceSN())) {// SN不为空说明有设备
                deviceReady = true;
            } else {
                deviceReady = false;
            }
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
                if (!TextUtils.isEmpty(deviceSnString)) {
                    memberInfo1.setDeviceSnString(deviceSnString);
                }

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
        System.out.println("groupInfoAllBtApp1:\n"
                + HwtxCommandUtility.bytesToHexText(groupInfoAllBtApp1
                .toBytes()));
        return groupInfoAllBtApp1.toBytes();
    }

    /**
     * 生成groupid
     */
    private short getGroupIDShort(String endNum) {
        //第一位
//        short max = 2;
//        short min = -2;
//        Random random = new Random();
//        String firstStr = String.valueOf(random.nextInt(max) % (max - min + 1) + min);
//        //第2、3位
//        Calendar calendar = Calendar.getInstance();
//        String secondStr = String.valueOf(calendar.get(Calendar.SECOND));
        //第4、5位
        String endStr = endNum.substring(endNum.length() - 5);

        return Short.valueOf(endStr);
    }

    private HwtxDataGroupInfoAllBtApp groupInfoAllBtApp2 = null;
    private int time = 0;
    private short GroupIDShort = 0;

    /**
     * 队员详细信息表
     */
    public byte[] InformationTable2() {
        System.out
                .println("InformationTable2:\n");
        List<SignUpUser> signUpUserListGet = LitepalUtil.getAllUser();
        SignUpUser leader = LitepalUtil.getLeader();
        // 5循环加入以上多个队员
        groupInfoAllBtApp2 = new HwtxDataGroupInfoAllBtApp();
        // 整个有效表的实际总大小：包括wCrcGrpInfoAllBtApp,dwSizeInByte~到最后一个成员的HWTX_MemberInfo_BtAPP内容。
        // groupInfoAllBtApp2.setCrcGrpInfoAllBtAppShort((short) 0x1234);
        int InformationTableSiseInt = InformationTableSise();
        groupInfoAllBtApp2.setSizeInByteShort((short) InformationTableSiseInt);
        // 本结构的数据类型. 应该是: HT_TYPE_GIA
        groupInfoAllBtApp2.setbDataType((byte) 0x6C);
        // 时间戳。由领队APP每次修改本表时，写入当前系统时间。每次修改表元素，都需要更新为不同的时间戳。----重要，底层FW根据它来判断是否更新了新元素。
        time = (int) (System.currentTimeMillis());
        groupInfoAllBtApp2.setRtcTimeInteger(time);
        String sn = leader.getStrDeviceSN();
        GroupIDShort = getGroupIDShort(sn);
        //组ID，由APP生成唯一值。
        groupInfoAllBtApp2.setwGroupIDShort(GroupIDShort);

        // 小组领队宝同行宝总数。（设备总数：包含有领队宝设备、同行宝设备的所有成员。不含无设备的成员或未参加的成员）
        int grpCountDevice = 0;// 直接从数据库查询当前关联设备号个数
        for (int i = 0; i < signUpUserListGet.size(); i++) {
            if (!TextUtils.isEmpty(signUpUserListGet.get(i).getStrDeviceSN())) {
                grpCountDevice++;
            }
        }
        groupInfoAllBtApp2.setGrpCountDeviceByte(Utile
                .int2OneByte(grpCountDevice));
        // 实际参加的总人数:含有设备的，无设备；即报道人数
        int grpCountAttend = 0;
        for (int i = 0; i < signUpUserListGet.size(); i++) {
//            if (signUpUserListGet.get(i).getIs_report() == 1
//                    || !TextUtils.isEmpty(signUpUserListGet.get(i)
//                    .getStrDeviceSN())) {
            grpCountAttend++;
//            }
        }
        groupInfoAllBtApp2.setGrpCountAttendByte(Utile
                .int2OneByte(grpCountAttend));
        // 报名总人数。小组总人数：包括有领队宝设备的、有同行宝设备的、无同行宝但参加了本次活动的队员、报名但未参加的队员。
        int grpCountTotal = signUpUserListGet.size();
        groupInfoAllBtApp2.setGrpCountTotalByte(Utile
                .int2OneByte(grpCountTotal));
        // for循环遍历[1234]，加入5中
        for (int i = 0; i < signUpUserListGet.size(); i++) {
            // if (signUpUserListGet.get(i).getIs_report() == 1
            // || !TextUtils.isEmpty(signUpUserListGet.get(i)
            // .getStrDeviceSN())) {
            /** 组内编号 */
            int devNumInGrou = signUpUserListGet.get(i).getTps_id();
            /** 设备编号 */
            String deviceSnString = signUpUserListGet.get(i).getStrDeviceSN();
            /** 昵称，最大10字节字母或5个汉字 */
            String nickNameString = signUpUserListGet.get(i).getU_nickname();
            /** 本设备是否领队宝 */
            boolean tDeviceIsLdb = false;
            if (i == 0) {// 第一个为领队
                tDeviceIsLdb = true;
            } else {
                tDeviceIsLdb = false;
            }

            /** 是否有同行宝设备 */
            boolean deviceReady = false;
            if (!TextUtils.isEmpty(signUpUserListGet.get(i).getStrDeviceSN())) {// SN不为空说明有设备
                deviceReady = true;
            } else {
                deviceReady = false;
            }
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
                if (!TextUtils.isEmpty(deviceSnString)) {
                    memberInfo1.setDeviceSnString(deviceSnString);
                }

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
    public int InformationTableSise() {
        System.out
                .println("InformationTableSise:\n");
        List<SignUpUser> signUpUserListGet = LitepalUtil.getAllUser();
        HwtxDataGroupInfoAllBtApp groupInfoAllBtAppSize = new HwtxDataGroupInfoAllBtApp();
        int tempNum = 0;
        for (int i = 0; i < signUpUserListGet.size(); i++) {
            // if (signUpUserListGet.get(i).getIs_report() == 1
            // || !TextUtils.isEmpty(signUpUserListGet.get(i)
            // .getStrDeviceSN())) {
            tempNum++;
            // }
        }
        HwtxDataMemberInfoBtApp memberInfoBtApp = new HwtxDataMemberInfoBtApp();

        return groupInfoAllBtAppSize.toBytes().length
                + memberInfoBtApp.toBytes().length * tempNum;

    }

    /**
     * 添加入组 手动与扫描TODO 0xYYYYYYYY 00：入组失败，0xYYYYYYYY 01：入组成功------------------《修改队员信息》-------------------------------------------------------
     */
    private HwtxCommandAddDevice commandAddIn = null;
    private byte[] tagAddIn = null;

    public void addIn(String deviceSn) {

        if (mConnected) {
            System.out
                    .println("添加入组:\n");
            if (commandAddIn == null) {
                commandAddIn = new HwtxCommandAddDevice();
            }
            if (tagAddIn == null) {
                tagAddIn = commandAddIn.getCommandTagRaw();
            }

            try {
                commandAddIn.setDeviceSn(deviceSn);
            } catch (HwtxCommandException e) {
                //
                e.printStackTrace();
            }
            sendData(commandAddIn.getBluetoothPropertyUuidSend(),
                    commandAddIn.getBluetoothPropertyUuidRead(),
                    commandAddIn.toBytes());
        } else {
            EventBus.getDefault().post(
                    new ConnectBean(mConnected));
        }


    }

    /**
     * 离队
     */
    private HwtxCommandLIDUI commandLIDUI = null;
    private byte[] tagLidui = null;

    public void liDui(String deviceSn) {
        if (mConnected) {
            System.out
                    .println("离队:\n");
            if (commandLIDUI == null) {
                commandLIDUI = new HwtxCommandLIDUI();
            }
            if (tagLidui == null) {
                tagLidui = commandLIDUI.getCommandTagRaw();
            }

            try {
                commandLIDUI.setDeviceSn(deviceSn);
            } catch (HwtxCommandException e) {
                //
                e.printStackTrace();
            }
            sendData(commandLIDUI.getBluetoothPropertyUuidSend(),
                    commandLIDUI.getBluetoothPropertyUuidRead(),
                    commandLIDUI.toBytes());

        } else {
            EventBus.getDefault().post(
                    new ConnectBean(mConnected));
        }
    }

    /**
     * 归队
     */
    private HwtxCommandGUIDUI commandGUIDUI = null;
    private byte[] tagGuidui = null;

    public void guiDui(String deviceSn) {
        System.out
                .println("归队:\n");
        if (mConnected) {
            if (commandGUIDUI == null) {
                commandGUIDUI = new HwtxCommandGUIDUI();
            }
            if (tagGuidui == null) {
                tagGuidui = commandGUIDUI.getCommandTagRaw();
            }

            try {
                commandGUIDUI.setDeviceSn(deviceSn);
            } catch (HwtxCommandException e) {
                //
                e.printStackTrace();
            }
            sendData(commandGUIDUI.getBluetoothPropertyUuidSend(),
                    commandGUIDUI.getBluetoothPropertyUuidRead(),
                    commandGUIDUI.toBytes());
        } else {
            EventBus.getDefault().post(
                    new ConnectBean(mConnected));
        }


    }

    /**
     * 准备同步设备参数 --------------------------------《活动设置》------------------------------------------------------
     */
    private HwtxCommandReceiveSetting commandHwtxCommandSendGpsPrepare = null;
    private byte[] tagHwtxCommandSendGpsPrepare = null;
    private Integer bGpsInterval = 0;
    private Integer bHktAtInterval = 0;
    private Integer bLostContactNum = 0;
    private Integer wWarningDistance1 = 0;
    private Integer wWarningDistance2 = 0;
    private Integer wWarningDistance3 = 0;
    private Integer bWarningBatteryPercent = 0;

    public void hwtxCommandSendGpsPrepare(Integer bGpsInterval, Integer bHktAtInterval, Integer bLostContactNum, Integer wWarningDistance1, Integer wWarningDistance2, Integer wWarningDistance3, Integer bWarningBatteryPercent) {
        if (mConnected) {
            //
            System.out
                    .println(" 准备同步设备参数:\n");
            this.bGpsInterval = bGpsInterval;
            this.bHktAtInterval = bHktAtInterval;
            this.bLostContactNum = bLostContactNum;
            this.wWarningDistance1 = wWarningDistance1;
            this.wWarningDistance1 = wWarningDistance1;
            this.wWarningDistance2 = wWarningDistance2;
            this.wWarningDistance3 = wWarningDistance3;
            this.bWarningBatteryPercent = bWarningBatteryPercent;
            setB00lean = false;
            if (commandHwtxCommandSendGpsPrepare == null) {
                commandHwtxCommandSendGpsPrepare = new HwtxCommandReceiveSetting();
            }

            if (tagHwtxCommandSendGpsPrepare == null) {
                tagHwtxCommandSendGpsPrepare = commandHwtxCommandSendGpsPrepare
                        .getCommandTagRaw();
            }

            try {
                commandHwtxCommandSendGpsPrepare
                        .setSettingDataLength(equipmentParameterListSize());
            } catch (HwtxCommandException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Utile.needContinue = true;
            sendData(
                    commandHwtxCommandSendGpsPrepare.getBluetoothPropertyUuidSend(),
                    commandHwtxCommandSendGpsPrepare.getBluetoothPropertyUuidRead(),
                    commandHwtxCommandSendGpsPrepare.toBytes());
        } else {
            EventBus.getDefault().post(
                    new ConnectBean(mConnected));
        }
    }

    /**
     * 算出所有数据大小
     */
    public void equipmentParameterListAll() {
        System.out
                .println("equipmentParameterListAll:\n");
        byte[] temp = equipmentParameterList2();
        // 本数据结构，除了wCrcGrpInfoAllBtApp之外所有数据的CRC值。(CRC计算：包括从dwSizeInByte开始，到最后一个成员的HWTX_MemberInfo_BtAPP内容）
        command.setConfigCRCInteger(HwtxCommandUtility.crc16(temp,
                temp.length - 2));

    }

    /**
     * 设备参数表 TODO 需要发送的值
     */
    HwtxDataFirmwareConfig command = null;

    private byte[] equipmentParameterList() {
        System.out
                .println("设置设备参数表:\n");
        command = new HwtxDataFirmwareConfig();
        command.setbDataType(0x62);
        command.setGpsIntervalInteger(bGpsInterval);
        command.setHktAtIntervalInteger(bHktAtInterval);
        command.setLostContactNumInteger(bLostContactNum);
        command.setWarningDistance1Integer(wWarningDistance1);
        command.setWarningDistance2Integer(wWarningDistance2);
        command.setWarningDistance3Integer(wWarningDistance3);
        command.setWarningBatteryPercentInteger(bWarningBatteryPercent);
        equipmentParameterListAll();
        System.out.println("equipmentParameterList:\n"
                + HwtxCommandUtility.bytesToHexText(command.toBytes()));
        return command.toBytes();
    }

    /**
     * @return计算crc值
     */
    private byte[] equipmentParameterList2() {
        System.out
                .println("equipmentParameterList2:\n");
        HwtxDataFirmwareConfig command = new HwtxDataFirmwareConfig();
        command.setbDataType(0x62);
        command.setGpsIntervalInteger(bGpsInterval);
        command.setHktAtIntervalInteger(bHktAtInterval);
        command.setLostContactNumInteger(bLostContactNum);
        command.setWarningDistance1Integer(wWarningDistance1);
        command.setWarningDistance2Integer(wWarningDistance2);
        command.setWarningDistance3Integer(wWarningDistance3);
        command.setWarningBatteryPercentInteger(bWarningBatteryPercent);
        return command.toBytes();
    }

    /**
     * @return 整个大小
     */
    private int equipmentParameterListSize() {
        System.out
                .println("equipmentParameterListSize:\n");
        HwtxDataFirmwareConfig command = new HwtxDataFirmwareConfig();
        return command.toBytes().length;
    }

    /**
     * （领队宝） 获取GPS数据表 每个从硬件获取数据的操作，1硬件都会先返回数据的大小，2再返回数据TODO-------------------《地图界面》--------------------------------------------
     */
    private HwtxCommandReceiveGps commandGetGPSDataTable = null;
    private byte[] tagGetGPSDataTable = null;

    public void getGPSDataTable() {
        System.out
                .println("获取GPS数据表:\n");
        if (mConnected) {
            if (commandGetGPSDataTable == null) {
                commandGetGPSDataTable = new HwtxCommandReceiveGps();
            }
            tagGetGPSDataTable = commandGetGPSDataTable.getCommandTagRaw();
            sendData(commandGetGPSDataTable.getBluetoothPropertyUuidSend(),
                    commandGetGPSDataTable.getBluetoothPropertyUuidRead(),
                    commandGetGPSDataTable.toBytes());
        } else {
            EventBus.getDefault().post(
                    new ConnectBean(mConnected));
        }
    }

    /**
     * 从设备获取经纬度解析
     */

    // 表1
    private HwtxDataGroupGPSInfoTable hwtxDataGroupGPSInfoTable = null;
    // 表2
    private HwtxDataGrpGpsInfoItem hwtxDataGrpGpsInfoItem = null;
    // 表3
    private HwtxDataGpsInfoDataComp gpsInfoDataComp = null;
    ArrayList<HwtxDataGrpGpsInfoItem> indexGpsInfoArray = null;

    private void getHwtxDataGpsInfoDataComp(byte[] gpsInfobyte) {
        System.out
                .println("从设备获取经纬度解析:\n");
        // 第一步，拿到所有队员的gps信息
        if (hwtxDataGroupGPSInfoTable == null) {
            hwtxDataGroupGPSInfoTable = new HwtxDataGroupGPSInfoTable();
        }
        hwtxDataGroupGPSInfoTable.fromBytes(gpsInfobyte);
        indexGpsInfoArray = hwtxDataGroupGPSInfoTable.getIndexGpsInfoArray();
        // 拿到第一个队员（领队）的gps信息
        if (hwtxDataGrpGpsInfoItem == null) {
            hwtxDataGrpGpsInfoItem = new HwtxDataGrpGpsInfoItem();
        }
        // 逻辑编号（index>=1) 组内编号 GPS数据
        // byte[] devNumInGroup=hwtxDataGrpGpsInfoItem.getDevNumInGroupRaw();
        int devNumInGroupInt = 1;
        for (int i = 0; i < indexGpsInfoArray.size(); i++) {
            int tps_id = indexGpsInfoArray
                    .get(i).getNewFlaguDevNumInGroupU().getDevNumInGroup();
            SignUpUser s = LitepalUtil.getAllUserByTpsid(String.valueOf(tps_id));
            if(s!=null){
            String deviceSN = s.getStrDeviceSN();
            String LingDuiNameSN = App.getInstance().getBleLingDuiName();
            String DuiYuanNameSN = App.getInstance().getBleDuiYuanName();
            //通过设备参数号找到对应的当前设备的队员
            if ((TextUtils.equals(deviceSN, LingDuiNameSN) || (TextUtils.equals(deviceSN, DuiYuanNameSN)))) {
                devNumInGroupInt = i;
            }
            }
        }
        hwtxDataGrpGpsInfoItem = indexGpsInfoArray.get(devNumInGroupInt); // TODO
        // 根据队员的位子来拿对应的经纬度
        hwtxDataGrpGpsInfoItem.toBytes();
        // hwtxDataGrpGpsInfoItem.fromBytes(indexGpsInfoArray.get(0).toBytes());
        if (gpsInfoDataComp == null) {
            gpsInfoDataComp = new HwtxDataGpsInfoDataComp();
        }
        gpsInfoDataComp = hwtxDataGrpGpsInfoItem.getGpsInfoDataComp();
        // byte[] testbyte=gpsInfoDataComp.toBytes();
        // gpsInfoDataComp.fromBytes(gpsInfobyte);

        getGps(gpsInfoDataComp, hwtxDataGrpGpsInfoItem);
    }

    /**
     * 获取具体的gps工具方法
     */
    private void getGps(HwtxDataGpsInfoDataComp gpsInfoDataComp, HwtxDataGrpGpsInfoItem hwtxDataGrpGpsInfoItem) {
        System.out
                .println("获取具体的gps工具方法:\n");
        // 从设备拿到经纬度等数据信息
        double longitude = 0;
        double latitude = 0;
        double gpsLatitude = 0;
        double gpsLongitude = 0;
        byte[] byt0 = {0};
        byte[] byt1 = {1};
        byte[] testbyte = gpsInfoDataComp.toBytes();
        byte[] gpsTimebyte = gpsInfoDataComp.getGpsTimeRaw();
        byte[] gpsLatitudebyte = gpsInfoDataComp.getGpsLatitudeRaw();
        byte[] gpsLongitudebyte = gpsInfoDataComp.getGpsLongitudeRaw();
        boolean hasNewConfig = hwtxDataGrpGpsInfoItem.getNewFlaguDevNumInGroupU().getuHasNewConfig();
        //大表2更新标志
        boolean hasNewBt = hwtxDataGrpGpsInfoItem.getNewFlaguDevNumInGroupU().getuHasNewBtApp2();
        // unsigned Year : 12; //00~2xxx.(max4095)
        // unsigned Month : 4; //01~12
        // unsigned Day : 5; //01~31
        // unsigned Hour : 5; //00~23
        // unsigned Minute : 6; //00~59

        // unsigned Sign:1 ; //符号位
        // unsigned Integral : 10; //整数部分: 0~511
        // unsigned Decimal : 21; //小数部分: 0~2097152

        // 时间
        byte[] gpsTimeYear = HwtxCommandUtility.extractBitsFromBytes(
                gpsTimebyte, 0, 12);

        byte[] gpsTimeMonth = HwtxCommandUtility.extractBitsFromBytes(
                gpsTimebyte, 12, 4);
        byte[] gpsTimeDay = HwtxCommandUtility.extractBitsFromBytes(
                gpsTimebyte, 16, 5);
        byte[] gpsTimeHour = HwtxCommandUtility.extractBitsFromBytes(
                gpsTimebyte, 21, 5);
        byte[] gpsTimeMinute = HwtxCommandUtility.extractBitsFromBytes(
                gpsTimebyte, 26, 6);

        String gpsTimeYearStr = String.valueOf(HwtxCommandUtility
                .bytesToInt32(gpsTimeYear));
        String gpsTimeMonthStr = String.valueOf(HwtxCommandUtility
                .bytesToInt32(gpsTimeMonth));
        String gpsTimeDayStr = String.valueOf(HwtxCommandUtility
                .bytesToInt32(gpsTimeDay));
        String gpsTimeHourStr = String.valueOf(HwtxCommandUtility
                .bytesToInt32(gpsTimeHour));
        String gpsTimeMinuteStr = String.valueOf(HwtxCommandUtility
                .bytesToInt32(gpsTimeMinute));
        String gpsTime = gpsTimeYearStr + "-" + gpsTimeMonthStr + "-"
                + gpsTimeDayStr + "-" + gpsTimeHourStr + "-"
                + gpsTimeMinuteStr;
        // unsigned Sign:1 ; //符号位
        // unsigned Integral : 10; //整数部分: 0~511
        // unsigned Decimal : 21; //小数部分: 0~2097152
        // gpsLatitude
        byte[] gpsLatitudeSign = HwtxCommandUtility
                .extractBitsFromBytes(gpsLatitudebyte, 0, 1);
        byte[] gpsLatitudeIntegral = HwtxCommandUtility
                .extractBitsFromBytes(gpsLatitudebyte, 1, 10);
        byte[] gpsLatitudeDecimal = HwtxCommandUtility
                .extractBitsFromBytes(gpsLatitudebyte, 11, 21);

        String gpsLatitudeDecimalStr = String.valueOf(String
                .valueOf(HwtxCommandUtility
                        .bytesToInt32(gpsLatitudeDecimal)));
        switch (gpsLatitudeDecimalStr.length()) {
            case 5:
                gpsLatitudeDecimalStr = "0" + gpsLatitudeDecimalStr;
                break;
            case 4:
                gpsLatitudeDecimalStr = "00" + gpsLatitudeDecimalStr;
                break;
            case 3:
                gpsLatitudeDecimalStr = "000" + gpsLatitudeDecimalStr;
                break;
            case 2:
                gpsLatitudeDecimalStr = "0000" + gpsLatitudeDecimalStr;
                break;
            case 1:
                gpsLatitudeDecimalStr = "00000" + gpsLatitudeDecimalStr;
                break;

            default:
                break;
        }
        double gpsLatitudeTemp = Double.parseDouble(String
                .valueOf(
                        HwtxCommandUtility
                                .bytesToInt32(gpsLatitudeIntegral))
                .concat(".").concat(gpsLatitudeDecimalStr));
        if (Arrays.equals(gpsLatitudeSign, byt0)) {// 正，否则负
            gpsLatitude = gpsLatitudeTemp;
        } else {
            gpsLatitude = -gpsLatitudeTemp;
        }

        // gpsLongitude
        byte[] gpsLongitudeSign = HwtxCommandUtility
                .extractBitsFromBytes(gpsLongitudebyte, 0, 1);
        byte[] gpsLongitudeIntegral = HwtxCommandUtility
                .extractBitsFromBytes(gpsLongitudebyte, 1, 10);
        byte[] gpsLongitudeDecimal = HwtxCommandUtility
                .extractBitsFromBytes(gpsLongitudebyte, 11, 21);
        String gpsLongitudeIntegralStr = String
                .valueOf(HwtxCommandUtility
                        .bytesToInt32(gpsLongitudeIntegral));
        String LongitudeDecimalStr = String.valueOf(String
                .valueOf(HwtxCommandUtility
                        .bytesToInt32(gpsLongitudeDecimal)));
        switch (LongitudeDecimalStr.length()) {
            case 5:
                LongitudeDecimalStr = "0" + LongitudeDecimalStr;
                break;
            case 4:
                LongitudeDecimalStr = "00" + LongitudeDecimalStr;
                break;
            case 3:
                LongitudeDecimalStr = "000" + LongitudeDecimalStr;
                break;
            case 2:
                LongitudeDecimalStr = "0000" + LongitudeDecimalStr;
                break;
            case 1:
                LongitudeDecimalStr = "00000" + LongitudeDecimalStr;
                break;

            default:
                break;
        }
        double gpsLongitudeTemp = Double
                .parseDouble(gpsLongitudeIntegralStr.concat(".")
                        .concat(LongitudeDecimalStr));

        if (Arrays.equals(gpsLongitudeSign, byt0)) {// 正，否则负
            gpsLongitude = gpsLongitudeTemp;
        } else {
            gpsLongitude = -gpsLongitudeTemp;
        }
        // gpsTime
        System.out.println("gpsTime:" + gpsTime + "\ngpsLatitude:"
                + gpsLatitude + "\ngpsLongitude:" + gpsLongitude);

        LatLng desLatLng = CommonUtility.convertGps(gpsLatitude, gpsLongitude);
        gpsLatitude = desLatLng.latitude;
        gpsLongitude = desLatLng.longitude;
        //当前队员的gps数据已经所有队员的数据包
        EventBus.getDefault().post(
                new GpsBean(gpsTime, gpsLatitude, gpsLongitude, indexGpsInfoArray, hasNewConfig, hasNewBt));
    }

    /**
     * 获取设备参数表---------------------------------------------《队员的同行宝界面》----------------------------------------------------------------------------
     */
    private HwtxCommandReceiveSetingGet commandSetingGet = null;
    private byte[] tagSetingGet = null;

    public void getSetingGetTable() {
        System.out
                .println("获取设备参数表:\n");
        if (mConnected) {
            if (commandSetingGet == null) {
                commandSetingGet = new HwtxCommandReceiveSetingGet();
            }
            tagSetingGet = commandSetingGet.getCommandTagRaw();
            sendData(commandSetingGet.getBluetoothPropertyUuidSend(),
                    commandSetingGet.getBluetoothPropertyUuidRead(),
                    commandSetingGet.toBytes());
        } else {
            EventBus.getDefault().post(
                    new ConnectBean(mConnected));
        }
    }

    private void getSetingValue(byte[] returenByte) {
        HwtxDataFirmwareConfig firmwareConfig = new HwtxDataFirmwareConfig();
        firmwareConfig.fromBytes(returenByte);
        Integer bGpsInterval = firmwareConfig.getGpsIntervalInteger();
        Integer bHktAtInterval = firmwareConfig.getHktAtIntervalInteger();
        Integer bLostContactNum = firmwareConfig.getLostContactNumInteger();
        Integer wWarningDistance1 = firmwareConfig.getWarningDistance1Integer();
        Integer wWarningDistance2 = firmwareConfig.getWarningDistance2Integer();
        Integer wWarningDistance3 = firmwareConfig.getWarningDistance3Integer();
        Integer bWarningBatteryPercent = firmwareConfig.getWarningBatteryPercentInteger();
        EventBus.getDefault().post(
                new ActivitySetBean(bGpsInterval, bHktAtInterval, bLostContactNum, wWarningDistance1, wWarningDistance2, wWarningDistance3, bWarningBatteryPercent));
    }

    /**
     * 获取队员信息表
     */
    private HwtxCommandReceiveMember receiveMember = null;
    private byte[] tagreceiveMember = null;

    public void getGroupInfoAllBtApp() {
        System.out
                .println("获取队员信息表:\n");
        if (mConnected) {
            if (receiveMember == null) {
                receiveMember = new HwtxCommandReceiveMember();
            }
            tagreceiveMember = receiveMember.getCommandTagRaw();
            sendData(receiveMember.getBluetoothPropertyUuidSend(),
                    receiveMember.getBluetoothPropertyUuidRead(),
                    receiveMember.toBytes());

        } else {
            EventBus.getDefault().post(
                    new ConnectBean(mConnected));
        }
    }

    private void getReceiveMember(byte[] returenByte) {
        try {
            //每次同步的时候先清空数据库
            DataSupport.deleteAll(SignUpUser.class);
            HwtxDataGroupInfoAllBtApp hdg = new HwtxDataGroupInfoAllBtApp();
            hdg.fromBytes(returenByte);
            ArrayList<HwtxDataMemberInfoBtApp> indexMemberInfoArray = hdg.getIndexMemberInfoArray();
            for (int i = 0; i < indexMemberInfoArray.size(); i++) {
                //目前只app端只需要这三个参数
                String strNickName = indexMemberInfoArray.get(i).getNickNameString();
                String strDeviceSN = indexMemberInfoArray.get(i).gethWTXMemberInfoMap().getMemberInfo().getDeviceSnString();
                Integer uDevNumInGroup = indexMemberInfoArray.get(i).gethWTXMemberInfoMap().getMemberInfo().getDevInfo().getDevNumInGroup();
                boolean deviceReady = indexMemberInfoArray.get(i).gethWTXMemberInfoMap().getMemberInfo().getDevInfo().getDeviceReady();
                //存入本地数据库中
                SignUpUser s = new SignUpUser();
                s.setU_nickname(strNickName);
                s.setStrDeviceSN(strDeviceSN);
                if (deviceReady) {
                    s.setDeviceReady(1);
                } else {
                    s.setDeviceReady(2);
                }
                s.setTps_id(uDevNumInGroup);

                if (uDevNumInGroup == 1) {//等于1时为领队
                    s.setTps_type(CommonUtility.LINGDUI);
                } else {
                    s.setTps_type(CommonUtility.DUIYUAN);
                }
                String duiYuanNameStr = App.getInstance().getBleDuiYuanName();
                if (TextUtils.equals(duiYuanNameStr, strDeviceSN)) {//相等则是自己
                    s.setIsUsedByCurrentDevice(CommonUtility.SELF);
                } else if (TextUtils.equals(App.getInstance().getBleLingDuiName(), strDeviceSN)) {
                    s.setIsUsedByCurrentDevice(CommonUtility.SELF);
                }
                s.setAct_isleave(CommonUtility.GUIDUI);
                s.save();
            }
            EventBus.getDefault().post(
                    new GetMemberInfoBean());
        } catch (Exception e) {

        }
    }

    /**
     * @param bs 获取需要展示的值 。拿到所有的数据之后再进行其他操作，这里需要一个值来判断是否接收完成
     */
    private List<Integer> errorNumList = null;// 未同步成功的队员
    private List<byte[]> tempByte = null;

    public void displayData(byte[] bs) {
        System.out.println("展示的值:\n");
        tempByte.add(bs);
        byte[] newByte = ByteUtil.sysCopy(tempByte);
        // 返回的tag值
        byte[] tagByte = HwtxCommandUtility
                .extractBytesFromBytes(newByte, 0, 4);
        if (Arrays.equals(tagByte, tagQueryMode)) {// 查询模式返回值

            byte[] mode = HwtxCommandUtility.extractBytesFromBytes(newByte, 4,
                    1);
            EventBus.getDefault().post(
                    new QueryModeBean(mode));


        } else if (Arrays.equals(tagByte, tagSetTeamLeader)) {/**设置领队模式返回值,设置了之后继续查询看是否设置成功*/

        } else if (Arrays.equals(tagByte, tagCancelTeamLeader)) {/** 设置取消领队模式返回值,设置了之后继续查询看是否设置成功*/
        } else if (Arrays.equals(tagByte, tagSynchronizationInformationTable)) {/** 同步队员信息表返回值*/
            // 设备返回的byte[] //
            byte[] returenByte = HwtxCommandUtility.extractBytesFromBytes(
                    newByte, 4, newByte.length - 4);
            // 计算当前crc计算错误需要的byte
            byte[] tempByte = new byte[newByte.length - 4];
            for (int i = 0; i < tempByte.length; i++) {
                tempByte[i] = 0;
            }
            if (Arrays.equals(returenByte, tempByte)) {// crc值计算错误，需重新发数据

                errorNumList = new ArrayList<Integer>();
                EventBus.getDefault().post(
                        new MemberInfoBean(true, errorNumList));
            } else {
                List<SignUpUser> signUpUserListGet = DataSupport.findAll(SignUpUser.class);
                int tempNum = 0;
                for (int i = 0; i < signUpUserListGet.size(); i++) {
                    if (!TextUtils.isEmpty(signUpUserListGet.get(i)
                            .getStrDeviceSN())) {// 同行宝和领队宝的总数
                        tempNum++;
                    }
                }
                // 我自己需要的byte[]
                byte[] needByte = HwtxCommandUtility.extractBitsFromBytes(
                        returenByte, 0, tempNum);//
                errorNumList = new ArrayList<Integer>();
                // 拿到所有bit为0（未成功的）的队员位置
                if (needByte != null) {
                    for (int i = 0; i < tempNum; i++) {// 根据同行宝和领队宝的总数来计算
                        if (HwtxCommandUtility.extractBitFromBytes(needByte, i) == 0)// 1表示同步成功，0表示同步失败
                        {
                            errorNumList.add(i + 1);
                        }
                    }
                }
                EventBus.getDefault().post(
                        new MemberInfoBean(false, errorNumList));
            }

        } else if (Arrays.equals(tagByte, tagAddIn)) {/**添加入组返回值*/
            byte[] mode = HwtxCommandUtility.extractBytesFromBytes(newByte, 4,
                    1);
            EventBus.getDefault().post(
                    new AddInBean(mode));
        } else if (Arrays.equals(tagByte, tagLidui)) {/**离队*/
            byte[] mode = HwtxCommandUtility.extractBytesFromBytes(newByte, 9,
                    1);
            EventBus.getDefault().post(
                    new LidDUIBean(mode));
        } else if (Arrays.equals(tagByte, tagGuidui)) {/**归队*/
            byte[] mode = HwtxCommandUtility.extractBytesFromBytes(newByte, 9,
                    1);
            EventBus.getDefault().post(
                    new GuiDuiBean(mode));
        } else if (Arrays.equals(tagByte, tagHwtxCommandSendGpsPrepare)) {/**设置设备参数返回值*/
            // 设备返回的byte // TODO
            byte[] returenByte = HwtxCommandUtility.extractBytesFromBytes(
                    newByte, 4, newByte.length - 4);
            byte[] tempByte = new byte[newByte.length - 4];
            for (int i = 0; i < tempByte.length; i++) {
                tempByte[i] = 0;
            }
            if (Arrays.equals(returenByte, tempByte)) {// crc值计算错误，需重新发数据
                errorNumList = new ArrayList<Integer>();
                EventBus.getDefault().post(
                        new SetBean(true, errorNumList));
            } else {
                List<SignUpUser> signUpUserListGet = DataSupport.findAll(SignUpUser.class);
                int tempNum = 0;
                for (int i = 0; i < signUpUserListGet.size(); i++) {
                    if (!TextUtils.isEmpty(signUpUserListGet.get(i)
                            .getStrDeviceSN())) {// 同行宝和领队宝的总数
                        tempNum++;
                    }
                }
                // 我自己需要的byte[]
                byte[] needByte = HwtxCommandUtility.extractBitsFromBytes(
                        returenByte, 0, tempNum);//
                errorNumList = new ArrayList<Integer>();
                // 拿到所有bit为0（未成功的）的队员位置
                if (needByte != null) {
                    for (int i = 0; i < tempNum; i++) {// 根据同行宝和领队宝的总数来计算
                        if (HwtxCommandUtility.extractBitFromBytes(needByte, i) == 0)// 1表示同步成功，0表示同步失败
                        {
                            errorNumList.add(i + 1);
                        }
                    }
                }
                EventBus.getDefault().post(
                        new SetBean(false, errorNumList));
            }
        } else if (Arrays.equals(tagByte, tagGetGPSDataTable)) {/** 获取GPS数据表*/
            // gps数据的长度
            byte[] lengthByte = HwtxCommandUtility.extractBytesFromBytes(
                    newByte, 9, 6);
            int lengthInt = HwtxCommandUtility.bytesToInt32(lengthByte);
            System.out.println("lengthInt:----------" + lengthInt);
            if (lengthInt == 0) {
                EventBus.getDefault().post(
                        new LengthZero(1));
            } else if (newByte.length == (15 + lengthInt)) {// 如果总长度等于准备传输的长度+gps数据长度时执行
                // 设备返回的byte //
                byte[] returenByte = HwtxCommandUtility.extractBytesFromBytes(
                        newByte, 15, newByte.length - 15);

                getHwtxDataGpsInfoDataComp(returenByte);
            }

        } else if (Arrays.equals(tagByte, tagSetingGet)) {/** 获取设备参数表*/
            // gps数据的长度
            byte[] lengthByte = HwtxCommandUtility.extractBytesFromBytes(
                    newByte, 9, 6);//// 6字节表示长度 private byte[] commandData = { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
            int lengthInt = HwtxCommandUtility.bytesToInt32(lengthByte);
            System.out.println("lengthInt:----------" + lengthInt);
            if (newByte.length == (15 + lengthInt)) {// 如果总长度等于准备传输的长度（15为固定值）+备参数表数据长度时执行
                // 设备返回的byte //
                byte[] returenByte = HwtxCommandUtility.extractBytesFromBytes(
                        newByte, 15, newByte.length - 15);

                getSetingValue(returenByte);
            }
        } else if (Arrays.equals(tagByte, tagreceiveMember)) {/** 获取队员信息表*/
            // gps数据的长度
            byte[] lengthByte = HwtxCommandUtility.extractBytesFromBytes(
                    newByte, 9, 6);//// 6字节表示长度 private byte[] commandData = { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
            int lengthInt = HwtxCommandUtility.bytesToInt32(lengthByte);
            System.out.println("lengthInt:----------" + lengthInt);
            if (lengthInt == 0) {
                EventBus.getDefault().post(
                        new LengthZero(2));
            } else if (newByte.length == (15 + lengthInt)) {// 如果总长度等于准备传输的长度（15为固定值）+备参数表数据长度时执行
                // 设备返回的byte //
                byte[] returenByte = HwtxCommandUtility.extractBytesFromBytes(
                        newByte, 15, newByte.length - 15);

                getReceiveMember(returenByte);
            }

        } else if (Arrays.equals(tagByte, new byte[]{0, 0, 51, 0})) {/** --------------------------------------------------《GPS 数据测试》-----------------------------------------------------------*/
            getHwtxDataGpsInfoDataComp(newByte);
        } else if (Arrays.equals(tagByte, new byte[]{59, -92, 109, 0})) {/** --------------------------------------------------《同步队员信息表 数据测试》-----------------------------------------------------------*/
            getReceiveMember(newByte);
        }
    }
}
