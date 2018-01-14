import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';

import { createGroup } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderField, validate } from '../../helpers/form_utils';

const FIELDS = {
  name: {
    type: 'text',
    label: 'Name',
    validate: validateName
  },
  description: {
    type: 'text',
    label: 'Description',
    validate: validateDescription
  }
};

class NewGroup extends Component {

  onSubmit(values) {
    const { history } = this.props;
    this.props.createGroup(values, ({status, headers}) => {
      // if (status === 200) {
      //   history.push('/');
      // }
    });
  }

  render() {
    const { handleSubmit } = this.props;
    return (
      <div>
        <form onSubmit={handleSubmit(this.onSubmit.bind(this))}>
          {_.map(FIELDS, renderField.bind(this))}
          <button type="submit" className="btn btn-primary">Create</button>
          <button className="btn btn-primary">Cancel</button>
        </form>
      </div>
    );
  }
}

export default reduxForm({
  validate,
  //a unique id for this form
  form:'NewGroup',
  fields: _.keys(FIELDS),
  fields_def: FIELDS
})(
  connect(null, { createGroup })(NewGroup)
);
