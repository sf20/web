package openDemo.service.sync.jomoows;

public class LBEBusinessServiceProxy implements openDemo.service.sync.jomoows.LBEBusinessService {
  private String _endpoint = null;
  private openDemo.service.sync.jomoows.LBEBusinessService lBEBusinessService = null;
  
  public LBEBusinessServiceProxy() {
    _initLBEBusinessServiceProxy();
  }
  
  public LBEBusinessServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initLBEBusinessServiceProxy();
  }
  
  private void _initLBEBusinessServiceProxy() {
    try {
      lBEBusinessService = (new openDemo.service.sync.jomoows.LBEBusinessWebServiceLocator()).getLBEBusinessServiceImplPort();
      if (lBEBusinessService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)lBEBusinessService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)lBEBusinessService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (lBEBusinessService != null)
      ((javax.xml.rpc.Stub)lBEBusinessService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public openDemo.service.sync.jomoows.LBEBusinessService getLBEBusinessService() {
    if (lBEBusinessService == null)
      _initLBEBusinessServiceProxy();
    return lBEBusinessService;
  }
  
  public openDemo.service.sync.jomoows.LogoutResult logout(java.lang.String sessionId) throws java.rmi.RemoteException{
    if (lBEBusinessService == null)
      _initLBEBusinessServiceProxy();
    return lBEBusinessService.logout(sessionId);
  }
  
  public openDemo.service.sync.jomoows.QueryResult query(java.lang.String sessionId, java.lang.String objectName, openDemo.service.sync.jomoows.LbParameter[] params, java.lang.String condition, openDemo.service.sync.jomoows.QueryOption queryOption) throws java.rmi.RemoteException{
    if (lBEBusinessService == null)
      _initLBEBusinessServiceProxy();
    return lBEBusinessService.query(sessionId, objectName, params, condition, queryOption);
  }
  
  public openDemo.service.sync.jomoows.LoginResult login(java.lang.String userid, java.lang.String password, java.lang.String scheme, java.lang.String algorithm, java.lang.String securityCode) throws java.rmi.RemoteException{
    if (lBEBusinessService == null)
      _initLBEBusinessServiceProxy();
    return lBEBusinessService.login(userid, password, scheme, algorithm, securityCode);
  }
  
  
}