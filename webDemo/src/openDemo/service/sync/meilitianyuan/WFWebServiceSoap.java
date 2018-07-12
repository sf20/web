/**
 * WFWebServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.meilitianyuan;

public interface WFWebServiceSoap extends java.rmi.Remote {
    public java.lang.String helloWorld() throws java.rmi.RemoteException;

    /**
     * 组织架构
     */
    public java.lang.String orgList() throws java.rmi.RemoteException;

    /**
     * 人员信息
     */
    public java.lang.String userList(int pageSize, int currentPage) throws java.rmi.RemoteException;

    /**
     * 门店信息
     */
    public java.lang.String storeList(int pageSize, int currentPage) throws java.rmi.RemoteException;

    /**
     * 产品信息
     */
    public java.lang.String productList(int pageSize, int currentPage, java.lang.String startTime, java.lang.String endTime) throws java.rmi.RemoteException;

    /**
     * 护理信息
     */
    public java.lang.String beautyCareList(int pageSize, int currentPage, java.lang.String startTime, java.lang.String endTime) throws java.rmi.RemoteException;

    /**
     * 疗程信息
     */
    public java.lang.String courseList(int pageSize, int currentPage, java.lang.String startTime, java.lang.String endTime) throws java.rmi.RemoteException;

    /**
     * 活动信息
     */
    public java.lang.String activityList(int pageSize, int currentPage, java.lang.String startTime, java.lang.String endTime) throws java.rmi.RemoteException;

    /**
     * 主数据数据字典
     */
    public java.lang.String subjectsList(int pageSize, int currentPage, java.lang.String parent_Code, java.lang.String ext1) throws java.rmi.RemoteException;
}
