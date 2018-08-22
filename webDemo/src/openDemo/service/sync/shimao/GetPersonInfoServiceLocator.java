/**
 * GetPersonInfoServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.shimao;

public class GetPersonInfoServiceLocator extends org.apache.axis.client.Service implements GetPersonInfoService {

    public GetPersonInfoServiceLocator() {
    }


    public GetPersonInfoServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GetPersonInfoServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GetPersonInfoServiceSoap
    private java.lang.String GetPersonInfoServiceSoap_address = "http://10.6.1.154:8018/GetPersonInfoService.asmx";

    public java.lang.String getGetPersonInfoServiceSoapAddress() {
        return GetPersonInfoServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GetPersonInfoServiceSoapWSDDServiceName = "GetPersonInfoServiceSoap";

    public java.lang.String getGetPersonInfoServiceSoapWSDDServiceName() {
        return GetPersonInfoServiceSoapWSDDServiceName;
    }

    public void setGetPersonInfoServiceSoapWSDDServiceName(java.lang.String name) {
        GetPersonInfoServiceSoapWSDDServiceName = name;
    }

    public GetPersonInfoServiceSoap getGetPersonInfoServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GetPersonInfoServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGetPersonInfoServiceSoap(endpoint);
    }

    public GetPersonInfoServiceSoap getGetPersonInfoServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            GetPersonInfoServiceSoapStub _stub = new GetPersonInfoServiceSoapStub(portAddress, this);
            _stub.setPortName(getGetPersonInfoServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGetPersonInfoServiceSoapEndpointAddress(java.lang.String address) {
        GetPersonInfoServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (GetPersonInfoServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                GetPersonInfoServiceSoapStub _stub = new GetPersonInfoServiceSoapStub(new java.net.URL(GetPersonInfoServiceSoap_address), this);
                _stub.setPortName(getGetPersonInfoServiceSoapWSDDServiceName());
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
        if ("GetPersonInfoServiceSoap".equals(inputPortName)) {
            return getGetPersonInfoServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "GetPersonInfoService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "GetPersonInfoServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GetPersonInfoServiceSoap".equals(portName)) {
            setGetPersonInfoServiceSoapEndpointAddress(address);
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
