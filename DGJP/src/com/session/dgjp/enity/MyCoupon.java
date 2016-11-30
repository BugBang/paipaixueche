package com.session.dgjp.enity;


public class MyCoupon {

	/** Y,未使用{@link #useable} */
	public final static String USEABLE_Y = "Y";
	private long id; // 学生所属优惠卷id
	private long couponId; // 优惠券id（区分学生所属优惠卷id）
	private String couponName; // 优惠券名称
	private String couponTitle; // 优惠券标题
	private int discountPrice; // 优惠金额,单位为分
	private String expiryType; // 有效期类型,T:指定时间,D:指定天数,F:永久
	private String expiryValue; // 有效期值:当type=T时,为时间,type=D时,为天数,type=F时,为空
	private int useCondition; // 使用条件,满金额数,单位为分
	private boolean issup; // 是否可以叠加使用(预留)
	private String couponRemark; // 优惠券描述
	private long styleId; // 样式ID
	private String putOutType; // 发放形式,S:自行领取,D:充值;R:注册
	private String putOutTypeName; // 发放形式名称
	private int payFeeTotal; // 充值满此数值时,发放优惠券,只有当.put_out_type为D存入数据
	private String isallscope; // 是否全部适用;Y:全部,N:非全部
	private String couponCode; // 优惠券编码(预留)
	private String receiveType; // 领取的类型:S:自行领取,P:为发放
	private String receiveTypeName; // 领取的类型名称
	private String useable; // 是否可用,Y:未使用;N:已使用
	private String receiveTime; // 领取时间
	private String expiryTime; // 失效时间
	private String styleName; // 样式名称(预留)
	private String background; // 背景图片地址
	private String backgroundColor; // 优惠券背景色(预留)
	private String titleFontColor; // 优惠券标题字体颜色(预留)
	private int titleFontSize; // 优惠券标题字体大小(预留)
	private String countFontColor; // 优惠券内容字体颜色(预留)
	private int countFontSize; // 优惠券内容字体大小(预留)
	private String footFontColor; // 优惠券底部字体颜色(预留)
	private int footFontSize; // 优惠券底部字体大小(预留)

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponTitle() {
		return couponTitle;
	}

	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle;
	}

	public int getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(int discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getExpiryType() {
		return expiryType;
	}

	public void setExpiryType(String expiryType) {
		this.expiryType = expiryType;
	}

	public String getExpiryValue() {
		return expiryValue;
	}

	public void setExpiryValue(String expiryValue) {
		this.expiryValue = expiryValue;
	}

	public int getUseCondition() {
		return useCondition;
	}

	public void setUseCondition(int useCondition) {
		this.useCondition = useCondition;
	}

	public boolean isIssup() {
		return issup;
	}

	public void setIssup(boolean issup) {
		this.issup = issup;
	}

	public String getCouponRemark() {
		return couponRemark;
	}

	public void setCouponRemark(String couponRemark) {
		this.couponRemark = couponRemark;
	}

	public long getStyleId() {
		return styleId;
	}

	public void setStyleId(long styleId) {
		this.styleId = styleId;
	}

	public String getPutOutType() {
		return putOutType;
	}

	public void setPutOutType(String putOutType) {
		this.putOutType = putOutType;
	}

	public String getPutOutTypeName() {
		return putOutTypeName;
	}

	public void setPutOutTypeName(String putOutTypeName) {
		this.putOutTypeName = putOutTypeName;
	}

	public int getPayFeeTotal() {
		return payFeeTotal;
	}

	public void setPayFeeTotal(int payFeeTotal) {
		this.payFeeTotal = payFeeTotal;
	}

	public String getIsallscope() {
		return isallscope;
	}

	public void setIsallscope(String isallscope) {
		this.isallscope = isallscope;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getReceiveTypeName() {
		return receiveTypeName;
	}

	public void setReceiveTypeName(String receiveTypeName) {
		this.receiveTypeName = receiveTypeName;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getTitleFontColor() {
		return titleFontColor;
	}

	public void setTitleFontColor(String titleFontColor) {
		this.titleFontColor = titleFontColor;
	}

	public int getTitleFontSize() {
		return titleFontSize;
	}

	public void setTitleFontSize(int titleFontSize) {
		this.titleFontSize = titleFontSize;
	}

	public String getCountFontColor() {
		return countFontColor;
	}

	public void setCountFontColor(String countFontColor) {
		this.countFontColor = countFontColor;
	}

	public int getCountFontSize() {
		return countFontSize;
	}

	public void setCountFontSize(int countFontSize) {
		this.countFontSize = countFontSize;
	}

	public String getFootFontColor() {
		return footFontColor;
	}

	public void setFootFontColor(String footFontColor) {
		this.footFontColor = footFontColor;
	}

	public int getFootFontSize() {
		return footFontSize;
	}

	public void setFootFontSize(int footFontSize) {
		this.footFontSize = footFontSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyCoupon other = (MyCoupon) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
