package org.databene.model.escalate;

public class Escalation {
    
    public final String message;
    public final Object originator;
    public final Object cause;
    
    public Escalation(String message, Object originator, Object cause) {
        super();
        this.message = message;
        this.originator = originator;
        this.cause = cause;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (originator != null)
            builder.append(originator).append(": ");
        builder.append(message);
        if (cause != null)
            builder.append(": ").append(cause);
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result
                + ((originator == null) ? 0 : originator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Escalation other = (Escalation) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (originator == null) {
            if (other.originator != null)
                return false;
        } else if (!originator.equals(other.originator))
            return false;
        return true;
    }

    
}
