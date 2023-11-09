package io.fluent.ebeans.audit.boundary;

import io.fluent.ebeans.audit.boundary.annotation.AuditEntityManager;
import io.fluent.ebeans.audit.boundary.interceptor.AuditSynchronousInterceptor;
import io.fluent.ebeans.audit.control.AuditConfig;
import io.fluent.ebeans.audit.control.AuditController;
import io.fluent.ebeans.audit.control.AuditUser;
import io.fluent.ebeans.audit.control.AuditUtil;
import io.fluent.ebeans.audit.entity.AuditChange;
import io.fluent.ebeans.audit.entity.AuditEvent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;


/**
 * @author coodoo GmbH (coodoo.io)
 */
@Stateless
public class AuditService {

    @Inject
    @AuditEntityManager
    EntityManager entityManager;

    @Inject
    AuditUserBean auditUser;

    @Inject
    AuditController auditController;

    public List<AuditEvent> getEvents(String entity) {
        return AuditEvent.getAllEvents(entityManager, entity);
    }

    public List<AuditEvent> getEvents(String entity, Long entityId) {
        return AuditEvent.getAllEventsForId(entityManager, entity, entityId);
    }

    public List<AuditChange> getChanges(Long eventId) {
        return AuditChange.getByEvent(entityManager, eventId);
    }

    public void createAuditEvent(AuditInitialValues entity, AuditAction action) {

        LocalDateTime createdAt = LocalDateTime.now(ZoneId.of(AuditConfig.LOCAL_DATE_TIME_ZONE));

        if (AuditUtil.groupEvents(entity, action) || AuditSynchronousInterceptor.isSynchonousOnly()) {
            auditController.createAuditEvent(entity, action, getAuditUser(), createdAt);
        } else {
            auditController.createAuditEventAsynchronous(entity, action, getAuditUser(), createdAt);
        }
    }

    private AuditUser getAuditUser() {
        return new AuditUser(auditUser);
    }
}
