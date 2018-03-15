package openDemo.service.common.landray.oa;

public class ISysSynchroGetOrgWebServiceProxy implements openDemo.service.common.landray.oa.ISysSynchroGetOrgWebService {
  private String _endpoint = null;
  private openDemo.service.common.landray.oa.ISysSynchroGetOrgWebService iSysSynchroGetOrgWebService = null;
  
  public ISysSynchroGetOrgWebServiceProxy() {
    _initISysSynchroGetOrgWebServiceProxy();
  }
  
  public ISysSynchroGetOrgWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initISysSynchroGetOrgWebServiceProxy();
  }
  
  private void _initISysSynchroGetOrgWebServiceProxy() {
    try {
      iSysSynchroGetOrgWebService = (new openDemo.service.common.landray.oa.ISysSynchroGetOrgWebServiceServiceLocator()).getISysSynchroGetOrgWebServicePort();
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
  
  public openDemo.service.common.landray.oa.ISysSynchroGetOrgWebService getISysSynchroGetOrgWebService() {
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService;
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgTokenResult getUpdatedElementsByToken(openDemo.service.common.landray.oa.SysSynchroGetOrgInfoTokenContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getUpdatedElementsByToken(arg0);
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgResult getRoleConfCateInfo(openDemo.service.common.landray.oa.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleConfCateInfo(arg0);
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgResult getUpdatedElements(openDemo.service.common.landray.oa.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getUpdatedElements(arg0);
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgResult getRoleConfInfo(openDemo.service.common.landray.oa.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleConfInfo(arg0);
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgResult getElementsBaseInfo(openDemo.service.common.landray.oa.SysSynchroGetOrgBaseInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getElementsBaseInfo(arg0);
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgResult getRoleInfo(openDemo.service.common.landray.oa.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleInfo(arg0);
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgResult getRoleLineDefaultRoleInfo(openDemo.service.common.landray.oa.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleLineDefaultRoleInfo(arg0);
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgResult getRoleLineInfo(openDemo.service.common.landray.oa.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getRoleLineInfo(arg0);
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgResult getOrgGroupCateInfo(openDemo.service.common.landray.oa.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getOrgGroupCateInfo(arg0);
  }
  
  public openDemo.service.common.landray.oa.SysSynchroOrgResult getOrgStaffingLevelInfo(openDemo.service.common.landray.oa.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.common.landray.oa.Exception{
    if (iSysSynchroGetOrgWebService == null)
      _initISysSynchroGetOrgWebServiceProxy();
    return iSysSynchroGetOrgWebService.getOrgStaffingLevelInfo(arg0);
  }
  
  
}