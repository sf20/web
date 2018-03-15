/**
 * SysSynchroGetOrgInfoContext.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.common.landray.oa;

public class SysSynchroGetOrgInfoContext  extends openDemo.service.common.landray.oa.SysSynchroGetOrgContext  implements java.io.Serializable {
    private java.lang.String beginTimeStamp;

    private int count;

    public SysSynchroGetOrgInfoContext() {
    }

    public SysSynchroGetOrgInfoContext(
           java.lang.String returnOrgType,
           java.lang.String beginTimeStamp,
           int count) {
        super(
            returnOrgType);
        this.beginTimeStamp = beginTimeStamp;
        this.count = count;
    }


    /**
     * Gets the beginTimeStamp value for this SysSynchroGetOrgInfoContext.
     * 
     * @return beginTimeStamp
     */
    public java.lang.String getBeginTimeStamp() {
        return beginTimeStamp;
    }


    /**
     * Sets the beginTimeStamp value for this SysSynchroGetOrgInfoContext.
     * 
     * @param beginTimeStamp
     */
    public void setBeginTimeStamp(java.lang.String beginTimeStamp) {
        this.beginTimeStamp = beginTimeStamp;
    }


    /**
     * Gets the count value for this SysSynchroGetOrgInfoContext.
     * 
     * @return count
     */
    public int getCount() {
        return count;
    }


    /**
     * Sets the count value for this SysSynchroGetOrgInfoContext.
     * 
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SysSynchroGetOrgInfoContext)) return false;
        SysSynchroGetOrgInfoContext other = (SysSynchroGetOrgInfoContext) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.beginTimeStamp==null && other.getBeginTimeStamp()==null) || 
             (this.beginTimeStamp!=null &&
              this.beginTimeStamp.equals(other.getBeginTimeStamp()))) &&
            this.count == other.getCount();
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
        if (getBeginTimeStamp() != null) {
            _hashCode += getBeginTimeStamp().hashCode();
        }
        _hashCode += getCount();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SysSynchroGetOrgInfoContext.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://out.webservice.organization.sys.kmss.landray.com/", "sysSynchroGetOrgInfoContext"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beginTimeStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "beginTimeStamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
