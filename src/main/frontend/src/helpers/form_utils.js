import React from 'react';
import _ from 'lodash';
import Dropzone from 'react-dropzone';
import { Form, Message, Input, TextArea } from 'semantic-ui-react'

export const renderInputField = function({ input, transparent, size, id, placeholder, label, type, meta: { touched, error } }) {
    return renderField(<Input size={ size } transparent={transparent} { ...input } id={ id } type={ type } placeholder={ placeholder } />, id, label, touched, error);
}

export const renderTextAreaField = function({ input, rows, transparent, id, placeholder, label, meta: { touched, error } }) {
  return renderField(<TextArea rows={ rows } className={ transparent ? "transparent-textarea" : undefined } { ...input } id={ id } placeholder={ placeholder } />, id, label, touched, error);
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
  let displayError = touched && error;
  let errorMessage, inputField;
  if (displayError) {
    errorMessage = <Message
      error
      content={error}
    />;
    inputField = <Form.Field error>
      <label htmlFor={ id }>{ label }</label>
      { input }
    </Form.Field>;
  } else {
    inputField = <Form.Field>
      <label htmlFor={ id }>{ label }</label>
      { input }
    </Form.Field>;
  }
  return (
    <div>
      { inputField }
      { errorMessage }
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
