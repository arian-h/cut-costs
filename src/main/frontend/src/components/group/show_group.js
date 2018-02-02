import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import _ from 'lodash';

import { updateGroup, fetchGroup } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderField, validate } from '../../helpers/form_utils';

const FIELDS = {
  name: {
    validate: validateName,
    fieldType: 'input',
    props: {
      className: 'name-field',
      type: 'text'
    }
  },
  description: {
    validate: validateDescription,
    fieldType: 'textarea',
    props: {
      className: 'desc-field',
      rows: 2,
      placeholder: 'Add Description To Your Group',
      onBlur: function() {
        this._updateGroupNameDesc();
      }
    }
  }
}

// onSubmit={handleSubmit(this._onUpdate.bind(this))}
class ShowGroup extends Component {
  constructor(props) {
    super(props);
    this.groupId = this.props.match.params.id;
  }

  componentWillMount() {
    this.props.fetchGroup(this.groupId);
  }

  _updateGroupNameDesc = () => { // only updates group name or description on the event of onBlur
    const { updateGroup } = this.props;
    debugger;
    updateGroup({
      id: this.groupId,
      ...this.props.values
    }, () => {

    });
    //update the form
  }

  renderMyField = ({input, label, meta: {touched, error}, ...rest}) => {
    const { fieldType } = rest;
    const textareaType = <textarea {...input} {...rest} className={`form-control ${touched && error ? 'has-danger' : ''}`}/>;
    const inputType = <input {...input} {...rest} className={`form-control ${touched && error ? 'has-danger' : ''}`}/>;
    return (
      <div>
        <label>{label}</label>
        <div>
          {fieldType === 'textarea' ? textareaType : inputType}
          {touched && error && <span>{error}</span>}
        </div>
      </div>
    );
  }

  render() {
    //what is the best practice here ?
    let group = this.props.groups[this.groupId];
    if (this.props.isLading) {
      return <div>Loading group ....</div>;
    }
    if (this.props.errorFetching) {
      return <div>Cannot fetch group with id {this.groupId} </div>;
    }
    console.log(group);
    console.log(this.props);
    debugger;
    return (
      <div className="show-group">
        {/* <form>
          <Field
            name="name"
            fieldType="input"
            type="password"
            component={this.renderMyField}
            label="Name"
            validate={validateName}
          />
        </form> */}
        <span>{group.name}</span>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return { groups: state.groups, isLoading: state.isLoading, errorFetching: state.errorFetching };
}

const mapDispatchToProps = (dispatch) => {
    return {
        fetchGroup: (id, callback) => dispatch(fetchGroup(id, callback)),
        updateGroup: (id) => dispatch(updateGroup(id))
    };
};

export default reduxForm({
  validate,
  //a unique id for this form
  form:'ShowGroup',
  fields: _.keys(FIELDS),
  fields_def: FIELDS
})(
  connect(mapStateToProps, mapDispatchToProps)(ShowGroup)
);
