package com.session.dgjp.enity;

public class PayType
{
	public final static String ALIPAY = "ALIPAY";
	public final static String WEIXIN = "WEIXIN";
	public final static String YUE = "YUE";
	
	private String id;
	private String payTypeName;
	private int balance;//余额，使用之前调用接口查询
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getPayTypeName()
	{
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName)
	{
		this.payTypeName = payTypeName;
	}
	public int getBalance()
	{
		return balance;
	}
	public void setBalance(int balance)
	{
		this.balance = balance;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PayType other = (PayType) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
