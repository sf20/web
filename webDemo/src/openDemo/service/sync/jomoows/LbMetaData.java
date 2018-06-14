/**
 * LbMetaData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.jomoows;

public class LbMetaData  implements java.io.Serializable {
    private openDemo.service.sync.jomoows.ColInfo[] colInfo;

    private int columnCount;

    public LbMetaData() {
    }

    public LbMetaData(
           openDemo.service.sync.jomoows.ColInfo[] colInfo,
           int columnCount) {
           this.colInfo = colInfo;
           this.columnCount = columnCount;
    }


    /**
     * Gets the colInfo value for this LbMetaData.
     * 
     * @return colInfo
     */
    public openDemo.service.sync.jomoows.ColInfo[] getColInfo() {
        return colInfo;
    }


    /**
     * Sets the colInfo value for this LbMetaData.
     * 
     * @param colInfo
     */
    public void setColInfo(openDemo.service.sync.jomoows.ColInfo[] colInfo) {
        this.colInfo = colInfo;
    }

    public openDemo.service.sync.jomoows.ColInfo getColInfo(int i) {
        return this.colInfo[i];
    }

    public void setColInfo(int i, openDemo.service.sync.jomoows.ColInfo _value) {
        this.colInfo[i] = _value;
    }


    /**
     * Gets the columnCount value for this LbMetaData.
     * 
     * @return columnCount
     */
    public int getColumnCount() {
        return columnCount;
    }


    /**
     * Sets the columnCount value for this LbMetaData.
     * 
     * @param columnCount
     */
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LbMetaData)) return false;
        LbMetaData other = (LbMetaData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.colInfo==null && other.getColInfo()==null) || 
             (this.colInfo!=null &&
              java.util.Arrays.equals(this.colInfo, other.getColInfo()))) &&
            this.columnCount == other.getColumnCount();
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
        if (getColInfo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getColInfo());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getColInfo(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getColumnCount();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LbMetaData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "lbMetaData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("colInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "colInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "colInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columnCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "columnCount"));
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
