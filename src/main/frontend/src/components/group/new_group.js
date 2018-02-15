import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';

import { createGroup } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderField, validate } from '../../helpers/form_utils';

const validators = [{
    field: 'name',
    validator: validateName
  },
  {
    field: 'description',
    validator: validateDescription
  }
];

class NewGroup extends Component {

  constructor(props) {
    super(props);
    this.state = {
      errorMessage: null
    }
  }

  _onSubmit(values) {
    this.props.createGroup(values, () => this.props.history.push("/group"), errorMessage => this.setState({errorMessage: errorMessage}));
  }

  render() {
    let error = null;
    if (this.state.errorMessage != null) {
      error = <span>{this.state.errorMessage}</span>;
    }
    const { handleSubmit } = this.props;
    return (
      <div>
        <form onSubmit={handleSubmit(this._onSubmit.bind(this))}>
          <Field name="name" label="Name" type="text" fieldType="input" component={renderField.bind(this)}/>
          <Field name="description" label="Description" type="text" fieldType="input" component={renderField.bind(this)}/>
          <button type="submit" className="btn btn-primary">Create</button>
          <button type="button" className="btn btn-primary" onClick={() => this.props.history.push('/group')}>Cancel</button>
        </form>
        {error}
      </div>
    );
  }
}

function mapStateToProps(state) {
  return { groups: state.groups.data };
}

export default reduxForm({
  validate,
  //a unique id for this form
  form:'NewGroup',
  validators
})(
  connect(mapStateToProps, { createGroup })(NewGroup)
);
