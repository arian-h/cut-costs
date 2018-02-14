import validator from 'validator';

const AT_LEAST_ONE_SMALL_LETTER_PATTERN = /^(?=.*[a-z]).+$/;
const AT_LEAST_ONE_CAPTIAL_LETTER_PATTERN = /^(?=.*[A-Z]).+$/;
const AT_LEAST_ONE_DIGIT_PATTERN = /^(?=.*\d).+$/;
const STARTS_WITH_ALPHABET = /^[a-zA-Z]+.*$/;
const ENDS_WITH_ALPHANUMERIC = /^.*[a-zA-Z0-9]+$/;
const AT_MOST_ONE_DOT = /^([a-zA-Z0-9]+(\.[a-zA-Z0-9]+)?)$/;

export const validatePassword = (password) => {
  password = password || '';
  if (!validator.isLength(password, {min:8, max: 30})) {
    return "Password must be between 8 and 30 characters long";
  } else if (!validator.matches(password, AT_LEAST_ONE_SMALL_LETTER_PATTERN)) {
    return "Password must contain at least one small letter";
  } else if (!validator.matches(password, AT_LEAST_ONE_CAPTIAL_LETTER_PATTERN)) {
    return "Password must contain at least one capital letter";
  } else if (!validator.matches(password, AT_LEAST_ONE_DIGIT_PATTERN)) {
    return "Password must contain at least one digit";
  }
};

export const validateEmail = (email) => {
  email = email || '';
  if (!validator.isEmail(email)) {
    return "Invalid email address";
  }
};

export const validateName = (name) => {
  name = name || '';
  if (!validator.isLength(name, {min:8, max: 15})) {
    return "Username must be between 8 and 15 characters long";
  } else if (!validator.matches(name, STARTS_WITH_ALPHABET)) {
    return "Username must start with a letter";
  } else if (!validator.matches(name, ENDS_WITH_ALPHANUMERIC)) {
    return "Username must end with a letter or a digit";
  } else if (!validator.matches(name, AT_MOST_ONE_DOT)) {
    return "Username can contain at most one dot";
  }
};

export const isAuthenticated = () => {
  return !!localStorage.getItem('jwt_token');
};

export const logout = () => {
  localStorage.removeItem('jwt_token');
  history.push('/login');
}

export const login = (authorization, redirected_from) => {
  localStorage.setItem('jwt_token', authorization);
  history.push(redirected_from);
}
