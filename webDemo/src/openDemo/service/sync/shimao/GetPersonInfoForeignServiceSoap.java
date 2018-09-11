/**
 * GetPersonInfoForeignServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.shimao;

public interface GetPersonInfoForeignServiceSoap extends java.rmi.Remote {
    public java.lang.String getObjRelationAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException;
    public java.lang.String getPersonAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException;
}
