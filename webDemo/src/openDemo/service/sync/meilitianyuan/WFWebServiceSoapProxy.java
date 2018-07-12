package openDemo.service.sync.meilitianyuan;

public class WFWebServiceSoapProxy implements WFWebServiceSoap {
  private String _endpoint = null;
  private WFWebServiceSoap wFWebServiceSoap = null;
  
  public WFWebServiceSoapProxy() {
    _initWFWebServiceSoapProxy();
  }
  
  public WFWebServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initWFWebServiceSoapProxy();
  }
  
  private void _initWFWebServiceSoapProxy() {
    try {
      wFWebServiceSoap = (new WFWebServiceLocator()).getWFWebServiceSoap();
      if (wFWebServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wFWebServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wFWebServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wFWebServiceSoap != null)
      ((javax.xml.rpc.Stub)wFWebServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public WFWebServiceSoap getWFWebServiceSoap() {
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap;
  }
  
  public java.lang.String helloWorld() throws java.rmi.RemoteException{
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap.helloWorld();
  }
  
  public java.lang.String orgList() throws java.rmi.RemoteException{
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap.orgList();
  }
  
  public java.lang.String userList(int pageSize, int currentPage) throws java.rmi.RemoteException{
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap.userList(pageSize, currentPage);
  }
  
  public java.lang.String storeList(int pageSize, int currentPage) throws java.rmi.RemoteException{
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap.storeList(pageSize, currentPage);
  }
  
  public java.lang.String productList(int pageSize, int currentPage, java.lang.String startTime, java.lang.String endTime) throws java.rmi.RemoteException{
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap.productList(pageSize, currentPage, startTime, endTime);
  }
  
  public java.lang.String beautyCareList(int pageSize, int currentPage, java.lang.String startTime, java.lang.String endTime) throws java.rmi.RemoteException{
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap.beautyCareList(pageSize, currentPage, startTime, endTime);
  }
  
  public java.lang.String courseList(int pageSize, int currentPage, java.lang.String startTime, java.lang.String endTime) throws java.rmi.RemoteException{
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap.courseList(pageSize, currentPage, startTime, endTime);
  }
  
  public java.lang.String activityList(int pageSize, int currentPage, java.lang.String startTime, java.lang.String endTime) throws java.rmi.RemoteException{
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap.activityList(pageSize, currentPage, startTime, endTime);
  }
  
  public java.lang.String subjectsList(int pageSize, int currentPage, java.lang.String parent_Code, java.lang.String ext1) throws java.rmi.RemoteException{
    if (wFWebServiceSoap == null)
      _initWFWebServiceSoapProxy();
    return wFWebServiceSoap.subjectsList(pageSize, currentPage, parent_Code, ext1);
  }
  
  
}