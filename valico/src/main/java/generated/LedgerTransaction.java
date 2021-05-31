
package generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ledgerTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ledgerTransaction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}transaction"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="fromAddress" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="fromAccountBlockHash" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="to" type="{}to" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ledgerTransaction", propOrder = {
    "type",
    "fromAddress",
    "fromAccountBlockHash",
    "to"
})
public class LedgerTransaction
    extends Transaction
{

    @XmlElement(required = true)
    protected String type;
    @XmlElement(required = true)
    protected String fromAddress;
    @XmlElement(required = true)
    protected String fromAccountBlockHash;
    @XmlElement(required = true)
    protected List<To> to;

    @Override
	public String toString() {
		return "LedgerTransaction [type=" + type + ", fromAddress=" + fromAddress + ", fromAccountBlockHash="
				+ fromAccountBlockHash + ", to=" + to + "]";
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
     * Gets the value of the fromAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * Sets the value of the fromAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromAddress(String value) {
        this.fromAddress = value;
    }

    /**
     * Gets the value of the fromAccountBlockHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromAccountBlockHash() {
        return fromAccountBlockHash;
    }

    /**
     * Sets the value of the fromAccountBlockHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromAccountBlockHash(String value) {
        this.fromAccountBlockHash = value;
    }

    /**
     * Gets the value of the to property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the to property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link To }
     * 
     * 
     */
    public List<To> getTo() {
        if (to == null) {
            to = new ArrayList<To>();
        }
        return this.to;
    }

}
