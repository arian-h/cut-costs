import history from '../history';

const AT_LEAST_ONE_SMALL_LETTER_PATTERN = /^(?=.*[a-z]).+$/;
const AT_LEAST_ONE_CAPTIAL_LETTER_PATTERN = /^(?=.*[A-Z]).+$/;
const AT_LEAST_ONE_DIGIT_PATTERN = /^(?=.*\d).+$/;
const STARTS_WITH_ALPHABET = /^[a-zA-Z]+.*$/;
const ENDS_WITH_ALPHANUMERIC = /^.*[a-zA-Z0-9]+$/;
const AT_MOST_ONE_DOT = /^([a-zA-Z0-9]+(\.[a-zA-Z0-9]+)?)$/;

export const validateAmount = amount => {
  amount = amount || '';
  if ( !inNumeric(amount) || amount <= 0) {
    return "Expense amount must be a positive number";
  }
};

export const validateName = name => {//TODO see if we can use validator.isLength instead
  name = name || '';
  if (name.length < 5 || name.length > 30) {
    return "Expense name must be between 5 and 30 characters";
  }
};

export const validateDescription = description => {
  description = description || '';
  if (description.length > 200) {
    return "Expense description must be shorter than 200 characters";
  }
};

const inNumeric = value => {
  return !isNaN(parseFloat(value)) && isFinite(value);
}
