/**
 * SysSynchroGetOrgBaseInfoContext.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.xnjz;

public class SysSynchroGetOrgBaseInfoContext  extends openDemo.service.sync.xnjz.SysSynchroGetOrgContext  implements java.io.Serializable {
    private java.lang.String returnType;

    public SysSynchroGetOrgBaseInfoContext() {
    }

    public SysSynchroGetOrgBaseInfoContext(
           java.lang.String returnOrgType,
           java.lang.String returnType) {
        super(
            returnOrgType);
        this.returnType = returnType;
    }


    /**
     * Gets the returnType value for this SysSynchroGetOrgBaseInfoContext.
     * 
     * @return returnType
     */
    public java.lang.String getReturnType() {
        return returnType;
    }


    /**
     * Sets the returnType value for this SysSynchroGetOrgBaseInfoContext.
     * 
     * @param returnType
     */
    public void setReturnType(java.lang.String returnType) {
        this.returnType = returnType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SysSynchroGetOrgBaseInfoContext)) return false;
        SysSynchroGetOrgBaseInfoContext other = (SysSynchroGetOrgBaseInfoContext) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.returnType==null && other.getReturnType()==null) || 
             (this.returnType!=null &&
              this.returnType.equals(other.getReturnType())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getReturnType() != null) {
            _hashCode += getReturnType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SysSynchroGetOrgBaseInfoContext.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgBaseInfoContext"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "returnType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
