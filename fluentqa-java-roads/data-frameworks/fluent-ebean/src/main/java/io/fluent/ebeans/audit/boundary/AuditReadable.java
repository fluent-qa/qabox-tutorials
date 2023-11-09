package io.fluent.ebeans.audit.boundary;


/**
 * Interface needed for the use of {@link AuditRelatedEntity} to provide a String representation.
 * 
 * @author coodoo GmbH (coodoo.io)
 */
public interface AuditReadable {

    /**
     * @return String representation of current object for the usage in auditing
     */
    public String toAuditableString();

}
