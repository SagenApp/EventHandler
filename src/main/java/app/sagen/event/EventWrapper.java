package app.sagen.event;

import java.lang.reflect.Method;

/**
 * This is a class used for internally wrapping Listeners with priority and a reflection Method.
 *
 * @author Sagen
 */
public class EventWrapper implements Comparable<EventWrapper> {

    /*
     * The listener class owning the method
     */
    private final Listener listener;

    /*
     * The method to call on an event
     */
    private final Method method;

    /*
     * The priority of this listener
     */
    private final EventPriority priority;

    /*
     * Whether this event has been cancelled or not
     */
    private final boolean ignoreCancelled;

    /**
     * Creates a new instance of this class
     *
     * @param listener The listener owning the method
     * @param method   The method to call on event
     * @param priority The priority of this listener
     * @param ignoreCancelled Whether or not this listener should ignore cancelled events

     */
    public EventWrapper(Listener listener, Method method, EventPriority priority, boolean ignoreCancelled) {
        this.listener = listener;
        this.method = method;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    /**
     * Returns the listener instance
     *
     * @return The listener
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * Calls the method defined in this listener with the given event class.
     *
     * @param event The event to pass to the listener
     */
    public void fire(Event event) {
        if (ignoreCancelled == true &&
                event instanceof Cancelable &&
                ((Cancelable) event).isCancelled()) return; // ignore if cancelled
        try {
            method.invoke(listener, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a String representation of this class
     *
     * @return The string representation
     */
    @Override
    public String toString() {
        return "[EventWrapper " + this.listener + ": " + method.getName() + ": " + this.priority.toString() + "]";
    }

    /**
     * Compares this instance with another instance
     *
     * @param other The other instance
     * @return the result of comparison.
     */
    @Override
    public int compareTo(EventWrapper other) {
        // compare the who instances. Use the hashCode as fallback if priorities are equal.
        int annotation = this.priority.getValue() - other.priority.getValue();
        if (annotation == 0) annotation = this.listener.hashCode() - other.listener.hashCode();
        return annotation;
    }
}
