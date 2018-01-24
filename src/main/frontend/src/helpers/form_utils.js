import React from 'react';
import _ from 'lodash';

export const renderField = function(fieldConfig, fieldName) {
  //fieldHelper is provided by redux-form
  const fieldHelper = this.props.fields[fieldName];
  const {touched, invalid, error} = fieldHelper;
  const className = `form-group ${touched && invalid ? 'has-danger':''}`;

  let propKeys = _.keys(fieldConfig.props); //bind all callbacks passed in fieldConfig.props to 'this'
  for (let i = 0; i < propKeys.length ;i++) {
    if (fieldConfig.props[propKeys[i]].bind) {
      fieldConfig.props[propKeys[i]] = fieldConfig.props[propKeys[i]].bind(this);
    }
  }

  return (
    <div className={className} key={fieldName}>
      <label>{fieldConfig.label}</label>
      <fieldConfig.fieldType
        {...fieldHelper}
        {...fieldConfig.props}
      />
      <div className="text-help">
        {touched ? error[fieldName]:''}
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
