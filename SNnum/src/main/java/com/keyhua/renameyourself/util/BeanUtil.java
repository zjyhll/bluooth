package com.keyhua.renameyourself.util;

import java.lang.reflect.Field;

public class BeanUtil {

	public static <T> T parserObjToT(Object obj, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		Class oclz = obj.getClass();
		T t = clazz.newInstance();
		Field[] ofields = oclz.getDeclaredFields();
		Field[] tfields = clazz.getDeclaredFields();
		for (Field of : ofields) {
			of.setAccessible(true);
			String oname = of.getName();
			for (Field tf : tfields) {
				tf.setAccessible(true);
				String tname = tf.getName();
				if (oname.equalsIgnoreCase(tname)) {
					if (of.getType().isAssignableFrom(tf.getType())) {
						tf.set(t, of.get(obj));
						continue;
					}
					continue;
				} else {
					continue;
				}
			}
		}
		return t;
	}
}
