package com.session.dgjp.enity;

import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.CoordinateConverter.CoordType;
import com.amap.api.maps2d.model.LatLng;

import java.io.Serializable;
import java.util.Date;

public class BranchSchool implements Serializable, Comparable<BranchSchool> {
    private static final long serialVersionUID = 1L;

    /**
     * 报名分校 {@link #branchSchoolType}
     */
    public final static String TYPE_APPLY = "1";
    /**
     * 最近预约的分校 {@link #branchSchoolType}
     */
    public final static String TYPE_LATEST = "2";
    /**
     * 其它分校 {@link #branchSchoolType}
     */
    public final static String TYPE_OTHER = "";

    public final static String ID = "id";

    /**
     * 分校 {@link #type}}
     */
    public final static String TYPE_SCHOOL = "1";
    /**
     * 分店 {@link #type}}
     */
    public final static String TYPE_STORE = "2";

    private static CoordinateConverter converter;

    private long id;
    private String name;// 分校名称
    private String remark;
    private char status;
    private String phone;
    private String linkman;
    private String email;
    private String driverSchoolName;// 驾校名称
    private int timeProid;
    private Date createTime;
    private Date updateTime;
    private transient double distance = -1;// 与驾校的距离，单位km
    private transient LatLng latLng;//分校的经纬度（对应高德地图的坐标系）

    private String address;
    private double longitude;
    private double latitude;

    private String photoUrl;
    private String smallPhotoUrl;
    private String branchSchoolType;

    private transient String ellipsisAddress;
    private transient String ellipsisPhone;
    private int trainerTotal = 0;//分校的教练人数
    private String type;//类型
    private String typeName;//类型名称

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDriverSchoolName() {
        return driverSchoolName;
    }

    public void setDriverSchoolName(String driverSchoolName) {
        this.driverSchoolName = driverSchoolName;
    }

    public int getTimeProid() {
        return timeProid;
    }

    public void setTimeProid(int timeProid) {
        this.timeProid = timeProid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBranchSchoolType() {
        return branchSchoolType;
    }

    public void setBranchSchoolType(String branchSchoolType) {
        this.branchSchoolType = branchSchoolType;
    }

    public String getSmallPhotoUrl() {
        return smallPhotoUrl;
    }

    public void setSmallPhotoUrl(String smallPhotoUrl) {
        this.smallPhotoUrl = smallPhotoUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
        BranchSchool other = (BranchSchool) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public String getEllipsisAddress() {
        if (ellipsisAddress != null) {
            return ellipsisAddress;
        } else {
            if (address != null) {
                if (address.length() > 13) {
                    ellipsisAddress = address.substring(0, 13) + "...";
                } else {
                    ellipsisAddress = address;
                }
                return ellipsisAddress;
            } else {
                return address;
            }
        }
    }

    public String getEllipsisPhone() {
        if (ellipsisPhone != null) {
            return ellipsisPhone;
        } else {
            if (phone != null) {
                String[] arr = phone.split("、");
                if (arr.length > 2) {
                    ellipsisPhone = arr[0] + "、" + arr[1] + "...";
                } else {
                    ellipsisPhone = phone;
                }
                return ellipsisPhone;
            } else {
                return phone;
            }

        }
    }

    public LatLng getLatLng() {
        if (latLng == null) {
            if (converter == null) {
                converter = new CoordinateConverter();
                converter.from(CoordType.GPS);
            }
            if (latitude > 0 && longitude > 0) {
                converter.coord(new LatLng(latitude, longitude));
                latLng = converter.convert();
            }
        }
        return latLng;
    }

    public int getTrainerTotal() {
        return trainerTotal;
    }

    public void setTrainerTotal(int trainerTotal) {
        this.trainerTotal = trainerTotal;
    }

    @Override
    public int compareTo(BranchSchool another) {
        if (distance > another.getDistance()) {
            return 1;
        } else if (distance < another.getDistance()) {
            return -1;
        } else {
            return 0;
        }
    }

}
