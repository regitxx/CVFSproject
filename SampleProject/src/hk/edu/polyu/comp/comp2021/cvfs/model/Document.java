package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
/**
 * Represents a document in the virtual file system.
 * A document has a name, type (e.g., txt, java, html, css), and content.
 */
public class Document extends File implements Serializable {
    private String docName;
    private final String docType;
    private final String docContent;
    /**
     * Base size for document in bytes.
     */
    public final int c = 40;
    /**
     * Constructs a new document with the specified name, type, and content.
     *
     * @param docName    The name of the document.
     * @param docType    The type of the document (e.g., txt, java, html, css).
     * @param docContent The content of the document.
     */
    public Document(String docName, String docType, String docContent) {
        super(docName);
        this.docType = docType;
        this.docContent = docContent;
    }
    /**
     * Retrieves the type of this document.
     *
     * @return The type of the document as a string.
     */
    @Override
    public String getType() {
        return docType;
    }
    /**
     * Calculates the size of this document in bytes.
     * The size is calculated as the base size (40 bytes) plus 2 bytes for each character in the content.
     *
     * @return The size of the document in bytes.
     */
    @Override
    public int getSize() {
        return c + 2 * docContent.length();
    }
    /**
     * Retrieves the content of this document.
     *
     * @return The content of the document as a string.
     */
    public String getDocContent() {
        return docContent;
    }
}
