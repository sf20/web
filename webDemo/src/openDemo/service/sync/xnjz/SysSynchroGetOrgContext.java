/**
 * SysSynchroGetOrgContext.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.xnjz;

public class SysSynchroGetOrgContext  implements java.io.Serializable {
    private java.lang.String returnOrgType;

    public SysSynchroGetOrgContext() {
    }

    public SysSynchroGetOrgContext(
           java.lang.String returnOrgType) {
           this.returnOrgType = returnOrgType;
    }


    /**
     * Gets the returnOrgType value for this SysSynchroGetOrgContext.
     * 
     * @return returnOrgType
     */
    public java.lang.String getReturnOrgType() {
        return returnOrgType;
    }


    /**
     * Sets the returnOrgType value for this SysSynchroGetOrgContext.
     * 
     * @param returnOrgType
     */
    public void setReturnOrgType(java.lang.String returnOrgType) {
        this.returnOrgType = returnOrgType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SysSynchroGetOrgContext)) return false;
        SysSynchroGetOrgContext other = (SysSynchroGetOrgContext) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.returnOrgType==null && other.getReturnOrgType()==null) || 
             (this.returnOrgType!=null &&
              this.returnOrgType.equals(other.getReturnOrgType())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getReturnOrgType() != null) {
            _hashCode += getReturnOrgType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SysSynchroGetOrgContext.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgContext"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnOrgType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "returnOrgType"));
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
