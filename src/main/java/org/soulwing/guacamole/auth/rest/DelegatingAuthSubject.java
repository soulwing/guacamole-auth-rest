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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;

import org.apache.guacamole.net.auth.Credentials;

/**
 * An authorization subject that delegates to a {@link Credentials} object.
 */
public class DelegatingAuthSubject implements AuthSubject {

  /** Lock used in lazy initialization of {@link #headers} field. */
  private final Lock lock = new ReentrantLock();

  /** Credentials to which accessor methods will delegate. */
  private final Credentials credentials;

  /** Lazily initialized headers extract from the delegate. */
  private volatile Map<String, List<String>> headers;


  /**
   * Constructs a new instance that delegates to the given credentials.
   *
   * @param credentials
   *    The credentials delegate.
   */
  DelegatingAuthSubject(Credentials credentials) {
    this.credentials = credentials;
  }

  /**
   * Gets the username of the subject user.
   *
   * @return
   *    The username or null if not available.
   */
  @Override
  public String getUsername() {
    return credentials.getUsername();
  }

  /**
   * Gets the password of the subject user.
   *
   * @return
   *    The password or null if not available.
   */
  @Override
  public String getPassword() {
    return credentials.getPassword();
  }

  /**
   * Gets the remote address of the subject.
   *
   * @return
   *    The remote address or null if not available.
   */
  @Override
  public String getRemoteAddress() {
    return credentials.getRemoteAddress();
  }

  /**
   * Gets the remote hostname of the subject.
   *
   * @return
   *    The remote hostname or null if not available.
   */
  @Override
  public String getRemoteHostname() {
    return credentials.getRemoteHostname();
  }

  /**
   * Gets the map of HTTP request headers.
   *
   * @return
   *    The header map, or null if there is no HTTP request associated with the
   *    subject.
   */
  @Override
  public Map<String, List<String>> getHeaders() {
    final HttpServletRequest request = credentials.getRequest();
    if (request == null) return null;
    if (headers == null) {
      lock.lock();
      try {
        if (headers == null) {
          headers = createHeaderMap(request);
        }
      }
      finally {
        lock.unlock();
      }
    }
    return headers;
  }

  /**
   * Creates a map of the header values in an HTTP request.
   * <p>
   * The resulting Map has as its keys the names of the headers in the request.
   * Associated with each key is a List of Strings corresponding to the values
   * for the given header.
   *
   * @param request
   *    The subject HTTP request
   *
   * @return
   *    Map of the headers in the given request.
   */
  private Map<String, List<String>> createHeaderMap(
      HttpServletRequest request) {
    final Map<String, List<String>> map =
        new LinkedHashMap<String, List<String>>();
    final Enumeration headers = request.getHeaderNames();
    while (headers.hasMoreElements()) {
      final String name = (String) headers.nextElement();
      final Enumeration values = request.getHeaders(name);
      while (values.hasMoreElements()) {
        final String value = (String) values.nextElement();
        List<String> list = map.get(name);
        if (list == null) {
          list = new ArrayList<String>();
          map.put(name, list);
        }
        list.add(value);
      }
    }
    return map;
  }

}
