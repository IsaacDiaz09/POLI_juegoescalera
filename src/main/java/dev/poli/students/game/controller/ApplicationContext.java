package dev.poli.students.game.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    private static final Map<String, Object> CONTEXT = new ConcurrentHashMap<>();

    public static void addBean(Object bean) {
        if (bean != null) {
            CONTEXT.put(bean.getClass().getCanonicalName(), bean);
        }
    }

    public static void addBean(String key, Object bean) {
        if (bean != null) {
            CONTEXT.put(key, bean);
        }
    }

    public static <T> T getBean(Class<T> beanType) {
        Object bean = CONTEXT.get(beanType.getCanonicalName());
        if (bean == null) {
            throw new IllegalArgumentException("No bean found of type " + beanType);
        }
        return beanType.cast(bean);
    }

    public static <T> T getBean(String beanName, Class<T> beanType) {
        Object bean = CONTEXT.get(beanName);
        if (bean == null) {
            throw new IllegalArgumentException("No bean found of type " + beanType);
        }
        return beanType.cast(bean);
    }
}
