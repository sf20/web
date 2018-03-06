/**
 * ISysSynchroGetOrgWebServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.xnjz;

public class ISysSynchroGetOrgWebServiceServiceLocator extends org.apache.axis.client.Service implements openDemo.service.sync.xnjz.ISysSynchroGetOrgWebServiceService {

    public ISysSynchroGetOrgWebServiceServiceLocator() {
    }


    public ISysSynchroGetOrgWebServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ISysSynchroGetOrgWebServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ISysSynchroGetOrgWebServicePort
    private java.lang.String ISysSynchroGetOrgWebServicePort_address = "http://ekpsrv.xnjz.com:8080/sys/webservice/sysSynchroGetOrgWebService";

    public java.lang.String getISysSynchroGetOrgWebServicePortAddress() {
        return ISysSynchroGetOrgWebServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ISysSynchroGetOrgWebServicePortWSDDServiceName = "ISysSynchroGetOrgWebServicePort";

    public java.lang.String getISysSynchroGetOrgWebServicePortWSDDServiceName() {
        return ISysSynchroGetOrgWebServicePortWSDDServiceName;
    }

    public void setISysSynchroGetOrgWebServicePortWSDDServiceName(java.lang.String name) {
        ISysSynchroGetOrgWebServicePortWSDDServiceName = name;
    }

    public openDemo.service.sync.xnjz.ISysSynchroGetOrgWebService getISysSynchroGetOrgWebServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ISysSynchroGetOrgWebServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getISysSynchroGetOrgWebServicePort(endpoint);
    }

    public openDemo.service.sync.xnjz.ISysSynchroGetOrgWebService getISysSynchroGetOrgWebServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            openDemo.service.sync.xnjz.ISysSynchroGetOrgWebServiceServiceSoapBindingStub _stub = new openDemo.service.sync.xnjz.ISysSynchroGetOrgWebServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getISysSynchroGetOrgWebServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setISysSynchroGetOrgWebServicePortEndpointAddress(java.lang.String address) {
        ISysSynchroGetOrgWebServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (openDemo.service.sync.xnjz.ISysSynchroGetOrgWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                openDemo.service.sync.xnjz.ISysSynchroGetOrgWebServiceServiceSoapBindingStub _stub = new openDemo.service.sync.xnjz.ISysSynchroGetOrgWebServiceServiceSoapBindingStub(new java.net.URL(ISysSynchroGetOrgWebServicePort_address), this);
                _stub.setPortName(getISysSynchroGetOrgWebServicePortWSDDServiceName());
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
        if ("ISysSynchroGetOrgWebServicePort".equals(inputPortName)) {
            return getISysSynchroGetOrgWebServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "ISysSynchroGetOrgWebServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "ISysSynchroGetOrgWebServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ISysSynchroGetOrgWebServicePort".equals(portName)) {
            setISysSynchroGetOrgWebServicePortEndpointAddress(address);
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
