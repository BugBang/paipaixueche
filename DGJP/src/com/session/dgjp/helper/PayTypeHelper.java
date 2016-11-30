package com.session.dgjp.helper;

import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.dgjp.request.PayTypeListRequestData;
import com.session.dgjp.response.PayTypeListResponseData;

public class PayTypeHelper
{
	private static BaseResponse<PayTypeListResponseData> baseResponse;
	
	/**
	 * 获取用户的支付方式，改方法需要在子线程中执行
	 * @author YJ Liang
	 * 2016  下午2:05:22
	 * @return
	 * @throws Exception
	 */
	public synchronized static BaseResponse<PayTypeListResponseData> getPayTypesSync() throws Exception
	{
		if (baseResponse == null || baseResponse.getResponseData() == null || baseResponse.getResponseData().getList() == null)
		{
			PayTypeListRequestData requestData = new PayTypeListRequestData();
			BaseRequest request = new BaseRequest(requestData);
			baseResponse = request.sendRequest(PayTypeListResponseData.class);
		}
		return baseResponse;
	}
	
	public static void clearCache()
	{
		synchronized (PayTypeHelper.class)
		{
			baseResponse = null;
		}
	}
}
