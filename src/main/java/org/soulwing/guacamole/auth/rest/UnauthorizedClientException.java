/*
 * File created on Dec 9, 2017
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

/**
 * An exception thrown by an {@link AuthService} when this provider (acting
 * in the role of client) must authenticate before making another authorization
 * request.
 */
class UnauthorizedClientException extends Exception {

  /**
   * Challenge that was received in the REST service response which indicated
   * authentication was needed.
   */
  private final ClientChallenge challenge;

  /**
   * Constructs a new instance using the challenge received from the REST
   * service.
   *
   * @param challenge
   *    The authentication challenge issued by the REST service.
   *
   */
  public UnauthorizedClientException(ClientChallenge challenge) {
    super("Client must authenticate before proceeding");
    this.challenge = challenge;
  }

  /**
   * Gets the authentication challenge that was issued by the REST service.
   *
   * @return
   *    Challenge object (never null)
   */
  public ClientChallenge getChallenge() {
    return challenge;
  }

}
