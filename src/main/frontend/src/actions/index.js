import axios from 'axios';

export const LOGIN_USER = 'login_user';

const ROOT_URL = "http://localhost:8443";
const AUTH_ENDPOINT_URL = `${ROOT_URL}/auth`;
const LOGIN_ENDPOINT = `${AUTH_ENDPOINT_URL}/login`;

export function loginUser(values, callback) {
  const request = axios.post(LOGIN_ENDPOINT,
    {
      username: values.username,
      password: values.password
    }
  );
  return {
    type: LOGIN_USER,
    payload: request
  };
}
