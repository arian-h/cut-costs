import React from 'react';
import _ from 'lodash';
import Dropzone from 'react-dropzone';

export const renderInputField = function({ input, id, placeholder, label, type, meta: { touched, error } }) {
  return renderField(<input {...input} id={id} type={type} placeholder={placeholder} />, id, label, touched, error);
}

export const renderTextAreaField = function({ input, id, placeholder, label, meta: { touched, error } }) {
  return renderField(<textarea {...input} id={id} placeholder={placeholder} />, id, label, touched, error);
}

export const renderDropzoneField = function ({ input, previewUrl, name, label, id, meta: { dirty, error } }) {
  return (
    <div>
      <Dropzone
        name={name}
        onDrop={filesToUpload => input.onChange(filesToUpload)}
        accepts="image/*"
        maxSize={1048576}
      >
        { previewUrl ? <img src={ previewUrl } className="image-preview"/> : <span>{ label }</span> }
      </Dropzone>
      {dirty &&
        (error && <span>{error}</span>)
      }
    </div>
  );
}

const renderField = function(input, id, label, touched, error) {
  return (
    <div>
      <label htmlFor={id}>{label}</label>
      <div>
        { input }
        { touched && error && <span>{error}</span> }
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
