package com.keyhua.renameyourself.main.le;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 曾金叶
 * @2015-10-28
 * @下午5:36:31 byte数组合并
 */
public class ByteUtil {
	/**
	 * 系统提供的数组拷贝方法arraycopy
	 * */
	public static byte[] sysCopy(List<byte[]> srcArrays) {
		int len = 0;
		for (byte[] srcArray : srcArrays) {
			len += srcArray.length;
		}
		byte[] destArray = new byte[len];
		int destLen = 0;
		for (byte[] srcArray : srcArrays) {
			System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
			destLen += srcArray.length;
		}
		return destArray;
	}

	/**
	 * 借助字节输出流ByteArrayOutputStream来实现字节数组的合并
	 * */
	public static byte[] streamCopy(List<byte[]> srcArrays) {
		byte[] destAray = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			for (byte[] srcArray : srcArrays) {
				bos.write(srcArray);
			}
			bos.flush();
			destAray = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
			}
		}
		return destAray;
	}

}