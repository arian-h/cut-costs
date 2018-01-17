import validator from 'validator';

export const validateName = (name) => {
  let errors = {};
  name = name || '';
  if (name.length < 5 || name.length > 25) {
    errors['name'] = "Group name must be between 5 and 25 characters";
  }
  return errors;
};

export const validateDescription = (description) => {
  let errors = {};
  description = description || '';
  if (description.length > 200) {
    errors['description'] = "Group description cannot be longer than 200 characters";
  }
  return errors;
};
