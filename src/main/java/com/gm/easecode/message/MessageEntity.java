package com.gm.easecode.message;

import com.alibaba.fastjson.JSONObject;

public class MessageEntity
{
    /** 消息类型，1：日志，2：进度 */
    public int msgType=1;
	public String msg;
	public boolean isRoot = false;
	public boolean isEnd = false;
	/** 是否是错误消息 */
	public boolean isError = false;
	/** 是否结束打包 */
	public boolean isAllEnd = false;
	/** JSON数据串 */
	public Object data;
	/** 消息归属任务ID */
    public Long taskId;
	
	public MessageEntity(){}
	/** 发送进度消息 */
	public MessageEntity(Object data) {
	    this.msgType = 2;
	    this.data = data;
	}
	public MessageEntity (String msg, int progress) {
	    this.msgType = 2;
	    JSONObject dataJson = new JSONObject();
        dataJson.put("progress", progress);
        dataJson.put("title", msg);
        this.msg = msg;
        this.data = dataJson;
	}
	public MessageEntity (String msg, int progress, int status, String statusStr, String fileName, String filePath) {
	    this.msgType = 2;
	    JSONObject dataJson = new JSONObject();
	    dataJson.put("progress", progress);
	    dataJson.put("title", msg);
	    dataJson.put("status", status);
	    dataJson.put("statusStr", statusStr);
	    dataJson.put("fileName", fileName);
	    dataJson.put("filePath", filePath);
	    this.data = dataJson;
	}
	public MessageEntity(String msg)
	{
		this.isRoot = false;
		this.isEnd = false;
		this.msg = msg;
	}
	public MessageEntity(String msg, boolean isError)
	{
		this.isRoot = false;
		this.isEnd = false;
		this.msg = msg;
		this.isError = isError;
	}
	public MessageEntity(String msg, boolean isRoot, boolean isEnd)
	{
		this.isRoot = isRoot;
		this.isEnd = isEnd;
		this.msg = msg;
	}
	public MessageEntity(String msg, boolean isRoot, boolean isEnd, boolean isError)
	{
		this.isRoot = isRoot;
		this.isEnd = isEnd;
		this.isError = isError;
		this.msg = msg;
	}
	public MessageEntity(String msg, boolean isRoot, boolean isEnd, String data)
	{
		this.isRoot = isRoot;
		this.isEnd = isEnd;
		this.msg = msg;
		this.data = data;
	}
	public MessageEntity(String msg, boolean isRoot, boolean isEnd, boolean isAllEnd, String data)
	{
		this.isRoot = isRoot;
		this.isEnd = isEnd;
		this.isAllEnd = isAllEnd;
		this.msg = msg;
		this.data = data;
	}
}
