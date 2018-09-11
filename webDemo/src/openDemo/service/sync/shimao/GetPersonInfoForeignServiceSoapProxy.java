package openDemo.service.sync.shimao;

public class GetPersonInfoForeignServiceSoapProxy implements GetPersonInfoForeignServiceSoap {
  private String _endpoint = null;
  private GetPersonInfoForeignServiceSoap getPersonInfoForeignServiceSoap = null;
  
  public GetPersonInfoForeignServiceSoapProxy() {
    _initGetPersonInfoForeignServiceSoapProxy();
  }
  
  public GetPersonInfoForeignServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initGetPersonInfoForeignServiceSoapProxy();
  }
  
  private void _initGetPersonInfoForeignServiceSoapProxy() {
    try {
      getPersonInfoForeignServiceSoap = (new GetPersonInfoForeignServiceLocator()).getGetPersonInfoForeignServiceSoap();
      if (getPersonInfoForeignServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)getPersonInfoForeignServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)getPersonInfoForeignServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (getPersonInfoForeignServiceSoap != null)
      ((javax.xml.rpc.Stub)getPersonInfoForeignServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public GetPersonInfoForeignServiceSoap getGetPersonInfoForeignServiceSoap() {
    if (getPersonInfoForeignServiceSoap == null)
      _initGetPersonInfoForeignServiceSoapProxy();
    return getPersonInfoForeignServiceSoap;
  }
  
  public java.lang.String getObjRelationAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException{
    if (getPersonInfoForeignServiceSoap == null)
      _initGetPersonInfoForeignServiceSoapProxy();
    return getPersonInfoForeignServiceSoap.getObjRelationAllInfo(pageIndex, pageSize, sourceSys);
  }
  
  public java.lang.String getPersonAllInfo(int pageIndex, int pageSize, java.lang.String sourceSys) throws java.rmi.RemoteException{
    if (getPersonInfoForeignServiceSoap == null)
      _initGetPersonInfoForeignServiceSoapProxy();
    return getPersonInfoForeignServiceSoap.getPersonAllInfo(pageIndex, pageSize, sourceSys);
  }
  
  
}