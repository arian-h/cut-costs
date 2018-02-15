import React from 'react';
import _ from 'lodash';

export const renderField = function({ input, fieldType, label, type, meta: { touched, error }, ...rest }) {
  //TODO how to use fieldType instead of input
  let FieldType = fieldType;
  return (
    <div>
      <label>{label}</label>
      <div>
        <FieldType {...input} {...rest} type={type} />
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
