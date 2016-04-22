package com.keyhua.renameyourself.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class MyHandler {
	private static Handler mainHandler;
	private final static HandlerThread business0HandlerThread = new HandlerThread(
			"Business0Handler");
	private static Handler business0Handler;
	private final static HandlerThread business1HandlerThread = new HandlerThread(
			"Business1Handler");
	private static Handler business1Handler;

	/*
	 * 该Handler运行在主线程中，因此一些必须放在主线程来处理的事务可以用该Hanlder来处理
	 */
	public static Handler getMainHandler(MyCallBack CALL_BACK) {
		if (mainHandler == null) {
			if (CALL_BACK != null) {
				mainHandler = new Handler(Looper.getMainLooper(), CALL_BACK);
			} else {
				mainHandler = new Handler(Looper.getMainLooper());
			}
		}
		return mainHandler;
	}

	/*
	 * 该Handler主要用于小事务处理，对于一些耗时但30秒钟能执行完的操作，建议大家放到该Handler来处理
	 */
	public static Handler getBusiness0Handler() {
		if (business0Handler == null) {
			synchronized (MyHandler.class) {
				if (business0Handler == null) {
					business0HandlerThread.start();
					business0Handler = new Handler(
							business0HandlerThread.getLooper());
				}
			}
		}
		return business0Handler;
	}

	/*
	 * 该Handler主要用于大事务处理，它可能会非常繁忙，它也许10分钟或许更久都没有空。
	 */
	public static Handler getBusiness1Handler() {
		if (business1Handler == null) {
			synchronized (MyHandler.class) {
				if (business1Handler == null) {
					business1HandlerThread.start();
					business1Handler = new Handler(
							business1HandlerThread.getLooper());
				}
			}
		}
		return business1Handler;
	}

}
