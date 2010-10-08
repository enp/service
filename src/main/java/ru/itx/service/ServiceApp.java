/*
 * Copyright (c) 2010 Eugene Prokopiev <enp@itx.ru>
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

package ru.itx.service;

import ru.itx.beans.Bean;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceApp {

	private Bean bean;

	public ServiceApp() throws Exception {
		updateClassPath();
		bean = new Bean();
	}

	private void updateClassPath() throws Exception {
		File lib = new File("lib");
		if (lib.exists()) {
			for (File entry : lib.listFiles())
				addClassPathEntry(entry.toURI().toURL());
		}
		File conf = new File("conf");
		if (conf.exists())
			addClassPathEntry(conf.toURI().toURL());
	}

	private void addClassPathEntry(URL url) throws Exception {
		URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		Class sysclass = URLClassLoader.class;
		Method method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
		method.setAccessible(true);
		method.invoke(sysloader, new Object[]{url});
	}

	public void init(String[] args) {
		bean.init();
	}

	public void start() {
		bean.start();
	}

	public void stop() {
		bean.stop();
	}

	public void destroy() {
		bean.destroy();
	}

	public static void main(String[] args) throws Exception {
		final ServiceApp serviceApp = new ServiceApp();
		serviceApp.init(args);
		serviceApp.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				serviceApp.stop();
				serviceApp.destroy();
			}
		});
		Thread.sleep(Long.MAX_VALUE);
	}
}
