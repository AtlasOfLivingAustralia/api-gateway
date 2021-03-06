# Atlas API Gateway

This repository is an issues & documentation repository for managing the delivery of the API Gateway for the Atlas.

The API gateway will provide a **single entry point** for all Atlas webservice services and will include an **enhanced authentication model** for web service users.
There will be two types of access supported: 
1. OIDC access - browser based 
2. Non-OIDC access - non-browser based

## OpenID Connect (OIDC) access

For browser or mobile applications based applications that can make use of OAuth SDKs or Spring Security to generate [JSON Web Tokens (JWT)](https://en.wikipedia.org/wiki/JSON_Web_Token).

<img width="999" alt="Screen Shot 2021-11-02 at 12 02 19 pm" src="https://user-images.githubusercontent.com/444897/139842638-e96e1d4b-da73-48ea-b1db-56cd4f5e1e4a.png">

## Non-OIDC based access

For applications that are not using a browser, there will be services allowing for the creation of JSON Web Tokens either by using [Basic Authentication](https://en.wikipedia.org/wiki/Basic_access_authentication) or an API Key/Secret mechanism (both have been protoyped - decision pending). Once generated, the JWT will be submitted with a request in the ```Authorization``` header. 

<img width="813" alt="Screen Shot 2021-11-02 at 2 44 17 pm" src="https://user-images.githubusercontent.com/444897/139869922-468bae10-c2c2-4e16-ba3c-0b775be78bf5.png">


# Authentication flow

All requests through the API Gateway will require both a JWT and an API Key.

### API key
The API key is only used for monitoring and will be only checked at the Gateway level. The backend webservices will **not** make use of this API key for authorisation or authentication. The API key will be submitted using the ```x-api-key``` header. 

### JSON Web Token (JWT)
JWT will need to be generated by applications and submitted with requests using the ```Authorization``` header. 
The JSON web token will contain user ID and Roles. The JWT will be accessible to backend webservices. This will allow the backend end webservices to apply their own access control based on roles and/or user ID.  
#### Examples of app-based access control: 
* Project level access in Ecodata
* Sensitive data access for a specific region in the Biocache service.

#### JWT Verification and accessing roles/userID
The verification of the JWT and extraction of userID and roles will be done by a Grails plugin for grails based applications (see issues#1) which should limited the amount of JWT logic spread in Atlas applications.

<img width="297" alt="Screen Shot 2021-11-02 at 12 03 02 pm" src="https://user-images.githubusercontent.com/444897/139842699-d146a824-7856-400e-9c3b-444df6fee211.png">


### Example JWT

A JSON Web Token is signed allowing the backend webservices to verify its source using the signature. The unpacked contents of the JWT will look this:

```json

{
  "country": "AU",
  "role": [
    "ROLE_COLLECTIONS_ADMIN",
    "ROLE_USER"
  ],
  "city": "Sydney",
  "iss": "auth.ala.org.au",
  "organisation": "MyOrg",
  "given_name": "Derek",
  "userid": 43956,
  "aud": "oidc-test-client-id",
  "nbf": 1635854927,
  "authority": "ROLE_COLLECTIONS_ADMIN,ROLE_USER",
  "state": "NSW",
  "exp": 1635854927,
  "iat": 1635853127,
  "family_name": "Smith",
  "email": "my@email.com"
}

```

# Registering Applications and Users

Both user and applications will be registered as "users" in the [UserDetails](https://github.com/atlasoflivingaustralia/userdetails) app. 
Roles will be assignable to applications in the same way they are currently assigned to users.
An application will need to create an ALA account to obtain an API key which will be sent with each request. 


# Legacy API Keys

The current system for Atlas-application-to-Atlas-application authentication which uses the [apikey](https://github.com/atlasoflivingaustralia/apikey), which is only used internally (i.e. not accessible for the third parties) will be phased out over time.
Support in the short term for legacy API keys will continue (through the [ala-ws-security-plugin](https://github.com/atlasoflivingaustralia/ala-ws-security-plugin) - see #1) but will gradually be phased out in favour of JWT support.
This means that the Authorizer implemented in Gateway will, in the short term, allow authorisation using the legacy API Key method until a time when the dependent front end applications have been modified to use Spring Security / OIDC and JWT.



