package com.session.common;

/**
 * 查询列表接口封装类的积累
 *
 * @author YJ Liang
 *         2016  下午4:44:55
 */
public abstract class BaseListRequestData extends BaseRequestData {
    private static final long serialVersionUID = 1L;

    protected String lastRecordValue = null;//最后一条记录的值，第一次查询，lastRecordValue的值为null
    protected int pageSize = 30;//默认每页获取30条

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getLastRecordValue() {
        return lastRecordValue;
    }

    public void setLastRecordValue(String lastRecordValue) {
        this.lastRecordValue = lastRecordValue;
    }

}
