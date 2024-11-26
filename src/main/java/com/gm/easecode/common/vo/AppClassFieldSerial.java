package com.gm.easecode.common.vo;

/**
 * 序列化字段
 * @author GM
 * @date 2024-04-29
 */
public class AppClassFieldSerial extends AppClassFieldList {

	private static final long serialVersionUID = 3731229923616004996L;

	public AppClassFieldSerial() {
		this.setName("serialVersionUID");
		this.setFinal(true);
		this.setStatic(true);
		this.setType("long");
		this.setValue(System.nanoTime() + "L");
	}
}
