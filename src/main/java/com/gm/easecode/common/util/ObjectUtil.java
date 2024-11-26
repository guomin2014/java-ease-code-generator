package com.gm.easecode.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtil {

	/**
	 * 克隆对象（深拷贝）
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T source) {
//		String json = JSON.toJSONString(source);
//		return (T)JSON.parseObject(json, source.getClass());
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			//将对象序列化成流
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(source);
			//将流反序列化成对象
			bais = new ByteArrayInputStream(baos.toByteArray());
			ois = new ObjectInputStream(bais);
			return (T)ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (Exception e) {}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (Exception e) {}
			}
			if (bais != null) {
				try {
					bais.close();
				} catch (Exception e) {}
			}
		}
		return null;
	}
}
