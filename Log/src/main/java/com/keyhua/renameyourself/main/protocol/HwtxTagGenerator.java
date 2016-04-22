package com.keyhua.renameyourself.main.protocol;

import java.util.Calendar;

public class HwtxTagGenerator {

	private Integer lastSeed = 0;

	private Integer counter = 0;

	private static HwtxTagGenerator instance = null;

	public static synchronized HwtxTagGenerator getInstance() {
		if (null == instance) {
			instance = new HwtxTagGenerator();
		}
		return instance;
	}

	private HwtxTagGenerator() {
		// TODO Auto-generated constructor stub
	}

	// 生成一个32位，4字节的tag
	public synchronized byte[] generate() {

		Calendar cal = Calendar.getInstance();
		Long time = cal.getTimeInMillis();
		Integer seed = time.intValue() & 0x7FFFFFFF;
		if (lastSeed == seed) {
			++counter;
		} else {
			counter = 0;
		}

		lastSeed = (seed + counter) & 0x7FFFFFFF;

		byte[] tag = HwtxCommandUtility.int32ToBytes(lastSeed);

		return tag;

	}

}
