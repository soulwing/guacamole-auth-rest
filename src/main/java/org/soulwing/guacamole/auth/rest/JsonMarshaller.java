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

import java.util.Map;

import org.apache.guacamole.GuacamoleException;

/**
 * A simple JSON marshaller.
 * <p>
 * This API supports only the marshalling operations required for the
 * REST auth protocol described in the top level README.
 * <p>
 * An implementation of this interface must be implemented such that a
 * single instance can be used concurrently by multiple threads.
 */
interface JsonMarshaller {

  /**
   * Creates a JSON representation of an {@link AuthSubject}.
   *
   * @param subject
   *    The subject to marshal.
   *
   * @return
   *    A JSON representation of the credentials in a UTF-8 encoded string.
   *
   * @throws GuacamoleException
   *    If an error occurs in creating the JSON representation.
   */
  String toJson(AuthSubject subject) throws GuacamoleException;

  /**
   * Creates a Map representation that corresponds to an arbitrary JSON object.
   * <p>
   * This operation recursively descends through the JSON object structure,
   * creating map entries with keys corresponding to the JSON object fields,
   * and values that are either String, Boolean, Number, List, or Map, based
   * on the corresponding value from the JSON object.
   *
   * @param json
   *    UTF-8 encoded string containing a single JSON object.
   *
   * @return
   *    A Map representation of the JSON object.
   *
   * @throws GuacamoleException
   *    If an error occurs in creating a map from the JSON representation
   *
   */
  Map toMap(String json) throws GuacamoleException;

}
