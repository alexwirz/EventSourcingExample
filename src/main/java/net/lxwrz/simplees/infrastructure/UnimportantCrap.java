package net.lxwrz.simplees.infrastructure;

import java.lang.reflect.Method;

public class UnimportantCrap {
    public static void invokeHandlerMethod(AggregateRoot aggregateRoot, Event event) {
        Method handlerMethod = getHandlerMethod(aggregateRoot, event);
        if (handlerMethod != null) {
            handlerMethod.setAccessible(true);
            try {
                handlerMethod.invoke(aggregateRoot, event);
            } catch (Exception e) {
                throw new RuntimeException("Unable to call event handler method for " + event.getClass().getName(), e);
            }
        }
    }

    private static Method getHandlerMethod(AggregateRoot aggregateRoot, Event event) {
        try {
            return aggregateRoot.getClass().getDeclaredMethod("apply", event.getClass());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
