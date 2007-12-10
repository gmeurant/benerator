package org.databene.model;

public interface Escalator {
    void escalate(String message, Object originator, Object cause);
}
