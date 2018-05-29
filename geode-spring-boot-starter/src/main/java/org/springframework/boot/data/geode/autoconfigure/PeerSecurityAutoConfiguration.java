/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.springframework.boot.data.geode.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.config.annotation.ApacheShiroSecurityConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.data.gemfire.config.annotation.GeodeIntegratedSecurityConfiguration;
import org.springframework.lang.Nullable;

/**
 * Spring Boot {@link EnableAutoConfiguration auto-configuration} enabling Apache Geode's Security functionality,
 * and specifically Authentication between a client and server using Spring Data Geode Security annotations.
 *
 * @author John Blum
 * @see org.apache.geode.security.SecurityManager
 * @see org.springframework.beans.factory.config.BeanPostProcessor
 * @see org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.data.gemfire.CacheFactoryBean
 * @see org.springframework.data.gemfire.client.ClientCacheFactoryBean
 * @see org.springframework.data.gemfire.config.annotation.ApacheShiroSecurityConfiguration
 * @see org.springframework.data.gemfire.config.annotation.EnableSecurity
 * @see org.springframework.data.gemfire.config.annotation.GeodeIntegratedSecurityConfiguration
 * @see org.springframework.data.gemfire.config.annotation.PeerCacheConfigurer
 * @since 1.0.0
 */
@Configuration
@ConditionalOnBean(org.apache.geode.security.SecurityManager.class)
@ConditionalOnMissingBean({
	ClientCacheFactoryBean.class,
	ApacheShiroSecurityConfiguration.class,
	GeodeIntegratedSecurityConfiguration.class
})
@EnableSecurity(securityManagerClassName = "org.springframework.boot.data.geode.security.support.SecurityManagerProxy")
@SuppressWarnings("unused")
public class PeerSecurityAutoConfiguration {

	@Bean
	@SuppressWarnings("all")
	BeanPostProcessor cacheFactoryBeanPostProcessor() {

		return new BeanPostProcessor() {

			@Nullable @Override
			public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

				if (bean instanceof CacheFactoryBean) {
					((CacheFactoryBean) bean).setUseBeanFactoryLocator(true);
				}

				return bean;
			}
		};
	}

	/*
	PeerCacheConfigurer gemfireCacheUseBeanFactoryLocatorConfigurer() {
		return (beanName, cacheFactoryBean) -> cacheFactoryBean.setUseBeanFactoryLocator(true);
	}
	*/

}