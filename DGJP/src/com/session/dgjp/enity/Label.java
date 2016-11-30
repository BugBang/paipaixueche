package com.session.dgjp.enity;

public class Label
{
	/**{@link #status} 正常使用*/
	//public final static char STATUS_NORMAL = 'Y';
	/**{@link #status} 已禁用*/
	//public final static char STATUS_FORBIDDEN = 'B';
	/**{@link #status} 已失效或其它异常*/
	//public final static char STATUS_FAIL = 'F';
	
	private long id;
	private int score;
	private String name;
	//private char status;
	
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public int getScore()
	{
		return score;
	}
	public void setScore(int score)
	{
		this.score = score;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
}
