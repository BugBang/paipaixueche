package com.session.dgjp.enity;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/** 我的消息 */
@DatabaseTable(tableName="my_message")
public class MyMessage implements Serializable {
	private static final long serialVersionUID = 2182893810841192318L;
	
	public final static String TYPE_ORDER = "O";
	public final static String TYPE_CANCEL = "C";
	public final static String TYPE_START_SIGN = "I";
	public final static String TYPE_END_SIGN = "E";
	public final static String ACCOUNT = "account";
	public final static String SEND_TIME = "send_time";
	public final static String MSG_TYPE = "msg_type";
	public final static String READED = "readed";
	
	@DatabaseField(generatedId=true)
	private long id = 0;//消息id
	@DatabaseField(columnName=SEND_TIME)
	private long sendTime;//发送时间,1970.1.1到发送时的毫秒数
	@DatabaseField
	private String content;//消息内容
	@DatabaseField
	private String title;//消息标题
	@DatabaseField
	private String extras;//其它附加内容
	@DatabaseField
	private String account;//消息所属用户
	@DatabaseField(columnName=MSG_TYPE)
	private String msgType;//消息类型
	@DatabaseField(columnName=READED)
	private boolean readed=false;//是否已读,true表示已读，false表示未读
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(long sendTime)
	{
		this.sendTime = sendTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExtras()
	{
		return extras;
	}

	public void setExtras(String extras)
	{
		this.extras = extras;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getMsgType()
	{
		return msgType;
	}

	public void setMsgType(String msgType)
	{
		this.msgType = msgType;
	}

	public boolean isReaded()
	{
		return readed;
	}

	public void setReaded(boolean readed)
	{
		this.readed = readed;
	}
	
}
