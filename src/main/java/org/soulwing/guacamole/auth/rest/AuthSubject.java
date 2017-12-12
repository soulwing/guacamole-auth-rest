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

import java.util.List;
import java.util.Map;

/**
 * An authorization subject as defined by the REST API specification in the
 * top level README.
 */
interface AuthSubject {

  /**
   * Gets the username of the subject user.
   *
   * @return
   *    The username or null if not available.
   */
  String getUsername();

  /**
   * Gets the password of the subject user.
   *
   * @return
   *    The password or null if not available.
   */
  String getPassword();

  /**
   * Gets the remote address of the subject.
   *
   * @return
   *    The remote address or null if not available.
   */
  String getRemoteAddress();

  /**
   * Gets the remote hostname of the subject.
   *
   * @return
   *    The remote hostname or null if not available.
   */
  String getRemoteHostname();

  /**
   * Gets the map of HTTP request headers.
   *
   * @return
   *    The header map, or null if there is no HTTP request associated with the
   *    subject.
   */
  Map<String, List<String>> getHeaders();

}
