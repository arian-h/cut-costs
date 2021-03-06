import history from '../history';
import { isNumeric } from './common_utils';

const AT_LEAST_ONE_SMALL_LETTER_PATTERN = /^(?=.*[a-z]).+$/;
const AT_LEAST_ONE_CAPTIAL_LETTER_PATTERN = /^(?=.*[A-Z]).+$/;
const AT_LEAST_ONE_DIGIT_PATTERN = /^(?=.*\d).+$/;
const STARTS_WITH_ALPHABET = /^[a-zA-Z]+.*$/;
const ENDS_WITH_ALPHANUMERIC = /^.*[a-zA-Z0-9]+$/;
const AT_MOST_ONE_DOT = /^([a-zA-Z0-9]+(\.[a-zA-Z0-9]+)?)$/;

export const validateAmount = amount => {
  amount = amount || '';
  if ( !isNumeric(amount) || amount <= 0) {
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

export const validateImage = imageList => {
  if (imageList) {
    if (imageList.length > 1) {
      return "You can upload one image at a time";
    } else if (imageList.length === 1) {
      let selectedImage = imageList[0];
      if (!selectedImage.type.match('image.*')) {
        return "Only image files are allowed";
      } else if (selectedImage.size > 1048576) {
        return "Maximum file size exceeded";
      }
    }
  }
};
