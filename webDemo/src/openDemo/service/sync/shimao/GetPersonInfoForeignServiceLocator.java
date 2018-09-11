/**
 * GetPersonInfoForeignServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.shimao;

public class GetPersonInfoForeignServiceLocator extends org.apache.axis.client.Service implements GetPersonInfoForeignService {

    public GetPersonInfoForeignServiceLocator() {
    }


    public GetPersonInfoForeignServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GetPersonInfoForeignServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GetPersonInfoForeignServiceSoap
    private java.lang.String GetPersonInfoForeignServiceSoap_address = "http://mds.shimaogroup.com:8082/GetPersonInfoForeignService.asmx";

    public java.lang.String getGetPersonInfoForeignServiceSoapAddress() {
        return GetPersonInfoForeignServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GetPersonInfoForeignServiceSoapWSDDServiceName = "GetPersonInfoForeignServiceSoap";

    public java.lang.String getGetPersonInfoForeignServiceSoapWSDDServiceName() {
        return GetPersonInfoForeignServiceSoapWSDDServiceName;
    }

    public void setGetPersonInfoForeignServiceSoapWSDDServiceName(java.lang.String name) {
        GetPersonInfoForeignServiceSoapWSDDServiceName = name;
    }

    public GetPersonInfoForeignServiceSoap getGetPersonInfoForeignServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GetPersonInfoForeignServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGetPersonInfoForeignServiceSoap(endpoint);
    }

    public GetPersonInfoForeignServiceSoap getGetPersonInfoForeignServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            GetPersonInfoForeignServiceSoapStub _stub = new GetPersonInfoForeignServiceSoapStub(portAddress, this);
            _stub.setPortName(getGetPersonInfoForeignServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGetPersonInfoForeignServiceSoapEndpointAddress(java.lang.String address) {
        GetPersonInfoForeignServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (GetPersonInfoForeignServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                GetPersonInfoForeignServiceSoapStub _stub = new GetPersonInfoForeignServiceSoapStub(new java.net.URL(GetPersonInfoForeignServiceSoap_address), this);
                _stub.setPortName(getGetPersonInfoForeignServiceSoapWSDDServiceName());
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
        if ("GetPersonInfoForeignServiceSoap".equals(inputPortName)) {
            return getGetPersonInfoForeignServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "GetPersonInfoForeignService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "GetPersonInfoForeignServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GetPersonInfoForeignServiceSoap".equals(portName)) {
            setGetPersonInfoForeignServiceSoapEndpointAddress(address);
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
