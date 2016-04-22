package com.keyhua.renameyourself.main;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.view.CleareditTextView;
import com.keyhua.renameyourself.view.CustomDialog;

public class DownOfflineActivity extends BaseActivity implements
		MKOfflineMapListener {
	private TextView cityid = null;// 用来显示城市id
	private CleareditTextView city = null;// 输入搜索内容
	private TextView search = null;// 搜索
	private TextView state = null;// 下载状态
	private TextView start = null;// 开始下载
	private TextView stop = null;// 停止下载
	private ListView allcitylist = null;
	private MKOfflineMap mOffline = null;
	private MyAllCityAdpter allCityAdpter = null;
	/**
	 * 已下载的离线地图信息列表
	 */
	private ArrayList<MKOLUpdateElement> localMapList = null;

	String cityidStrstart = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_offline);
		mOffline = new MKOfflineMap();
		mOffline.init(this);
		initHeaderOther("", "地图下载", true, false, false);
		init();
	}

	@Override
	protected void onPause() {
		int cityidInt = Integer.parseInt(!TextUtils.isEmpty(cityid.getText()
				.toString()) ? cityid.getText().toString() : "0");
		MKOLUpdateElement temp = mOffline.getUpdateInfo(cityidInt);
		if (temp != null && temp.status == MKOLUpdateElement.DOWNLOADING) {
			mOffline.pause(cityidInt);
		}
		super.onPause();
	}

	boolean isdown = false;
	boolean isstop = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.search:
				String cityStr = city.getText().toString();
				if (TextUtils.isEmpty(cityStr)) {
					showToast("查询内容为空");
				} else {
					if (TextUtils.equals(state.getText().toString(), "未开始")) {
						search(cityStr);
						// }else{
						// showAlertDialog("是否放弃当前下载?                   ", 1);
						// }
					} else {
						showToast("正在下载中");

					}

				}
				break;
			case R.id.start:
				isdown = false;
				cityidStrstart = cityid.getText().toString();
				// 获取已下过的离线地图信息
				localMapList = mOffline.getAllUpdateInfo();
				if (localMapList == null) {
					localMapList = new ArrayList<MKOLUpdateElement>();
				}
				for (int i = 0; i < localMapList.size(); i++) {
					if (TextUtils.equals(cityidStrstart, localMapList.get(i).cityID
							+ "")
							&& localMapList.get(i).ratio == 100) {
						isdown = true;
						state.setText("已下载");
					}
				}
				if (TextUtils.isEmpty(cityidStrstart)) {
					showToast("先选择或查询所需城市");
				} else {
					if (isReal) {
						if (TextUtils.equals(state.getText().toString(), "未开始")) {
							showToast("开始下载");
							start(cityidStrstart);
						} else {
							if (isstop) {
								showToast("开始下载");
								start(cityidStrstart);
							} else {
								showToast("地图下载中...");
							}

						}

					} else {
						showToast("请输入正确的城市名");
					}
				}
				break;
			case R.id.stop:
				String cityidStrstop = cityid.getText().toString();
				if (TextUtils.isEmpty(cityidStrstop)) {
					// showToast("先选择或查询所需城市");
					showToast("请先下载地图");
				} else {
					showToast("暂停成功");
					isstop = true;
					stop(cityidStrstop);
				}
				break;
			case R.id.toolbar_bac:
				finish();
			default:
				break;
		}
	}

	@Override
	protected void onInitData() {

		cityid = (TextView) findViewById(R.id.cityid);
		cityid.setText("");
		city = (CleareditTextView) findViewById(R.id.city);
		search = (TextView) findViewById(R.id.search);
		state = (TextView) findViewById(R.id.state);
		start = (TextView) findViewById(R.id.start);
		stop = (TextView) findViewById(R.id.stop);
		allcitylist = (ListView) findViewById(R.id.allcitylist);
		allCityAdpter = new MyAllCityAdpter(this);
		state.setText("未开始");
		// 获取已下过的离线地图信息
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}

	}

	private ArrayList<DownOffinebean> allCities = null;

	@Override
	protected void onResload() {
		// 获取所有支持离线地图的城市
		allCities = new ArrayList<DownOffinebean>();
		ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
		DownOffinebean downOffinebean = null;
		if (records2 != null) {
			for (MKOLSearchRecord r : records2) {
				// allCities.add(r.cityName + "(" + r.cityID + ")" + " --"
				// + this.formatDataSize(r.size));
				downOffinebean = new DownOffinebean();
				downOffinebean.setCityID(r.cityID);
				downOffinebean.setCityName(r.cityName);
				downOffinebean.setSize(r.size);
				allCities.add(downOffinebean);
			}
		}
		allcitylist.setAdapter(allCityAdpter);
	}

	@Override
	protected void setMyViewClick() {
		search.setOnClickListener(this);
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
	}

	/**
	 * 搜索离线需市
	 *
	 * @param view
	 */
	boolean isReal = false;

	public void search(String str) {
		ArrayList<MKOLSearchRecord> records = mOffline.searchCity(str);
		if (records == null || records.size() != 1) {
			isReal = false;
			// cityid.setText("");
			return;
		}
		cityid.setText(String.valueOf(records.get(0).cityID));
		showToast("点击开始进行下载");
		isReal = true;
	}

	/**
	 * 开始下载
	 *
	 * @param str
	 */
	@SuppressLint("NewApi")
	public void start(String str) {
		int cityid = Integer.parseInt(str);
		mOffline.start(cityid);
		if (!isdown) {
			state.setText("请稍等");
		}

		start.setBackground(getResources().getDrawable(
				R.drawable.solid_video_grey));
		stop.setBackground(getResources().getDrawable(R.drawable.solid_video));
		start.setClickable(false);
		stop.setClickable(true);

		// Toast.makeText(this, "开始下载 ", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 暂停下载
	 *
	 * @param str
	 */
	@SuppressLint("NewApi")
	public void stop(String str) {
		int cityid = Integer.parseInt(str);
		mOffline.pause(cityid);
		// Toast.makeText(this, "暂停下载 ", Toast.LENGTH_SHORT).show();

		stop.setBackground(getResources().getDrawable(
				R.drawable.solid_video_grey));
		start.setBackground(getResources().getDrawable(R.drawable.solid_video));
		stop.setClickable(false);
		start.setClickable(true);
	}

	/**
	 * 取消下载
	 *
	 * @param str
	 */
	public void cancle(String str) {
		int cityid = Integer.parseInt(str);
		mOffline.remove(cityid);
		// Toast.makeText(this, "暂停下载 ", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		/**
		 * 退出时，销毁离线地图模块
		 */
		mOffline.destroy();
		super.onDestroy();
	}

	public String formatDataSize(int size) {
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = String.format("%dK", size / 1024);
		} else {
			ret = String.format("%.1fM", size / (1024 * 1024.0));
		}
		return ret;
	}

	@Override
	public void onGetOfflineMapState(int type, int stateInt) {
		switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
				MKOLUpdateElement update = mOffline.getUpdateInfo(stateInt);
				// 处理下载进度更新提示
				if (update != null) {
					state.setText(String.format("%s : %d%%", update.cityName,
							update.ratio));
				}
				if (update.ratio == 100) {
					String cityidStrstop = cityid.getText().toString();
					if (TextUtils.isEmpty(cityidStrstop)) {
						// showToast("先选择或查询所需城市");
						showToast("请先下载地图");
					} else {
						showToast("暂停成功");
						isstop = true;
						stop(cityidStrstop);
					}

					cityid.setText("");
					// city.setText("");
					isReal = false;
					state.setText("已下载");
				}
			}
			break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				// 有新离线地图安装
				Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				// 版本更新提示
				// MKOLUpdateElement e = mOffline.getUpdateInfo(state);

				break;
			default:
				break;
		}
	}

	public class MyAllCityAdpter extends BaseAdapter {
		private Context context = null;

		public MyAllCityAdpter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return allCities.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_dituliebiao, null);
				holder = new ViewHolder();
				holder.ll = (RelativeLayout) convertView.findViewById(R.id.ll);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_daixao = (TextView) convertView
						.findViewById(R.id.tv_daixao);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (TextUtils.equals(state.getText().toString(), "已下载")
							|| TextUtils.equals(state.getText().toString(),
							"未开始")
							|| TextUtils.isEmpty(cityid.getText().toString())) {
						isReal = true;
						city.setText(allCities.get(position).getCityName());
						cityid.setText(allCities.get(position).getCityID() + "");
						showToast("您选择了"
								+ allCities.get(position).getCityName());
					} else {
						showToast("正在下载中");

					}
				}
			});
			holder.tv_name.setText(allCities.get(position).getCityName());
			holder.tv_daixao.setText(formatDataSize(allCities.get(position)
					.getSize()));
			return convertView;
		}

		private class ViewHolder {
			private RelativeLayout ll;
			private TextView tv_name, tv_daixao;
		}
	}

	/** Dialog */
	public void showAlertDialog(String title, final int index) {
		CustomDialog.Builder builder = new CustomDialog.Builder(
				DownOfflineActivity.this);
		builder.setCancelable(false);// 点击对话框外部不关闭对话框
		builder.setMessage(title);
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (index) {
					case 1:
						cancle(cityidStrstart);
						showToast("下载已取消");
						break;
					default:
						break;
				}
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

}
