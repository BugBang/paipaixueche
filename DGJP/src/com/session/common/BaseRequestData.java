package com.session.common;

import java.io.Serializable;





import com.session.common.utils.LogUtil;
import com.session.dgjp.AppInstance;
import com.session.dgjp.Constants;
import com.session.dgjp.enity.Account;

/**
 * 查询接口封装类的基类
 * @author YJ Liang
 * 2016  下午4:45:22
 */
public abstract class BaseRequestData implements Serializable
{

	private final static String TAG = BaseRequestData.class.getSimpleName();
	
	private static final long serialVersionUID = 1L;
	
	protected transient String url;
	
	private final String accountType = "S";
	
	private String account;
	

	public BaseRequestData()
	{
		super();
		url = Constants.URL+getSpecificUrlPath()+".flow";
		Account account = AppInstance.getInstance().getAccount();
		if(account != null)
		{
			this.account = account.getAccount();
		}
	}
	
	/**
	 * 子类重写该方法，获取接口的详细地址
	 * @author YJ Liang
	 * 2016  下午4:25:44
	 * @return
	 */
	protected abstract String getSpecificUrlPath();

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getAccountType()
	{
		return accountType;
	}


	public String getUrl()
	{
		return url;
	}
	
}
