package com.gm.easecode.common.vo;

import java.util.ArrayList;
import java.util.List;

public class AppClassMethodMain extends AppClassMethodList {

	private static final long serialVersionUID = 380985856082894430L;
	
	public AppClassMethodMain(Object body) {
		super("public", true, false, "main", "void", null, null);
		List<AppClassMethodParam> params = new ArrayList<>();
		AppClassMethodParam param = new AppClassMethodParam("args", "String[]");
		params.add(param);
		super.setParams(params);
		super.setBody(body);
	}

}
