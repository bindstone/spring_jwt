import React, { useEffect, useState, useRef } from 'react';
import axios from 'axios';
import keycloak from './keycloak';

const App: React.FC = () => {
  const [authenticated, setAuthenticated] = useState<boolean>(false);
  const [token, setToken] = useState<string | null>(null);
  const [message, setMessage] = useState<string>('');
  const isInitialized = useRef(false);

  useEffect(() => {
    if (isInitialized.current) return;
    isInitialized.current = true;

    keycloak.init({ onLoad: 'check-sso' }).then((auth: boolean) => {
      setAuthenticated(auth);
      if (auth) {
        setToken(keycloak.token || null);
        console.log('Authenticated');
      } else {
        console.log('Not authenticated');
      }
    }).catch(() => {
      console.error('Authenticated Failed');
    });
  }, []);

  const callApi = async (endpoint: string) => {
    try {
      const config = token ? { headers: { Authorization: `Bearer ${token}` } } : {};
      const response = await axios.get(`http://localhost:8080${endpoint}`, config);
      setMessage(`Success: ${response.data}`);
    } catch (error: any) {
      if (error.response && error.response.status === 401) {
        setMessage('Access Denied. Redirecting to login...');
        keycloak.login();
      } else {
        setMessage(`Error: ${error.message}`);
      }
    }
  };

  const handleLogout = () => {
    setToken(null);
    keycloak.logout();
  };

  return (
    <div style={{ padding: '20px' }}>
      <h1>React + Keycloak + Spring Boot</h1>
      <div>
        <button onClick={() => callApi('/public')}>Call Public API</button>
        <button onClick={() => callApi('/private')}>Call Private API</button>
        <button onClick={() => callApi('/admin')}>Call Admin API</button>
        <button onClick={handleLogout}>Logout</button>
      </div>
      <div style={{ marginTop: '20px', padding: '10px', border: '1px solid #ccc' }}>
        <strong>Message:</strong> {message}
      </div>
      {authenticated && (
        <div style={{ marginTop: '20px' }}>
          <strong>Token:</strong>
          <pre style={{ wordBreak: 'break-all', whiteSpace: 'pre-wrap' }}>{token}</pre>
        </div>
      )}
    </div>
  );
};

export default App;