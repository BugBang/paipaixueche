package com.session.dgjp.enity;

public enum Course
{
	K1("K1","科目一"),K2("K2","科目二"),K3("K3","科目三"),K4("K4","科目四");
	private final String code;
	private final String name;
	
	private Course(String code, String name)
	{
		this.code = code;
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
