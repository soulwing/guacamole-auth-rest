/*
 * File created on Dec 12, 2017
 *
 * Copyright (c) 2017 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.guacamole.auth.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.guacamole.net.auth.Credentials;

/**
 * Utility class for creating {@link AuthSubject} instances with fixed values
 * for unit tests.
 */
class AuthSubjectUtil {

  static final String USERNAME = "username";
  static final String PASSWORD = "password";
  static final String REMOTE_ADDRESS = "remoteAddress";
  static final String REMOTE_HOSTNAME = "remoteHostname";

  public static AuthSubject newAuthSubject(HttpServletRequest request) {
    final Credentials credentials = new Credentials(USERNAME, PASSWORD, request);
    credentials.setRemoteAddress(REMOTE_ADDRESS);
    credentials.setRemoteHostname(REMOTE_HOSTNAME);

    return new DelegatingAuthSubject(credentials);
  }

}
