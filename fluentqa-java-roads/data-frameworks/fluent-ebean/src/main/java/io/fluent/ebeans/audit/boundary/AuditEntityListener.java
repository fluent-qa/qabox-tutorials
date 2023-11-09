package io.fluent.ebeans.audit.boundary;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PreUpdate;

import io.fluent.ebeans.audit.boundary.interceptor.AuditExportInterceptor;
import io.fluent.ebeans.audit.boundary.interceptor.AuditImportInterceptor;
import io.fluent.ebeans.audit.control.AuditUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author coodoo GmbH (coodoo.io)
 */
@Stateless
public class AuditEntityListener {

    private static Logger log = LoggerFactory.getLogger(AuditEntityListener.class);

    @EJB // @Inject BUG: Inject not possible in EntityListener, FIX available for WildFly 11.0.0.Alpha1, see: https://issues.jboss.org/browse/WFLY-2387
    AuditService auditService;

    @PostLoad
    public void init(AuditInitialValues entity) {

        entity.setInitialValues(AuditUtil.getValues(entity));

        if (AuditExportInterceptor.isExport()) {

            log.debug("Audit {} Export", entity.getClass().getSimpleName());
            auditService.createAuditEvent(entity, AuditAction.EXPORT);
        }
    }

    @PostPersist
    public void create(AuditInitialValues entity) {

        if (AuditImportInterceptor.isImport()) {

            log.debug("Audit {} Import", entity.getClass().getSimpleName());
            auditService.createAuditEvent(entity, AuditAction.IMPORT);
        } else {

            log.debug("Audit {} Create", entity.getClass().getSimpleName());
            auditService.createAuditEvent(entity, AuditAction.CREATE);
        }

        entity.setInitialValues(AuditUtil.getValues(entity));
    }

    @PreUpdate
    public void update(AuditInitialValues entity) {

        log.debug("Audit {} Update", entity.getClass().getSimpleName());
        auditService.createAuditEvent(entity, AuditAction.UPDATE);
    }

    @PostRemove
    public void delete(AuditInitialValues entity) {

        log.debug("Audit {} Delete", entity.getClass().getSimpleName());
        auditService.createAuditEvent(entity, AuditAction.DELETE);
    }

}
