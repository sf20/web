/**
 * ISysSynchroGetOrgWebServiceServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.xnjz;

public class ISysSynchroGetOrgWebServiceServiceSoapBindingStub extends org.apache.axis.client.Stub implements openDemo.service.sync.xnjz.ISysSynchroGetOrgWebService {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[10];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getRoleConfCateInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getUpdatedElementsByToken");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoTokenContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgInfoTokenContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgTokenResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgTokenResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getRoleConfInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getUpdatedElements");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getElementsBaseInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgBaseInfoContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgBaseInfoContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getRoleInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getRoleLineDefaultRoleInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getRoleLineInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getOrgGroupCateInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getOrgStaffingLevelInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext"), openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult"));
        oper.setReturnClass(openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"),
                      "com.landray.kmss.sys.organization.webservice.out.Exception",
                      new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception"), 
                      true
                     ));
        _operations[9] = oper;

    }

    public ISysSynchroGetOrgWebServiceServiceSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ISysSynchroGetOrgWebServiceServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ISysSynchroGetOrgWebServiceServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "Exception");
            cachedSerQNames.add(qName);
            cls = openDemo.service.sync.xnjz.Exception.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgBaseInfoContext");
            cachedSerQNames.add(qName);
            cls = openDemo.service.sync.xnjz.SysSynchroGetOrgBaseInfoContext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgContext");
            cachedSerQNames.add(qName);
            cls = openDemo.service.sync.xnjz.SysSynchroGetOrgContext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext");
            cachedSerQNames.add(qName);
            cls = openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoTokenContext");
            cachedSerQNames.add(qName);
            cls = openDemo.service.sync.xnjz.SysSynchroGetOrgInfoTokenContext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgResult");
            cachedSerQNames.add(qName);
            cls = openDemo.service.sync.xnjz.SysSynchroOrgResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroOrgTokenResult");
            cachedSerQNames.add(qName);
            cls = openDemo.service.sync.xnjz.SysSynchroOrgTokenResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleConfCateInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getRoleConfCateInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgTokenResult getUpdatedElementsByToken(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoTokenContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getUpdatedElementsByToken"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgTokenResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgTokenResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgTokenResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleConfInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getRoleConfInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgResult getUpdatedElements(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getUpdatedElements"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgResult getElementsBaseInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgBaseInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getElementsBaseInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getRoleInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleLineDefaultRoleInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getRoleLineDefaultRoleInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgResult getRoleLineInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getRoleLineInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgResult getOrgGroupCateInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getOrgGroupCateInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public openDemo.service.sync.xnjz.SysSynchroOrgResult getOrgStaffingLevelInfo(openDemo.service.sync.xnjz.SysSynchroGetOrgInfoContext arg0) throws java.rmi.RemoteException, openDemo.service.sync.xnjz.Exception {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "getOrgStaffingLevelInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {arg0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (openDemo.service.sync.xnjz.SysSynchroOrgResult) org.apache.axis.utils.JavaUtils.convert(_resp, openDemo.service.sync.xnjz.SysSynchroOrgResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof openDemo.service.sync.xnjz.Exception) {
              throw (openDemo.service.sync.xnjz.Exception) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
