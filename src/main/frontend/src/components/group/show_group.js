import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import _ from 'lodash';
//TODO fix the form
//TODO fix the data table
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
class ShowGroup extends Component {

  constructor(props) {
    super(props);
    this.groupId = this.props.match.params.id;
    this.state = {
      loading: true,
      error: null
    };
  }

  componentWillMount() {
    this.props.fetchGroup(this.groupId,
      () => this.setState({loading: false}),
      error => this.setState({loading:false, error: error})
    );
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

  render() {
    if (this.state.loading) {
      return <div>Loading group ....</div>;
    }
    if (this.state.error) {
      return <div>{this.state.error}</div>;
    }
    let group = this.props.groups[this.groupId];
    return (
      <div className="show-group">
        <form>
          <Field
            name="name"
            fieldType="input"
            type="text"
            component={renderField}
            label="Name"
            validate={validateName}
          />
        </form>
        <p>Name : {group.name}</p>
        <p>Description : {group.description}</p>
        <p>Admin Name: {group.admin.name}</p>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    groups: state.groups,
    initialValues: {
      name: 'salam'
    }
  };
}

const mapDispatchToProps = (dispatch) => {
    return {
        fetchGroup: (id, successfulCallback, unsuccessfulCallback) => dispatch(fetchGroup(id, successfulCallback, unsuccessfulCallback)),
        updateGroup: (id, successfulCallback, unsuccessfulCallback) => dispatch(updateGroup(id, successfulCallback, unsuccessfulCallback))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(reduxForm({
  // validate,
  //a unique id for this form
  form:'ShowGroup'
})(ShowGroup));
