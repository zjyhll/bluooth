/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keyhua.renameyourself.main.le;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.keyhua.renameyourself.main.protocol.HwtxCommandUtility;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.hwacreate.outdoor.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_CONNECTED_AGIAN = "com.hwacreate.outdoor.bluetooth.le.ACTION_GATT_CONNECTED_AGIAN";
    public final static String ACTION_GATT_DISCONNECTED = "com.hwacreate.outdoor.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.hwacreate.outdoor.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.hwacreate.outdoor.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.hwacreate.outdoor.bluetooth.le.EXTRA_DATA";
    public final static String SENDOVER = "com.hwacreate.outdoor.bluetooth.le.OVER";

    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
            .fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    // Implements callback methods for GATT events that the app cares about. For
    // example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            Log.w(TAG, "onConnectionStateChange: " + "onConnectionStateChange");
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:"
                        + mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.w(TAG, "onServicesDiscovered: ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            Log.w(TAG, "onCharacteristicRead: ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            try {
                byte[] aa = characteristic.getValue();
                Log.e("返回读出的值:", Utile.byte2HexStr(aa));
                Log.e("返回读出的值str:", new String(aa, "GB2312"));
                Log.e("返回读出的值UUID:", characteristic.getUuid().toString());
            } catch (Exception e) {
            }
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {// TODO
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            // if (data != null && data.length > 0) {
            // final StringBuilder stringBuilder = new StringBuilder(
            // data.length);
            // for (byte byteChar : data)
            // stringBuilder.append(String.format("%02X ", byteChar));
            // intent.putExtra(EXTRA_DATA, stringBuilder.toString());
            // }
            intent.putExtra(EXTRA_DATA, data);
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that
        // BluetoothGatt.close() is called
        // such that resources are cleaned up properly. In this particular
        // example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter
        // through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The
     * connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG,
                    "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device. Try to reconnect.
        if (mBluetoothDeviceAddress != null
                && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG,
                    "Trying to use an existing mBluetoothGatt for connection.");// TODO
            // 已连接状态，不用重新连接
            broadcastUpdate(ACTION_GATT_CONNECTED_AGIAN);
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        BluetoothDevice device = null;
        if (!TextUtils.isEmpty(address)) {
            device = mBluetoothAdapter.getRemoteDevice(address);
        }

        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the
        // autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if(mBluetoothGatt!=null){
            mBluetoothGatt.disconnect();
        }

    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }else{
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }

    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification. False otherwise.
     */
    public void setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if (characteristic != null) {

            boolean a = mBluetoothGatt.setCharacteristicNotification(
                    characteristic, enabled);

            BluetoothGattDescriptor descriptor = characteristic
                    .getDescriptor(UUID
                            .fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            if (descriptor != null) {
                descriptor
                        .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                boolean success = mBluetoothGatt.writeDescriptor(descriptor);
                Log.e(TAG, "descriptor:" + descriptor.getValue().toString());
            }
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }

    /**
     * 发送数据
     *
     * @param characteristic
     * @param bb
     * @return
     */
    public Boolean write(final BluetoothGattCharacteristic characteristic,
                         final byte[] bbb) {
        System.out.println("6----------------------------" + mBluetoothGatt);
        if (mBluetoothGatt == null) {
            System.out.println("7----------------------------");
            Log.e(TAG, "mBluetoothGatt==空");
            return false;
        }
        System.out.println("8----------------------------" + characteristic);
        if (characteristic == null) {
            System.out.println("9----------------------------");
            Log.e(TAG, "characteristic==空");
            return false;
        }
        String s = HwtxCommandUtility.bytesToHexText(bbb);
        System.out.println("SEND:\n" + s);
        /** 发送大于20字节或小于20字节数据方法 */
        new Handler().postDelayed(new Runnable() {
            public void run() {

                byte[] aa = bbb;
                byte[] bb = null;
                int len = aa.length;
                int flags = 0;
                boolean f = false;
                do {
                    if (len <= 20) {
                        bb = new byte[len];
                        System.arraycopy(aa, flags, bb, 0, len);
                    } else {
                        bb = new byte[20];
                        System.arraycopy(aa, flags, bb, 0, 20);

                    }

                    characteristic.setValue(bb);
                    f = mBluetoothGatt.writeCharacteristic(characteristic);
                    System.out.println("是否发送成功：" + f);
                    if (f) {// 发送成功时减20
                        if (len <= 20) {

                        } else {
                            flags += 20;
                        }

                        len -= 20;
                    }

                } while (len > 0);
                // 同步数据时用到,判断是否是同步数据
                Utile.onlyOnce = true;
                if (Utile.needContinue) {
                    String intentAction = SENDOVER;
                    mConnectionState = STATE_CONNECTED;
                    broadcastUpdate(intentAction);
                }

            }
        }, 1000);// 需要延时操作
        return null;
    }
}
