
package generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _VIAChain_QNAME = new QName("", "VIA_chain");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link generated.Transaction.TransactionObject }
     * 
     */
    public generated.Transaction.TransactionObject createTransactionTransactionObject() {
        return new generated.Transaction.TransactionObject();
    }

    /**
     * Create an instance of {@link Block }
     * 
     */
    public Block createBlock() {
        return new Block();
    }

    /**
     * Create an instance of {@link BlockList }
     * 
     */
    public BlockList createBlockList() {
        return new BlockList();
    }

    /**
     * Create an instance of {@link GenesisTransaction }
     * 
     */
    public GenesisTransaction createGenesisTransaction() {
        return new GenesisTransaction();
    }

    /**
     * Create an instance of {@link TypeTransaction }
     * 
     */
    public TypeTransaction createTypeTransaction() {
        return new TypeTransaction();
    }

    /**
     * Create an instance of {@link KycTransaction }
     * 
     */
    public KycTransaction createKycTransaction() {
        return new KycTransaction();
    }

    /**
     * Create an instance of {@link ServiceTransaction }
     * 
     */
    public ServiceTransaction createServiceTransaction() {
        return new ServiceTransaction();
    }

    /**
     * Create an instance of {@link AuthTransaction }
     * 
     */
    public AuthTransaction createAuthTransaction() {
        return new AuthTransaction();
    }

    /**
     * Create an instance of {@link AccountTransaction }
     * 
     */
    public AccountTransaction createAccountTransaction() {
        return new AccountTransaction();
    }

    /**
     * Create an instance of {@link ProfileTransaction }
     * 
     */
    public ProfileTransaction createProfileTransaction() {
        return new ProfileTransaction();
    }

    /**
     * Create an instance of {@link ContentTransaction }
     * 
     */
    public ContentTransaction createContentTransaction() {
        return new ContentTransaction();
    }

    /**
     * Create an instance of {@link PublishTransaction }
     * 
     */
    public PublishTransaction createPublishTransaction() {
        return new PublishTransaction();
    }

    /**
     * Create an instance of {@link LedgerTransaction }
     * 
     */
    public LedgerTransaction createLedgerTransaction() {
        return new LedgerTransaction();
    }

    /**
     * Create an instance of {@link To }
     * 
     */
    public To createTo() {
        return new To();
    }

    /**
     * Create an instance of {@link generated.Transaction.TransactionObject.Entry }
     * 
     */
    public generated.Transaction.TransactionObject.Entry createTransactionTransactionObjectEntry() {
        return new generated.Transaction.TransactionObject.Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Block }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Block }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "VIA_chain")
    public JAXBElement<Block> createVIAChain(Block value) {
        return new JAXBElement<Block>(_VIAChain_QNAME, Block.class, null, value);
    }

}
