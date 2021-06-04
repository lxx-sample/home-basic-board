package li.dongpo.home.configuration;

import li.dongpo.home.annotation.HandlerMapping;
import li.dongpo.home.manager.MessageHandlerManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author dongpo.li
 * @date 2021/6/4
 */
@Component
public class HandlerMappingAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        String basePath = "";
        Class<?> clazz = bean.getClass();

        HandlerMapping typeAnnotation = clazz.getAnnotation(HandlerMapping.class);
        if (typeAnnotation != null) {
            basePath = StringUtils.trim(typeAnnotation.value());
        }

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            HandlerMapping methodAnnotation = method.getAnnotation(HandlerMapping.class);
            if (methodAnnotation == null) {
                // do nothing
                continue;
            }

            String thisPath = methodAnnotation.value();

            if (!StringUtils.endsWith(basePath, "/") && !StringUtils.startsWith(thisPath, "/")) {
                thisPath = "/" + thisPath;
            }

            String path = basePath + thisPath;

            MessageHandlerManager.put(path, bean, method);
        }

        return bean;
    }
}
