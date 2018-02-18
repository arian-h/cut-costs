import React from 'react';
import _ from 'lodash';

export const renderField = function({ input, fieldType, label, type, meta: { touched, error }}) {
  let FieldType = fieldType;
  return (
    <div>
      <label>{label}</label>
      <div>
        <FieldType {...input} type={type} />
        {touched && error && <span>{error}</span>}
      </div>
    </div>
  );
}

export const validate = (values, props) => {
  const { validators } = props;
  let errors = {};
  _.each(validators, ({ validator, field }) => {
    errors[field] = validator(values[field]);
  });
  return errors;
}
