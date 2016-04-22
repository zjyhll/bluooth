package com.keyhua.renameyourself.main.le;

//package com.hwacreate.outdoor.bluetooth.le;
//
//import java.util.ArrayList;
//
//import com.hwacreate.outdoor.R;
//import com.hwacreate.outdoor.leftFragment.myguardianFragment.ContactMyGuardianAcitivty;
//
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.Context;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.animation.AnimationUtils;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//public class LeDeviceListAdapter extends BaseAdapter {
//
//	// Adapter for holding devices found through scanning.
//
//	private ArrayList<BluetoothDevice> mLeDevices;
//	private LayoutInflater mInflator;
//	private Activity mContext;
//
//	public LeDeviceListAdapter(Activity c) {
//		super();
//		mContext = c;
//		mLeDevices = new ArrayList<BluetoothDevice>();
//		mInflator = mContext.getLayoutInflater();
//	}
//
//	public void addDevice(BluetoothDevice device) {
//		if (!mLeDevices.contains(device)) {
//			mLeDevices.add(device);
//		}
//	}
//
//	public BluetoothDevice getDevice(int position) {
//		return mLeDevices.get(position);
//	}
//
//	public void clear() {
//		mLeDevices.clear();
//	}
//
//	@Override
//	public int getCount() {
//		return mLeDevices.size();
//	}
//
//	@Override
//	public Object getItem(int i) {
//		return mLeDevices.get(i);
//	}
//
//	@Override
//	public long getItemId(int i) {
//		return i;
//	}
//
//	@Override
//	public View getView(int i, View view, ViewGroup viewGroup) {
//		ViewHolder viewHolder;
//		// General ListView optimization code.
//		if (view == null) {
//			view = mInflator.inflate(R.layout.listitem_device, null);
//			viewHolder = new ViewHolder();
//			viewHolder.tv_tocontact = (TextView) view
//					.findViewById(R.id.tv_tocontact);
//			viewHolder.deviceName = (TextView) view
//					.findViewById(R.id.device_name);
//			view.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) view.getTag();
//		}
//
//		BluetoothDevice device = mLeDevices.get(i);
//		final String deviceName = device.getName();
//		if (deviceName != null && deviceName.length() > 0)
//			viewHolder.deviceName.setText(deviceName);
//		else
//			viewHolder.deviceName.setText(R.string.unknown_device);
//
//		viewHolder.deviceName.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			}
//		});
//
//		return view;
//	}
//
//	class ViewHolder {
//		TextView deviceName;
//		TextView tv_tocontact;
//	}
// }
