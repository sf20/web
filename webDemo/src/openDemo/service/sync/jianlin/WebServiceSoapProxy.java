package openDemo.service.sync.jianlin;

public class WebServiceSoapProxy implements openDemo.service.sync.jianlin.WebServiceSoap {
  private String _endpoint = null;
  private openDemo.service.sync.jianlin.WebServiceSoap webServiceSoap = null;
  
  public WebServiceSoapProxy() {
    _initWebServiceSoapProxy();
  }
  
  public WebServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initWebServiceSoapProxy();
  }
  
  private void _initWebServiceSoapProxy() {
    try {
      webServiceSoap = (new openDemo.service.sync.jianlin.WebServiceLocator()).getWebServiceSoap();
      if (webServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)webServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)webServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (webServiceSoap != null)
      ((javax.xml.rpc.Stub)webServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public openDemo.service.sync.jianlin.WebServiceSoap getWebServiceSoap() {
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap;
  }
  
  public int orgBeginGetDept(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.orgBeginGetDept(sessionID, newOnly, time);
  }
  
  public int orgBeginGetDept2(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.orgBeginGetDept2(sessionID, newOnly, time);
  }
  
  public int orgBeginGetPosition(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.orgBeginGetPosition(sessionID, newOnly, time);
  }
  
  public int orgBeginGetPosition2(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.orgBeginGetPosition2(sessionID, newOnly, time);
  }
  
  public java.lang.String orgGetDept(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.orgGetDept(sessionID, newOnly, time);
  }
  
  public int empBeginGetEmployee(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.empBeginGetEmployee(sessionID, newOnly, time);
  }
  
  public int empBeginGetEmployee2(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.empBeginGetEmployee2(sessionID, newOnly, time);
  }
  
  public int empBeginGetEmployee_XiaShun(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.empBeginGetEmployee_XiaShun(sessionID, newOnly, time);
  }
  
  public int empBeginGetCard(java.lang.String sessionID, java.lang.String newOnly, java.lang.String time) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.empBeginGetCard(sessionID, newOnly, time);
  }
  
  public java.lang.String attSendLeave(java.lang.String sessionID, java.lang.String strXml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attSendLeave(sessionID, strXml);
  }
  
  public java.lang.String attSendLeave2(java.lang.String sessionID, java.lang.String strXml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attSendLeave2(sessionID, strXml);
  }
  
  public java.lang.String selectLeave(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.selectLeave(sessionID, getxml);
  }
  
  public java.lang.String leaveApply(java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.leaveApply(getxml);
  }
  
  public java.lang.String approvalEnd(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.approvalEnd(sessionID, getxml);
  }
  
  public java.lang.String leaveApplyHand(java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.leaveApplyHand(getxml);
  }
  
  public java.lang.String attSendOver2(java.lang.String sessionID, java.lang.String strXml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attSendOver2(sessionID, strXml);
  }
  
  public java.lang.String attOaApiLeaveCalTime(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attOaApiLeaveCalTime(sessionID, getxml);
  }
  
  public java.lang.String attOaApiLeaveAdd(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attOaApiLeaveAdd(sessionID, getxml);
  }
  
  public java.lang.String attOaApiLeaveUpdateState(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attOaApiLeaveUpdateState(sessionID, getxml);
  }
  
  public java.lang.String attOaApiOverCalTime(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attOaApiOverCalTime(sessionID, getxml);
  }
  
  public java.lang.String attOaApiOverAdd(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attOaApiOverAdd(sessionID, getxml);
  }
  
  public java.lang.String attOaApiOverUpdateState(java.lang.String sessionID, java.lang.String getxml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attOaApiOverUpdateState(sessionID, getxml);
  }
  
  public java.lang.String attGetEmployeeInfo(java.lang.String args) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attGetEmployeeInfo(args);
  }
  
  public java.lang.String attGetEmployeeInfoByA0190(java.lang.String args) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attGetEmployeeInfoByA0190(args);
  }
  
  public java.lang.String attGetTemplates(java.lang.String args) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attGetTemplates(args);
  }
  
  public java.lang.String attSetTemplate(java.lang.String args) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attSetTemplate(args);
  }
  
  public java.lang.String attDeleteTemplate(java.lang.String args) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.attDeleteTemplate(args);
  }
  
  public java.lang.String recGetPostList() throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.recGetPostList();
  }
  
  public java.lang.String recGetPostDetail(int reqId) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.recGetPostDetail(reqId);
  }
  
  public java.lang.String recPutResume(java.lang.String sessionID, java.lang.String xml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.recPutResume(sessionID, xml);
  }
  
  public java.lang.String recPutApplyInfo(java.lang.String sessionID, java.lang.String xml) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.recPutApplyInfo(sessionID, xml);
  }
  
  public java.lang.String login(java.lang.String s1, java.lang.String s2) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.login(s1, s2);
  }
  
  public java.lang.String checkLicense(java.lang.String sessionId) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.checkLicense(sessionId);
  }
  
  public java.lang.String logOut(java.lang.String sessionID) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.logOut(sessionID);
  }
  
  public java.lang.String getSegment(java.lang.String sessionID, int from, int to) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.getSegment(sessionID, from, to);
  }
  
  public java.lang.String clearCache(java.lang.String sessionID) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.clearCache(sessionID);
  }
  
  public java.lang.String getSoaKeyString(java.lang.String s1, java.lang.String s2, java.lang.String s3) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.getSoaKeyString(s1, s2, s3);
  }
  
  public java.lang.String logoutSoaKeyString(java.lang.String s1, java.lang.String s2, java.lang.String s3) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.logoutSoaKeyString(s1, s2, s3);
  }
  
  public java.lang.String customerFunction(java.lang.String args) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.customerFunction(args);
  }
  
  public java.lang.String getDataByWsProc(java.lang.String args) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.getDataByWsProc(args);
  }
  
  
}