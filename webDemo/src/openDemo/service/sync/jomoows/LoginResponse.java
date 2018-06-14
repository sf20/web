/**
 * LoginResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package openDemo.service.sync.jomoows;

public class LoginResponse  implements java.io.Serializable {
    private openDemo.service.sync.jomoows.LoginResult loginResult;

    public LoginResponse() {
    }

    public LoginResponse(
           openDemo.service.sync.jomoows.LoginResult loginResult) {
           this.loginResult = loginResult;
    }


    /**
     * Gets the loginResult value for this LoginResponse.
     * 
     * @return loginResult
     */
    public openDemo.service.sync.jomoows.LoginResult getLoginResult() {
        return loginResult;
    }


    /**
     * Sets the loginResult value for this LoginResponse.
     * 
     * @param loginResult
     */
    public void setLoginResult(openDemo.service.sync.jomoows.LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LoginResponse)) return false;
        LoginResponse other = (LoginResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.loginResult==null && other.getLoginResult()==null) || 
             (this.loginResult!=null &&
              this.loginResult.equals(other.getLoginResult())));
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
        if (getLoginResult() != null) {
            _hashCode += getLoginResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LoginResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "loginResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginResult");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LoginResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.livebos.apex.com/", "loginResult"));
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
