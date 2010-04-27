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

public class ServiceApp {

	public void init(String[] args) {
		System.out.println("init");
	}

	public void start() {
		System.out.println("start");
	}

	public void stop() {
		System.out.println("stop");
	}

	public void destroy() {
		System.out.println("destroy");
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
