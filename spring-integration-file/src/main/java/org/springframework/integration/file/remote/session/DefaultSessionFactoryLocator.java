/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.file.remote.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default implementation of {@link SessionFactoryLocator} using a simple map lookup
 * and an optional default to fall back on.
 *
 * @author Gary Russell
 * @since 4.2
 *
 */
public class DefaultSessionFactoryLocator<F> implements SessionFactoryLocator<F> {

	private final Map<Object, SessionFactory<F>> factories = new ConcurrentHashMap<Object, SessionFactory<F>>();

	private final SessionFactory<F> defaultFactory;

	/**
	 * @param factories A map of factories, keyed by lookup key.
	 */
	public DefaultSessionFactoryLocator(Map<Object, SessionFactory<F>> factories) {
		this(factories, null);
	}

	/**
	 * @param factories A map of factories, keyed by lookup key.
	 * @param defaultFactory A default to be used if the lookup fails.
	 */
	public DefaultSessionFactoryLocator(Map<Object, SessionFactory<F>> factories, SessionFactory<F> defaultFactory) {
		this.factories.putAll(factories);
		this.defaultFactory = defaultFactory;
	}

	/**
	 * Add a session factory.
	 * @param key the lookup key.
	 * @param factory the factory.
	 */
	public void addSessionFactory(String key, SessionFactory<F> factory) {
		this.factories.put(key, factory);
	}

	/**
	 * Remove a session factory.
	 * @param key the lookup key.
	 * @return the factory, if it was present.
	 */
	public SessionFactory<F> removeSessionFactory(Object key) {
		return this.factories.remove(key);
	}

	@Override
	public SessionFactory<F> getSessionFactory(Object key) {
		if (key == null) {
			return this.defaultFactory;
		}
		SessionFactory<F> factory = this.factories.get(key);
		return factory != null ? factory : this.defaultFactory;
	}

}
