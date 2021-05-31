
package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NodeBalancingInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NodeBalancingInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="protocolCPU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="protocolMem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="blockStorageSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="totalStorageSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="protocolHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeBalancingInfo", propOrder = {
    "protocolCPU",
    "protocolMem",
    "blockStorageSize",
    "totalStorageSize",
    "protocolHost"
})
public class NodeBalancingInfo {

    protected String protocolCPU;
    protected String protocolMem;
    protected String blockStorageSize;
    protected String totalStorageSize;
    protected String protocolHost;

    /**
     * Gets the value of the protocolCPU property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocolCPU() {
        return protocolCPU;
    }

    /**
     * Sets the value of the protocolCPU property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocolCPU(String value) {
        this.protocolCPU = value;
    }

    /**
     * Gets the value of the protocolMem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocolMem() {
        return protocolMem;
    }

    /**
     * Sets the value of the protocolMem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocolMem(String value) {
        this.protocolMem = value;
    }

    /**
     * Gets the value of the blockStorageSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlockStorageSize() {
        return blockStorageSize;
    }

    /**
     * Sets the value of the blockStorageSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlockStorageSize(String value) {
        this.blockStorageSize = value;
    }

    /**
     * Gets the value of the totalStorageSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalStorageSize() {
        return totalStorageSize;
    }

    /**
     * Sets the value of the totalStorageSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalStorageSize(String value) {
        this.totalStorageSize = value;
    }

    /**
     * Gets the value of the protocolHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocolHost() {
        return protocolHost;
    }

    /**
     * Sets the value of the protocolHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocolHost(String value) {
        this.protocolHost = value;
    }

}
