package openDemo.service.sync.shimao;

public class GetPersonInfoServiceSoapProxy implements GetPersonInfoServiceSoap {
  private String _endpoint = null;
  private GetPersonInfoServiceSoap getPersonInfoServiceSoap = null;
  
  public GetPersonInfoServiceSoapProxy() {
    _initGetPersonInfoServiceSoapProxy();
  }
  
  public GetPersonInfoServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initGetPersonInfoServiceSoapProxy();
  }
  
  private void _initGetPersonInfoServiceSoapProxy() {
    try {
      getPersonInfoServiceSoap = (new GetPersonInfoServiceLocator()).getGetPersonInfoServiceSoap();
      if (getPersonInfoServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)getPersonInfoServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)getPersonInfoServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (getPersonInfoServiceSoap != null)
      ((javax.xml.rpc.Stub)getPersonInfoServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public GetPersonInfoServiceSoap getGetPersonInfoServiceSoap() {
    if (getPersonInfoServiceSoap == null)
      _initGetPersonInfoServiceSoapProxy();
    return getPersonInfoServiceSoap;
  }
  
  public java.lang.String getListCount(java.lang.String dataType, java.lang.String sourceSys) throws java.rmi.RemoteException{
    if (getPersonInfoServiceSoap == null)
      _initGetPersonInfoServiceSoapProxy();
    return getPersonInfoServiceSoap.getListCount(dataType, sourceSys);
  }
  
  public java.lang.String getPersonByuserID(java.lang.String userID, java.lang.String userName) throws java.rmi.RemoteException{
    if (getPersonInfoServiceSoap == null)
      _initGetPersonInfoServiceSoapProxy();
    return getPersonInfoServiceSoap.getPersonByuserID(userID, userName);
  }
  
  public java.lang.String getCorporateAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException{
    if (getPersonInfoServiceSoap == null)
      _initGetPersonInfoServiceSoapProxy();
    return getPersonInfoServiceSoap.getCorporateAllInfo(pageIndex, pageSize, sourceSys);
  }
  
  public java.lang.String getObjRelationAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException{
    if (getPersonInfoServiceSoap == null)
      _initGetPersonInfoServiceSoapProxy();
    return getPersonInfoServiceSoap.getObjRelationAllInfo(pageIndex, pageSize, sourceSys);
  }
  
  public java.lang.String getOrganizationAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException{
    if (getPersonInfoServiceSoap == null)
      _initGetPersonInfoServiceSoapProxy();
    return getPersonInfoServiceSoap.getOrganizationAllInfo(pageIndex, pageSize, sourceSys);
  }
  
  public java.lang.String getPersonAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException{
    if (getPersonInfoServiceSoap == null)
      _initGetPersonInfoServiceSoapProxy();
    return getPersonInfoServiceSoap.getPersonAllInfo(pageIndex, pageSize, sourceSys);
  }
  
  public java.lang.String getPostAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException{
    if (getPersonInfoServiceSoap == null)
      _initGetPersonInfoServiceSoapProxy();
    return getPersonInfoServiceSoap.getPostAllInfo(pageIndex, pageSize, sourceSys);
  }
  
  public java.lang.String getUserAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException{
    if (getPersonInfoServiceSoap == null)
      _initGetPersonInfoServiceSoapProxy();
    return getPersonInfoServiceSoap.getUserAllInfo(pageIndex, pageSize, sourceSys);
  }
  
  
}