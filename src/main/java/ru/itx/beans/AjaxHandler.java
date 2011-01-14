/*
 * Copyright (c) 2011 Eugene Prokopiev <enp@itx.ru>
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

package ru.itx.beans;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AjaxHandler extends AbstractHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<Continuation> continuations = new ArrayList<Continuation>();

	public AjaxHandler() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while(true){
					try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
					logger.debug("waiting requests: {}", continuations);
					for(Continuation continuation : continuations) {
						if (continuation.isSuspended()) {
							logger.debug("async resume: {}", continuation);
							continuation.resume();
						}
					}
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		logger.debug("target : {}", target);
		if (target.equals("/ajax-sync")) {
			try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
			logger.debug("sync");
			processRequest(baseRequest, response);
		} else if (target.equals("/ajax-async")) {
			Continuation continuation = ContinuationSupport.getContinuation(request);
			if (continuation.isInitial()) {
				continuations.add(continuation);
				logger.debug("async suspend: {}", continuation);
				continuation.suspend();
				continuation.undispatch();
			} else {
				logger.debug("async continue: {}", continuation);
				continuations.remove(continuation);
				processRequest(baseRequest, response);
			}
		}
	}

	private void processRequest(Request baseRequest, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println(new Date());
	}
}
