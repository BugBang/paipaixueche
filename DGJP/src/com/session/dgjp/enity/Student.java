package com.session.dgjp.enity;

import java.io.Serializable;

public class Student implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 学员账号 */
	private String account;
	/** 姓名 */
	private String name;
	/** 当前培训进度。K1科目一，K2科目二，K3科目三，K4科目四 */
	private String progress;
	/** 手机号码 */
	private String phone;
	/** 身份证号码 */
	private String idcard;
	/** 住址 */
	private String address;
	/** 报名分校ID */
	private long branchSchoolId;
	/** 报名分校 */
	private String branchSchoolName;
	/** 驾校名称 */
	private String driverSchoolName;
	/**分校电话*/
	private String branchSchoolPhone;
	/**分校图片*/
	private String branchSchoolPhotoUrl;
	/**分校缩略图*/
	private String branchSchoolSmallPhotoUrl;
	/**分校地址*/
	private String branchSchoolAddress;
	/**分校经度*/
	private double branchSchoolLongitude;
	/**分校纬度*/
	private double branchSchoolLatitude;
	private int trainerTotal = 0;
	/** 报名时间 */
	private String applyTime;
	/** 钱包余额。单位分 */
	private int balance;
	/** 头像地址 */
	private String photoUrl;
	/** 是否设置了支付密码。Y是，N否 */
	private String isSetPayPwd;
	/** 最近登录时间，若为空，则表示是首次登录 */
	private String lastLoginTime;

	/**学员所在分校*/
	private BranchSchool branchSchool;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getBranchSchoolId() {
		return branchSchoolId;
	}

	public void setBranchSchoolId(long branchSchoolId) {
		this.branchSchoolId = branchSchoolId;
	}

	public String getBranchSchoolName() {
		return branchSchoolName;
	}

	public void setBranchSchoolName(String branchSchoolName) {
		this.branchSchoolName = branchSchoolName;
	}

	public String getDriverSchoolName() {
		return driverSchoolName;
	}

	public void setDriverSchoolName(String driverSchoolName) {
		this.driverSchoolName = driverSchoolName;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getIsSetPayPwd() {
		return isSetPayPwd;
	}

	public void setIsSetPayPwd(String isSetPayPwd) {
		this.isSetPayPwd = isSetPayPwd;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public BranchSchool getBranchSchool()
	{
		return branchSchool;
	}

	public void setBranchSchool(BranchSchool branchSchool)
	{
		this.branchSchool = branchSchool;
	}

	public String getBranchSchoolPhone()
	{
		return branchSchoolPhone;
	}

	public void setBranchSchoolPhone(String branchSchoolPhone)
	{
		this.branchSchoolPhone = branchSchoolPhone;
	}

	public String getBranchSchoolPhotoUrl()
	{
		return branchSchoolPhotoUrl;
	}

	public void setBranchSchoolPhotoUrl(String branchSchoolPhotoUrl)
	{
		this.branchSchoolPhotoUrl = branchSchoolPhotoUrl;
	}

	public String getBranchSchoolSmallPhotoUrl()
	{
		return branchSchoolSmallPhotoUrl;
	}

	public void setBranchSchoolSmallPhotoUrl(String branchSchoolSmallPhotoUrl)
	{
		this.branchSchoolSmallPhotoUrl = branchSchoolSmallPhotoUrl;
	}

	public String getBranchSchoolAddress()
	{
		return branchSchoolAddress;
	}

	public void setBranchSchoolAddress(String branchSchoolAddress)
	{
		this.branchSchoolAddress = branchSchoolAddress;
	}

	public double getBranchSchoolLongitude()
	{
		return branchSchoolLongitude;
	}

	public void setBranchSchoolLongitude(double branchSchoolLongitude)
	{
		this.branchSchoolLongitude = branchSchoolLongitude;
	}

	public double getBranchSchoolLatitude()
	{
		return branchSchoolLatitude;
	}

	public void setBranchSchoolLatitude(double branchSchoolLatitude)
	{
		this.branchSchoolLatitude = branchSchoolLatitude;
	}

	public int getTrainerTotal()
	{
		return trainerTotal;
	}
	
}
