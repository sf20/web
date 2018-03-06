package openDemo.service.sync.xnjz;

public class ISysSynchroGetOrgWebServiceProxy implements openDemo.service.sync.xnjz.ISysSynchroGetOrgWebService {
  private String _endpoint = null;
  private openDemo.service.sync.xnjz.ISysSynchroGetOrgWebService iSysSynchroGetOrgWebService = null;
  
  public ISysSynchroGetOrgWebServiceProxy() {
    _initISysSynchroGetOrgWebServiceProxy();
  }
  
  public ISysSynchroGetOrgWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initISysSynchroGetOrgWebServiceProxy();
  }
  
  private void _initISysSynchroGetOrgWebServiceProxy() {
    try {
      iSysSynchroGetOrgWebService = (new openDemo.service.sync.xnjz.ISysSynchroGetOrgWebServiceServiceLocator()).getISysSynchroGetOrgWebServicePort();
      if (iSysSynchroGetOrgWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iSysSynchroGetOrgWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iSysSynchroGetOrgWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iSysSynchroGetOrgWebService != null)
      ((javax.xml.rpc.Stub)iSysSynchroGetOrgWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public openDemo.service.sync.xnjz.ISysSynchroGetOrgWebService getISysSynchroGetOrgWebService() {
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService;
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgTokenResult getUpdatedElementsByToken(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoTokenContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getUpdatedElementsByToken(arg0);
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleConfCateInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleConfCateInfo(arg0);
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgResult getUpdatedElements(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getUpdatedElements(arg0);
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleConfInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleConfInfo(arg0);
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgResult getElementsBaseInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgBaseInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getElementsBaseInfo(arg0);
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleInfo(arg0);
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleLineDefaultRoleInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleLineDefaultRoleInfo(arg0);
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleLineInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleLineInfo(arg0);
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgResult getOrgGroupCateInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getOrgGroupCateInfo(arg0);
  }
  
  public openDemo.service.sync.xnjz.SysSynchroOrgResult getOrgStaffingLevelInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getOrgStaffingLevelInfo(arg0);
  }
  
  
}