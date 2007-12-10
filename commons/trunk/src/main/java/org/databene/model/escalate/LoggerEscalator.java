package org.databene.model.escalate;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.databene.model.Escalator;

public class LoggerEscalator implements Escalator {
    
    private Set<Escalation> escalations;
    
    public LoggerEscalator() {
        this.escalations = new HashSet<Escalation>();
    }
    
    public void escalate(String message, Object originator, Object cause) {
        Class<? extends Object> category = (originator != null ? originator.getClass() : this.getClass());
        Log logger = LogFactory.getLog(category);
        Escalation escalation = new Escalation(message, originator, cause);
        if (!escalations.contains(escalation)) {
            escalations.add(escalation);
            if (cause instanceof Throwable)
                logger.warn(escalation, (Throwable)cause);
            else
                logger.warn(escalation);
        }
    }
}
