/**
 * QueryOption.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.jomoows;

public class QueryOption  implements java.io.Serializable {
    private int batchNo;

    private int batchSize;

    private java.lang.String orderBy;

    private boolean queryCount;

    private java.lang.String queryId;

    private openDemo.service.sync.jomoows.ValueOption valueOption;

    public QueryOption() {
    }

    public QueryOption(
           int batchNo,
           int batchSize,
           java.lang.String orderBy,
           boolean queryCount,
           java.lang.String queryId,
           openDemo.service.sync.jomoows.ValueOption valueOption) {
           this.batchNo = batchNo;
           this.batchSize = batchSize;
           this.orderBy = orderBy;
           this.queryCount = queryCount;
           this.queryId = queryId;
           this.valueOption = valueOption;
    }


    /**
     * Gets the batchNo value for this QueryOption.
     * 
     * @return batchNo
     */
    public int getBatchNo() {
        return batchNo;
    }


    /**
     * Sets the batchNo value for this QueryOption.
     * 
     * @param batchNo
     */
    public void setBatchNo(int batchNo) {
        this.batchNo = batchNo;
    }


    /**
     * Gets the batchSize value for this QueryOption.
     * 
     * @return batchSize
     */
    public int getBatchSize() {
        return batchSize;
    }


    /**
     * Sets the batchSize value for this QueryOption.
     * 
     * @param batchSize
     */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }


    /**
     * Gets the orderBy value for this QueryOption.
     * 
     * @return orderBy
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }


    /**
     * Sets the orderBy value for this QueryOption.
     * 
     * @param orderBy
     */
    public void setOrderBy(java.lang.String orderBy) {
        this.orderBy = orderBy;
    }


    /**
     * Gets the queryCount value for this QueryOption.
     * 
     * @return queryCount
     */
    public boolean isQueryCount() {
        return queryCount;
    }


    /**
     * Sets the queryCount value for this QueryOption.
     * 
     * @param queryCount
     */
    public void setQueryCount(boolean queryCount) {
        this.queryCount = queryCount;
    }


    /**
     * Gets the queryId value for this QueryOption.
     * 
     * @return queryId
     */
    public java.lang.String getQueryId() {
        return queryId;
    }


    /**
     * Sets the queryId value for this QueryOption.
     * 
     * @param queryId
     */
    public void setQueryId(java.lang.String queryId) {
        this.queryId = queryId;
    }


    /**
     * Gets the valueOption value for this QueryOption.
     * 
     * @return valueOption
     */
    public openDemo.service.sync.jomoows.ValueOption getValueOption() {
        return valueOption;
    }


    /**
     * Sets the valueOption value for this QueryOption.
     * 
     * @param valueOption
     */
    public void setValueOption(openDemo.service.sync.jomoows.ValueOption valueOption) {
        this.valueOption = valueOption;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryOption)) return false;
        QueryOption other = (QueryOption) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.batchNo == other.getBatchNo() &&
            this.batchSize == other.getBatchSize() &&
            ((this.orderBy==null && other.getOrderBy()==null) || 
             (this.orderBy!=null &&
              this.orderBy.equals(other.getOrderBy()))) &&
            this.queryCount == other.isQueryCount() &&
            ((this.queryId==null && other.getQueryId()==null) || 
             (this.queryId!=null &&
              this.queryId.equals(other.getQueryId()))) &&
            ((this.valueOption==null && other.getValueOption()==null) || 
             (this.valueOption!=null &&
              this.valueOption.equals(other.getValueOption())));
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
        _hashCode += getBatchNo();
        _hashCode += getBatchSize();
        if (getOrderBy() != null) {
            _hashCode += getOrderBy().hashCode();
        }
        _hashCode += (isQueryCount() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getQueryId() != null) {
            _hashCode += getQueryId().hashCode();
        }
        if (getValueOption() != null) {
            _hashCode += getValueOption().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryOption.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "queryOption"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "batchNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "batchSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderBy");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "queryCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "queryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valueOption");
        elemField.setXmlName(new javax.xml.namespace.QName("", "valueOption"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "valueOption"));
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
