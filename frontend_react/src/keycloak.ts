// @ts-ignore
import Keycloak from 'keycloak-js';

// @ts-ignore
const keycloak = new Keycloak({
  url: 'http://127.0.0.1:8888',
  realm: 'CONTINENTAL',
  clientId: 'CONTINENTAL-CLIENT',
});

export default keycloak;