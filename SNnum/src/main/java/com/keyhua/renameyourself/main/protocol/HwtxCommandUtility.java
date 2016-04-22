package com.keyhua.renameyourself.main.protocol;

import java.util.IllegalFormatCodePointException;

public class HwtxCommandUtility {

	public HwtxCommandUtility() {
		// TODO Auto-generated constructor stub
	}

	// 将输入的字节流的内容打印出来，方便调试
	public static String bytesToHexText(byte bytes[]) {

		String text = new String();
		String hexLine = "";
		String charLine = "";

		for (Integer i = 0; i < bytes.length; ++i) {
			hexLine += String.format("0x%02X", bytes[i]) + " ";
			try {
				if (bytes[i] < 0x20) {
					// 小于0x20的字符为控制字符，显示为点，方便查看
					charLine += String.format("%c", '.');
				} else {
					charLine += String.format("%c", bytes[i]);
				}
			} catch (IllegalFormatCodePointException e) {
				charLine += String.format("%c", '.');
			}
			if (((0 == (i + 1) % 8) && (i > 0))) {
				String headLine = String.format("0x%04X", i - 7);
				text += headLine + "  " + hexLine + "    " + charLine + "\r\n";
				hexLine = "";
				charLine = "";
			} else if ((i + 1 >= bytes.length)) {
				// 最后一行数据，可能不到8个字符，需要补齐，方便显示
				String headLine = String.format("0x%04X", ((i + 1) / 8) * 8);
				Integer remain = 8 - (i + 1) % 8;
				for (Integer j = 0; j < remain; ++j) {
					// 每一个字符的16进制内容是占位5个字符
					hexLine += "     ";
				}
				text += headLine + "  " + hexLine + "    " + charLine + "\r\n";
				hexLine = "";
				charLine = "";
			}
		}

		return text;
	}

	// 将64位整形数转换为字节流
	public static byte[] int64ToBytes(Long value) {

		byte bytes[] = new byte[8];

		bytes[0] = (byte) (value & 0xFF);
		bytes[1] = (byte) (value >> 8 & 0xFF);
		bytes[2] = (byte) (value >> 16 & 0xFF);
		bytes[3] = (byte) (value >> 24 & 0xFF);
		bytes[4] = (byte) (value >> 32 & 0xFF);
		bytes[5] = (byte) (value >> 40 & 0xFF);
		bytes[6] = (byte) (value >> 48 & 0xFF);
		bytes[7] = (byte) (value >> 56 & 0xFF);

		return bytes;
	}

	// 将Byte数组转换为64位整形，只取Byte数组的前8个字节
	public static Long bytesToInt64(byte bytes[]) {

		Long value = 0L;

		for (int i = 0; (i < 8 && i < bytes.length); ++i) {
			value = value | (((long) bytes[i] & 0x00000000000000FF) << 8 * i);
		}

		return value;
	}

	// 将整形数转换为字节流，注意只取整形数的低32bit，如果是64位整形，则超出部分会被丢掉
	public static byte[] int32ToBytes(Integer value) {

		byte bytes[] = new byte[4];

		bytes[0] = (byte) (value & 0xFF);
		bytes[1] = (byte) (value >> 8 & 0xFF);
		bytes[2] = (byte) (value >> 16 & 0xFF);
		bytes[3] = (byte) (value >> 24 & 0xFF);

		return bytes;
	}

	// 将Byte数组转换为32位整形，只取Byte数组的前4个字节
	public static Integer bytesToInt32(byte bytes[]) {

		Integer value = 0;

		for (int i = 0; (i < 4 && i < bytes.length); ++i) {
			value = value | ((bytes[i] & 0x000000FF) << 8 * i);
		}

		return value;
	}

	// 将整形数转换为字节流，注意只取整形数的低16bit，超出部分会被丢掉
	public static byte[] int16ToBytes(Short value) {

		byte bytes[] = new byte[2];

		bytes[0] = (byte) (value & 0xFF);
		bytes[1] = (byte) (value >> 8 & 0xFF);

		return bytes;
	}

	// 将Byte数组转换为16位整形，只取Byte数组的前2个字节
	public static Short bytesToInt16(byte bytes[]) {

		Integer value = 0;

		for (int i = 0; (i < 2 && i < bytes.length); ++i) {
			value = value | ((bytes[i] & 0x000000FF) << 8 * i);
		}

		return value.shortValue();
	}

	// 将整形数转换为字节流，注意只取整形数的低8bit，超出部分会被丢掉
	public static byte[] int8ToBytes(Byte value) {

		byte bytes[] = new byte[1];

		bytes[0] = (byte) (value & 0xFF);

		return bytes;
	}

	// 将Byte数组转换为8位整形，只取Byte数组的前1个字节
	public static Byte bytesToInt8(byte bytes[]) {

		Integer value = 0;

		for (int i = 0; (i < 1 && i < bytes.length); ++i) {
			value = value | ((bytes[i] & 0x000000FF) << 8 * i);
		}

		return value.byteValue();
	}

	// 将新的字节流（append）附件到已经存在的字节流（original）的后面，生成一个新的字节流
	public static byte[] appendBytesToBytes(byte[] original, byte[] append) {

		Integer lengthOfByte = original.length + append.length;

		byte[] bytes = new byte[lengthOfByte];

		Integer index = 0;

		for (Integer i = 0; i < original.length; ++i, ++index) {
			bytes[index] = original[i];
		}

		for (Integer i = 0; i < append.length; ++i, ++index) {
			bytes[index] = append[i];
		}

		return bytes;
	}

	// 将字节数组 original 从 start 开始的 length个bit位的值设置为 value，如果value的值超出范围则只取到最大值
	public static byte[] setBitsOfBytes(byte[] original, Integer start,
			Integer length, byte[] value) {

		if (start >= (original.length * 8) || start < 0) {
			return null;
		}

		if (length > (original.length * 8) || length <= 0) {
			return null;
		}

		// 保存结果的byte数组，长度与原始输入数组一致
		byte[] bytes = new byte[original.length];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = original[i];
		}

		for (int i = 0, index = start; ((i < length) && (index < original.length * 8)); ++i, ++index) {
			Integer bitValue = extractBitFromBytes(value, i);
			if (null == bitValue) {
				bitValue = 0;
			}
			bytes = setBitOfBytes(bytes, index, bitValue);
		}

		return bytes;
	}

	// 将字节数组 original 的 index 指定的 bit 位设置为 bitValue，bitValue 只能为 0 和 1，任何不为 0
	// 的值都当作 1 处理
	public static byte[] setBitOfBytes(byte[] original, Integer index,
			Integer bitValue) {

		if (index >= (original.length * 8) || index < 0) {
			return null;
		}

		// 单独创建一个字节数组保存结果，不改变输入值
		byte bytes[] = new byte[original.length];

		if (bitValue != 0) {
			bitValue = 1;
		}

		Integer byteIndex = index / 8;
		Integer bitIndex = index % 8;

		Integer byteValue = Integer.valueOf(0x000000FF & original[byteIndex]);
		Integer mask = 0x00000001 << bitIndex;

		byteValue = byteValue & (~mask);
		byteValue = byteValue | (bitValue << bitIndex);

		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = original[i];
		}
		bytes[byteIndex] = byteValue.byteValue();

		return bytes;

	}

	// 从给定的字节流original中截取指定范围的字节流内容并返回（从start开始，取length个字节）
	public static byte[] extractBytesFromBytes(byte[] original, Integer start,
			Integer length) {

		if (start >= original.length || start < 0) {
			return null;
		}

		if (length > original.length || length <= 0) {
			return null;
		}

		byte[] bytes = new byte[length];

		for (int i = 0, index = start; (i < length && index < original.length); ++i, ++index) {
			bytes[i] = original[index];
		}

		return bytes;
	}

	// 从给定的字节流original中截取指定范围的bit内容并返回（从start开始，取length个bit）
	// 注意 bit 和 byte 的关系，1个 byte 包含 8 个 bit 位，计算长度时要注意转换
	public static byte[] extractBitsFromBytes(byte[] original, Integer start,
			Integer length) {

		// 起始bit位超出范围
		if (start >= (original.length * 8) || start < 0) {
			return null;
		}

		// 取值长度超出范围
		if (length > (original.length * 8) || length <= 0) {
			return null;
		}

		// 用于保存截取后的 bit 内容的 byte 数组长度按bit转换为byte并向上取整
		Integer lengthOfByte = (int) Math.ceil(length.floatValue() / 8);

		byte bytes[] = new byte[lengthOfByte];

		for (Integer index = start, i = 0; ((i < length) && (index < (original.length * 8))); ++index, ++i) {
			Integer bitValue = extractBitFromBytes(original, index);
			Integer byteValue = Integer.valueOf(0x000000FF & bytes[i / 8]);
			byteValue = byteValue | (bitValue << (i % 8));
			bytes[i / 8] = (byte) (byteValue & 0x000000FF);
		}

		return bytes;
	}

	// 从给定的字节流original中取index指定位置的bit位的值，返回值只可能是0或者1
	public static Integer extractBitFromBytes(byte[] original, Integer index) {

		Integer bitValue = null;

		if (index >= (original.length * 8) || index < 0) {
			return null;
		}

		Integer byteIndex = index / 8;
		Integer bitIndex = index % 8;

		Integer byteValue = Integer.valueOf(0x000000FF & original[byteIndex]);
		Integer mask = 0x00000001 << bitIndex;
		bitValue = (byteValue & mask) >> bitIndex;

		return bitValue;

	}

	// 复制源字节数组 source 的内容并生成一个 targetLength 长度的目的字节数组
	// 如果源字节数组的长度大于 targetLength ，则只取 source 的前 targetLength 长度的内容
	// 如果源字节数组的长度小于 targetLength ，则生成的目的字节数组的不足部分填充为 0x0
	public static byte[] copyBytes(byte[] source, Integer targetLength) {

		if (targetLength <= 0) {
			return null;
		}

		byte target[] = new byte[targetLength];

		Integer sourceLength = source.length;

		if (sourceLength > targetLength) {
			target = HwtxCommandUtility.extractBytesFromBytes(source, 0,
					targetLength);
		} else {
			target = HwtxCommandUtility.setBitsOfBytes(target, 0,
					source.length * 8, source);
		}

		return target;
	}

	// 将输入的字节流像左移位 length 长度并返回结果
	// TODO: 目前的实现是不正确的
	public static byte[] leftShiftBytes(byte[] original, Integer length) {

		if (length > (original.length * 8) || length < 0) {
			return null;
		}

		// 向左移位，移位后的字节流长度与输入字节流相同
		byte[] tempAfterShift = new byte[original.length];
		byte[] bytesAfterShift = new byte[original.length];

		// 如果移动的bit长度超过1个byte（8bit），先按bit移位所有字节，再按字节移位
		// 需要向左移动的bit长度
		int shiftBitsLength = (length % 8);
		for (int i = 0; i < original.length; ++i) {
			Integer byteToShift = 0x000000FF & original[i];
			byte byteAfterShift = Integer.valueOf(
					Integer.rotateLeft(byteToShift, shiftBitsLength))
					.byteValue();
			tempAfterShift[i] = byteAfterShift;
		}

		System.out.println(Integer.toBinaryString(0xFFFF & HwtxCommandUtility
				.bytesToInt16(tempAfterShift)));

		// 需要向左移动的byte长度
		int shiftBytesLength = (length / 8);
		if (shiftBytesLength > 0) {
			for (int i = 0; i < shiftBytesLength; ++i) {
				bytesAfterShift[i + shiftBytesLength] = tempAfterShift[i];
			}
		} else {
			bytesAfterShift = tempAfterShift;
		}

		return bytesAfterShift;
	}

	// Java没有无符号数，Short可能为负数，调试时以16进制的方式显示
	public static Short crc16(byte[] buf, Integer length) {

		Integer i = 0;
		Integer j = 0;

		// unsigned short
		Short crcReg = 0;
		// unsigned char
		byte index = 0;
		// unsigned short
		Short toXor = 0;

		for (i = 0; i < length; i++) {
			index = (byte) ((crcReg ^ buf[i]) & 0xFF);
			toXor = (short) (index & 0xFF);

			for (j = 0; j < 8; j++) {
				if ((toXor & 0x0001) != 0) {
					toXor = (short) ((((toXor >> 1) & 0x7FFF) ^ 0x8408) & 0xFFFF); // 0x1021-LSB倒序
				} else {
					toXor = (short) ((toXor >> 1) & 0x7FFF);
				}
			}
			crcReg = (short) ((((crcReg >> 8) & 0xFF) ^ toXor) & 0xFFFF);
		}

		return crcReg;
	}

}
