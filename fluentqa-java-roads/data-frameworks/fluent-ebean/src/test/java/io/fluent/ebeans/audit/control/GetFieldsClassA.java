package io.fluent.ebeans.audit.control;

import io.fluent.ebeans.audit.boundary.annotation.AuditIgnoreField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;


public class GetFieldsClassA {

    @Id
    private String fieldA1Id;

    @Column
    private String fieldA2Column;

    @AuditIgnoreField
    private String fieldA3Ignore;

    private String fieldA4;

    @Transient
    private String fieldA5Transient;

    @Deprecated
    private String fieldA6Deprecated;

}
