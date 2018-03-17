import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import _ from 'lodash';
//TODO FIX THE FORM!!!
import { updateUser, fetchUser } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderField, validate } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';

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
class ShowUser extends Component {
  constructor(props) {
    super(props);
    this.userId = this.props.match.params.id;
    if (this.userId === undefined) {
      this.userId = getUserId();
    }
    this.state = {
      loading: true,
      error: null
    };
  }

  componentDidMount() {
    this.props.fetchUser(
      this.userId,
      () => this.setState({loading: false}),
      error => this.setState({loading:false, error: error})
    );
  }

  _updateUser = () => {
    const { updateUser } = this.props;
    updateUser({
      ...this.props.values
    }, () => {
      // TODO error case in updating the user info, what should be done ?
    });
    //TODO update the form
  }

  render() {
    let {props, state} = this;

    if (state.loading) {
      return <div>Loading user ....</div>;
    }
    if (state.error) {
      return <div>{state.error}</div>;
    }

    let user = props.users[this.userId];

    return (
      <div className="show-user-info">
        {/* <form>
          <Field
            name="name"
            fieldType="input"
            type="text"
            component={renderField}
            label="Name"
            validate={validateName}
            value="helloooo"
          />
        </form> */}
        <p>Name : {user.name}</p>
        <p>Description : {user.description}</p>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    users: state.users
  };
}

const mapDispatchToProps = (dispatch) => {
    return {
        updateUser: errorCallback => dispatch(updateUser(errorCallback)),
        fetchUser: (userId, successCallback, errorCallback) => dispatch(fetchUser(userId, successCallback, errorCallback))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(reduxForm({
  // validate,
  //a unique id for this form
  form:'ShowUser'
})(ShowUser));
