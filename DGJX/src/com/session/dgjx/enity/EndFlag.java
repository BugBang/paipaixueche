package com.session.dgjx.enity;


public enum EndFlag
{
	//0表示结束，没有下一页，1表示还有下一页
	Flag_0(0),Flag_1(1);
	
	private final int value;
	
	private EndFlag(int value)
	{
		this.value = value;
	}
	public int getValue()
	{
		return value;
	}
	
}
