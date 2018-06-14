/**
 * LBEBusinessWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.jomoows;

public class LBEBusinessWebServiceLocator extends org.apache.axis.client.Service implements openDemo.service.sync.jomoows.LBEBusinessWebService {

    public LBEBusinessWebServiceLocator() {
    }


    public LBEBusinessWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LBEBusinessWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LBEBusinessServiceImplPort
    private java.lang.String LBEBusinessServiceImplPort_address = "http://xsj.jomoo.cn/service/LBEBusiness";

    public java.lang.String getLBEBusinessServiceImplPortAddress() {
        return LBEBusinessServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LBEBusinessServiceImplPortWSDDServiceName = "LBEBusinessServiceImplPort";

    public java.lang.String getLBEBusinessServiceImplPortWSDDServiceName() {
        return LBEBusinessServiceImplPortWSDDServiceName;
    }

    public void setLBEBusinessServiceImplPortWSDDServiceName(java.lang.String name) {
        LBEBusinessServiceImplPortWSDDServiceName = name;
    }

    public openDemo.service.sync.jomoows.LBEBusinessService getLBEBusinessServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LBEBusinessServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLBEBusinessServiceImplPort(endpoint);
    }

    public openDemo.service.sync.jomoows.LBEBusinessService getLBEBusinessServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            openDemo.service.sync.jomoows.LBEBusinessWebServiceSoapBindingStub _stub = new openDemo.service.sync.jomoows.LBEBusinessWebServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getLBEBusinessServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLBEBusinessServiceImplPortEndpointAddress(java.lang.String address) {
        LBEBusinessServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (openDemo.service.sync.jomoows.LBEBusinessService.class.isAssignableFrom(serviceEndpointInterface)) {
                openDemo.service.sync.jomoows.LBEBusinessWebServiceSoapBindingStub _stub = new openDemo.service.sync.jomoows.LBEBusinessWebServiceSoapBindingStub(new java.net.URL(LBEBusinessServiceImplPort_address), this);
                _stub.setPortName(getLBEBusinessServiceImplPortWSDDServiceName());
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
        if ("LBEBusinessServiceImplPort".equals(inputPortName)) {
            return getLBEBusinessServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "LBEBusinessWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "LBEBusinessServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("LBEBusinessServiceImplPort".equals(portName)) {
            setLBEBusinessServiceImplPortEndpointAddress(address);
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
