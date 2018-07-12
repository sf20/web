/**
 * WFWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.meilitianyuan;

public class WFWebServiceLocator extends org.apache.axis.client.Service implements WFWebService {

    public WFWebServiceLocator() {
    }


    public WFWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WFWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WFWebServiceSoap
    private java.lang.String WFWebServiceSoap_address = "http://bpm.beautyfarm.com.cn/WFWebService/WFWebService.asmx";

    public java.lang.String getWFWebServiceSoapAddress() {
        return WFWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WFWebServiceSoapWSDDServiceName = "WFWebServiceSoap";

    public java.lang.String getWFWebServiceSoapWSDDServiceName() {
        return WFWebServiceSoapWSDDServiceName;
    }

    public void setWFWebServiceSoapWSDDServiceName(java.lang.String name) {
        WFWebServiceSoapWSDDServiceName = name;
    }

    public WFWebServiceSoap getWFWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WFWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWFWebServiceSoap(endpoint);
    }

    public WFWebServiceSoap getWFWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            WFWebServiceSoapStub _stub = new WFWebServiceSoapStub(portAddress, this);
            _stub.setPortName(getWFWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWFWebServiceSoapEndpointAddress(java.lang.String address) {
        WFWebServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (WFWebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                WFWebServiceSoapStub _stub = new WFWebServiceSoapStub(new java.net.URL(WFWebServiceSoap_address), this);
                _stub.setPortName(getWFWebServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WFWebServiceSoap".equals(inputPortName)) {
            return getWFWebServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "WFWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "WFWebServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WFWebServiceSoap".equals(portName)) {
            setWFWebServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
