/**
 * QueryResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.jomoows;

public class QueryResult  extends openDemo.service.sync.jomoows.LbeResult  implements java.io.Serializable {
    private int count;

    private boolean hasMore;

    private openDemo.service.sync.jomoows.LbMetaData metaData;

    private java.lang.String queryId;

    private openDemo.service.sync.jomoows.LbRecord[] records;

    public QueryResult() {
    }

    public QueryResult(
           java.lang.String message,
           int result,
           int count,
           boolean hasMore,
           openDemo.service.sync.jomoows.LbMetaData metaData,
           java.lang.String queryId,
           openDemo.service.sync.jomoows.LbRecord[] records) {
        super(
            message,
            result);
        this.count = count;
        this.hasMore = hasMore;
        this.metaData = metaData;
        this.queryId = queryId;
        this.records = records;
    }


    /**
     * Gets the count value for this QueryResult.
     * 
     * @return count
     */
    public int getCount() {
        return count;
    }


    /**
     * Sets the count value for this QueryResult.
     * 
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }


    /**
     * Gets the hasMore value for this QueryResult.
     * 
     * @return hasMore
     */
    public boolean isHasMore() {
        return hasMore;
    }


    /**
     * Sets the hasMore value for this QueryResult.
     * 
     * @param hasMore
     */
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }


    /**
     * Gets the metaData value for this QueryResult.
     * 
     * @return metaData
     */
    public openDemo.service.sync.jomoows.LbMetaData getMetaData() {
        return metaData;
    }


    /**
     * Sets the metaData value for this QueryResult.
     * 
     * @param metaData
     */
    public void setMetaData(openDemo.service.sync.jomoows.LbMetaData metaData) {
        this.metaData = metaData;
    }


    /**
     * Gets the queryId value for this QueryResult.
     * 
     * @return queryId
     */
    public java.lang.String getQueryId() {
        return queryId;
    }


    /**
     * Sets the queryId value for this QueryResult.
     * 
     * @param queryId
     */
    public void setQueryId(java.lang.String queryId) {
        this.queryId = queryId;
    }


    /**
     * Gets the records value for this QueryResult.
     * 
     * @return records
     */
    public openDemo.service.sync.jomoows.LbRecord[] getRecords() {
        return records;
    }


    /**
     * Sets the records value for this QueryResult.
     * 
     * @param records
     */
    public void setRecords(openDemo.service.sync.jomoows.LbRecord[] records) {
        this.records = records;
    }

    public openDemo.service.sync.jomoows.LbRecord getRecords(int i) {
        return this.records[i];
    }

    public void setRecords(int i, openDemo.service.sync.jomoows.LbRecord _value) {
        this.records[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryResult)) return false;
        QueryResult other = (QueryResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.count == other.getCount() &&
            this.hasMore == other.isHasMore() &&
            ((this.metaData==null && other.getMetaData()==null) || 
             (this.metaData!=null &&
              this.metaData.equals(other.getMetaData()))) &&
            ((this.queryId==null && other.getQueryId()==null) || 
             (this.queryId!=null &&
              this.queryId.equals(other.getQueryId()))) &&
            ((this.records==null && other.getRecords()==null) || 
             (this.records!=null &&
              java.util.Arrays.equals(this.records, other.getRecords())));
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
        _hashCode += getCount();
        _hashCode += (isHasMore() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMetaData() != null) {
            _hashCode += getMetaData().hashCode();
        }
        if (getQueryId() != null) {
            _hashCode += getQueryId().hashCode();
        }
        if (getRecords() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRecords());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRecords(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "queryResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasMore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hasMore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metaData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "metaData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "lbMetaData"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("records");
        elemField.setXmlName(new javax.xml.namespace.QName("", "records"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "lbRecord"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
