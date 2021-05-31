
package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serviceTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceTransaction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}transaction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="kycBlockHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="serviceCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="serviceSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="deactivate" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceTransaction", propOrder = {
    "kycBlockHash",
    "serviceCode",
    "serviceSign",
    "deactivate"
})
public class ServiceTransaction
    extends Transaction
{

    protected String kycBlockHash;
    protected String serviceCode;
    protected String serviceSign;
    @XmlElement(defaultValue = "false")
    protected boolean deactivate;

    /**
     * Gets the value of the kycBlockHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKycBlockHash() {
        return kycBlockHash;
    }

    /**
     * Sets the value of the kycBlockHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKycBlockHash(String value) {
        this.kycBlockHash = value;
    }

    /**
     * Gets the value of the serviceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * Sets the value of the serviceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceCode(String value) {
        this.serviceCode = value;
    }

    /**
     * Gets the value of the serviceSign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceSign() {
        return serviceSign;
    }

    /**
     * Sets the value of the serviceSign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceSign(String value) {
        this.serviceSign = value;
    }

    /**
     * Gets the value of the deactivate property.
     * 
     */
    public boolean isDeactivate() {
        return deactivate;
    }

    /**
     * Sets the value of the deactivate property.
     * 
     */
    public void setDeactivate(boolean value) {
        this.deactivate = value;
    }

}
