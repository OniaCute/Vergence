package cc.vergence.features.event.eventbus;

import cc.vergence.features.event.eventbus.interfaces.IEventBus;

/**
 * Thrown when an {@link IEventBus} can't find a registered lambda factory to use.
 */
public class NoLambdaFactoryException extends RuntimeException {
    public NoLambdaFactoryException(Class<?> klass) {
        super("No registered lambda listener for '" + klass.getName() + "'.");
    }
}
