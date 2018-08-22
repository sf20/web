/**
 * GetPersonInfoServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.shimao;

public interface GetPersonInfoServiceSoap extends java.rmi.Remote {
    public java.lang.String getListCount(java.lang.String dataType, java.lang.String sourceSys) throws java.rmi.RemoteException;
    public java.lang.String getPersonByuserID(java.lang.String userID, java.lang.String userName) throws java.rmi.RemoteException;
    public java.lang.String getCorporateAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException;
    public java.lang.String getObjRelationAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException;
    public java.lang.String getOrganizationAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException;
    public java.lang.String getPersonAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException;
    public java.lang.String getPostAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException;
    public java.lang.String getUserAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException;
}
