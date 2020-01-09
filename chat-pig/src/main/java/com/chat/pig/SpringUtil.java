package com.chat.pig;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 描述: 提供手动获取被spring管理的bean对象
 *
 * @author unkown
 * @date 2019/12/25
 */
public class SpringUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtil.applicationContext == null) {
			SpringUtil.applicationContext = applicationContext;
		}
	}

    /**
     * 获取applicationContext
     * @return ApplicationContext
     */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

    /**
     * 通过name获取 Bean
     * @param name beanClass
     * @return bean
     */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

    /**
     * 通过class获取 Bean
     * @param clazz beanName
     * @return bean
     */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}
    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name beanName
     * @param clazz beanClass
     * @param <T> 泛型
     * @return bean
     */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

}
