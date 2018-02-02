export const validateName = name => {
  name = name || '';
  if (name.length < 5 || name.length > 25) {
    return "Group name must be between 5 and 25 characters";
  }
  return null;
};

export const validateDescription = description => {
  description = description || '';
  if (description.length > 200) {
    return "Group description cannot be longer than 200 characters";
  }
  return null;
};
