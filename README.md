guacamole-auth-rest
===================

An auth provider extension for [Guacamole](http://guacamole.apache.org) that 
delegates to a simple REST API.

## Installation

Build the extension using Maven:
```
mvn package
```

After the build the extension and its dependencies will be in the `target/`
directory of the build.

Install into GUACAMOLE_HOME:

```
cp target/guacamole-auth-rest-1.0.0-SNAPSHOT.jar $GUACAMOLE_HOME/extensions/
cp target/dependency/* $GUACAMOLE_HOME/lib/
```

## REST API Specification

This auth provider relies on a REST service with a simple, well-defined API.
The provider sends a JSON object describing a subject to be authenticated 
and authorized in an HTTP GET request. The REST service performs whatever steps
are necessary to authenticate the subject and to determine the Guacamole
connection resources that are authorized for the user and returns a JSON 
object describing the result. 

### POST /authorization

Authenticates a _subject_ and determines the subject's authorized resources,
returning an _authorization result_.

The entity body for the request will contain a _subject_ as follows.

```
{
  "username": "<user name of the subject>",
  "password": "<password for the user, if any>",
  "remoteAddress": "<network address at which the subject is located, if available>",
  "remoteHostname": "<network host at which the subject is located, if available>",
  "request": {      // details of the HTTP request
    "headers": [
      "<header name>": [ "<value>", ... ]
    ]
  }
}
```
  
Note that because a given HTTP header may have multiple values, the object
associated with a header name is an array of strings, even if there is just 
one value for a given header name.

The structure of the _subject_ corresponds to the 
[Credentials](http://guacamole.apache.org/doc/guacamole-ext/org/apache/guacamole/net/auth/Credentials.html)
interface defined in [guacamole-ext](http://guacamole.apache.org/doc/guacamole-ext).

The REST service responds with an HTTP status code that indicates whether the
requested authorization was actually performed.

| Status   | Meaning                                                      |
| -------: | ------------------------------------------------------------ |
|   200    | Response entity body contains an _authorization result_      |
|   401    | Client must authenticate before attempting to authorize a subject; see [Client Authentication](#client-authentication)
|   4xx    | Authorization was not performed due to a client error; response entity body may describe the error |
|   5xx    | Authorization was not performed due to a service error; response entity body may describe the error |


When the response status is 200, the entity body of the response will contain
an _authorization result_ with the following structure.

```
{
  "authorized": <true|false>,
  "configurations": {
     "<connection name>": <connection resource>,
     ... 
  }
}
```

The _authorized_ flag indicates the overall success of the authorization. Note
that it deliberately includes no specifics -- the provider is simply informed 
as to whether the subject is authorized. This design ensures that no useful 
information can be leaked through the Guacamole UI that might help a malicious 
person in mounting an attack.

The _configurations_ object is included only for an authorized subject. Each
named object within _configurations_ describes an authorized connection resource.
The structure of each _connection resource_ is as follows.

```
  "protocol": "<Guacamole protocol name; e.g. RDP, VNC, SSH>",
  "parameters": {
    "<parameter name>": <parameter value>,
    ...
  }
```

The parameter names and corresponding value types are protocol-specific, and are 
typically defined and described in the Guacamole documentation for a supported 
protocol. The value data types may be either strings, numbers, or booleans. This
structure corresponds to the [GuacamoleConfiguration](http://guacamole.apache.org/doc/guacamole-common/org/apache/guacamole/protocol/GuacamoleConfiguration.html) defined in 
[guacamole-common](http://guacamole.apache.org/doc/guacamole-common).

### Client Authentication

The REST service may require the auth provider (as a client of the service) to 
authenticate before it will provide authorization responses. The supported 
authentication mechanisms are those supported by the HTTP protocol through the 
use of the `WWW-Authenticate` and `Authorization` headers; e.g. HTTP Basic
([RFC 7617](https://tools.ietf.org/html/rfc7617)), HTTP Digest
([RFC 7616](https://tools.ietf.org/html/rfc7616)), 
OAuth 2 authentication ([RFC 6749](https://tools.ietf.org/html/rfc6749)).

The REST service indicates that client authentication is required by returning
a 401 status code. The response headers _must_ include a `WWW-Authenticate`
header which describes the authentication challenge. The auth provider will 
utilize this header to determine how to authenticate itself and will make a
subsequent request with an appropriate `Authorization` header.

In the case of OAuth2, the provider implementation will make use of the `realm` 
parameter given in the `WWW-Authenticate` header in determining where to make 
the request for an access token. If the `realm` parameter starts with an `http:` 
or `https:` scheme and otherwise has the form of an HTTP URL, it will make a 
request for an access token using this URL. Otherwise, the realm string will be 
used to deduce the URL for the access token service from provider configuration 
properties. 

When requesting an OAuth2 access token, the provider may also be challenged to 
authenticate itself. In this case, the only supported mechanisms for 
authenticating the provider are the HTTP Basic and Digest mechanisms. 


## Provider Configuration

All configuration properties for the provider are defined below. These property
names and their corresponding values may be placed in the `guacamole.properties`
file. Alternatively, the value of each property may be provided via an 
equivalent environment variable name. The environment variable that corresponds
to a configuration property is formed by converting the property name to all 
upper case letters and replacing hyphens with underscores. For example, the
`auth-rest-service-url` configuration property may be specified in the 
environment as `AUTH_REST_SERVICE_URL`.


### Service Properties

* `auth-rest-service-url` -- The absolute URL for the REST service that is used
  to authorize subject users.
* `auth-rest-authorization-uri` -- The URI path for the authorization resource; 
  if not specified this defaults to `/authorization`.

### Basic Authentication Properties

The auth provider can authenticate itself to the REST service using HTTP Basic
authentication.

* `auth-rest-basic-username` -- The username to send in the response to a
  Basic authentication challenge.
* `auth-rest-basic-password` -- The password to send in the response to a
  Basic authentication challenge.

### Digest Authentication Properties

The auth provider can authenticate itself to the REST service using HTTP Digest
authentication.

* `auth-rest-digest-username` -- The username to send in the response to a
  Digest authentication challenge.
* `auth-rest-digest-password` -- The password to use in computing the response
  to a Digest authentication challenge.

### OAuth2 Authentication Properties

The auth provider can authenticate itself to the REST service using OAuth2 
authentication. 

An OAuth2 implementation can provide the URL for service used to request an
access token in the `Realm` parameter of the `WWW-Authenticate` header. If the
`Realm` parameter begins with `http:` or `https:` and otherwise has the form
of an HTTP URL, the parameter value will be used as the URL to use in making 
a request for an access token. Otherwise, the provider will use configuration
properties to determine the location of the access token service. 

The access token may also issue an authentication challenge by responding with
a 401 status code and including a `WWW-Authenticate` header. In this case the
only supported authentication types are HTTP Basic and Digest authentication,
and the provider will response to the challenge using the corresponding 
configuration properties as given in 
[Basic Authentication Properties](#basic-authentication-properties)
and [Digest Authentication Properties](#digest-authentication-properties).

* `auth-rest-oauth2-service-url` -- The absolute URL for the access token 
  service. This property is used only if the authentication challenge given
  in the `WWW-Authenticate` header contains a `Realm` that is *not* a valid
  HTTP URL.
                                              
