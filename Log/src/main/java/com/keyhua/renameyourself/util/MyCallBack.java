package com.keyhua.renameyourself.util;

import android.os.Handler.Callback;
import android.os.Message;

public abstract class MyCallBack implements Callback {

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case CommonUtility.ISREFRESH:// 刷新
			reFresh();
			return true;
		case CommonUtility.ISLOADMORE:// 加载更多
			loadMore();
			return true;
		default:
			break;
		}

		return false;
	}

	public abstract boolean reFresh();

	public abstract boolean loadMore();

}
