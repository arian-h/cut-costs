import React from 'react';

export const renderField = function(fieldConfig, field) {
  //this is provided by redux-form
  const fieldHelper = this.props.fields[field];
  const {touched, invalid, error} = fieldHelper;
  const className = `form-group ${touched && invalid ? 'has-danger':''}`;
  return (
    <div className={className} key={field}>
      <label>{fieldConfig.label}</label>
      <input className="form-control"
        type={fieldConfig.type}
        {...fieldHelper}
      />
      <div className="text-help">
        {touched ? error[field]:''}
      </div>
    </div>
  );
}

export const validate = (values, props) => {
  const { fields_def } = props;
  let errors = {};
  _.each(fields_def, (val, key) => {
    errors[key] = val.validate(values[key]);
  });
  return errors;
}
