
package generated;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for accountTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="accountTransaction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}transaction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="kycBlockHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nickname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="accountAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="accuracy" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "accountTransaction", propOrder = {
    "kycBlockHash",
    "type",
    "nickname",
    "accountAddress",
    "currencyCode",
    "accuracy",
    "value",
    "deactivate"
})
public class AccountTransaction
    extends Transaction
{

    protected String kycBlockHash;
    protected String type;
    protected String nickname;
    protected String accountAddress;
    protected String currencyCode;
    protected int accuracy;
    protected BigDecimal value;
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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the nickname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the value of the nickname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNickname(String value) {
        this.nickname = value;
    }

    /**
     * Gets the value of the accountAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountAddress() {
        return accountAddress;
    }

    /**
     * Sets the value of the accountAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountAddress(String value) {
        this.accountAddress = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the accuracy property.
     * 
     */
    public int getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the value of the accuracy property.
     * 
     */
    public void setAccuracy(int value) {
        this.accuracy = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValue(BigDecimal value) {
        this.value = value;
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
