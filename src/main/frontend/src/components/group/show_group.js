import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import _ from 'lodash';
//TODO fix the data table
//TODO fix the form
import { updateGroup, fetchGroup } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderField, validate } from '../../helpers/form_utils';

// const FIELDS = {
//   name: {
//     validate: validateName,
//     fieldType: 'input',
//     props: {
//       className: 'name-field',
//       type: 'text'
//     }
//   },
//   description: {
//     validate: validateDescription,
//     fieldType: 'textarea',
//     props: {
//       className: 'desc-field',
//       rows: 2,
//       placeholder: 'Add Description To Your Group',
//       onBlur: function() {
//         this._updateGroupNameDesc();
//       }
//     }
//   }
// }

// onSubmit={handleSubmit(this._onUpdate.bind(this))}


class ShowGroup extends Component {
  constructor(props) {
    super(props);
    this.groupId = this.props.match.params.id;
  }

  componentDidMount() {
    this.props.fetchGroup(this.groupId);
  }

  _updateGroupNameDesc = () => { // only updates group name or description on the event of onBlur
    const { updateGroup } = this.props;
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
    let group = this.props.groups[this.groupId];
    if (group.isLoading === undefined || group.mode === 'snippet_group') {
      return <div>Loading group ....</div>;
    }
    if (group.errorFetching) {
      return <div>{this.props.errorFetching}</div>;
    }
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
        <p>Name : {group.data.name}</p>
        <p>Description : {group.data.description}</p>
        <p>Admin Name: {group.data.admin.name}</p>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return { groups: state.groups.data };
}

const mapDispatchToProps = (dispatch) => {
    return {
        fetchGroup: (id) => dispatch(fetchGroup(id)),
        updateGroup: (id) => dispatch(updateGroup(id))
    };
};

export default reduxForm({
  // validate,
  //a unique id for this form
  form:'ShowGroup'
})(
  connect(mapStateToProps, mapDispatchToProps)(ShowGroup)
);
