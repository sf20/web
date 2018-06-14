/**
 * LBEBusinessService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.jomoows;

public interface LBEBusinessService extends java.rmi.Remote {
    public openDemo.service.sync.jomoows.LogoutResult logout(java.lang.String sessionId) throws java.rmi.RemoteException;
    public openDemo.service.sync.jomoows.QueryResult query(java.lang.String sessionId, java.lang.String objectName, openDemo.service.sync.jomoows.LbParameter[] params, java.lang.String condition, openDemo.service.sync.jomoows.QueryOption queryOption) throws java.rmi.RemoteException;
    public openDemo.service.sync.jomoows.LoginResult login(java.lang.String userid, java.lang.String password, java.lang.String scheme, java.lang.String algorithm, java.lang.String securityCode) throws java.rmi.RemoteException;
}
