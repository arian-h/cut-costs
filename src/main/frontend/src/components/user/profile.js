import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm, formValueSelector } from 'redux-form';
import _ from 'lodash';
import { Form } from 'semantic-ui-react';

import { updateUser, fetchUser } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderInputField, renderTextAreaField } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';
import Spinner from '../platform/spinner';

class Profile extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      error: null
    };
  }

  componentDidMount() {
    this.props.fetchUser(
      () => this.setState({ loading: false }),
      error => this.setState({ loading:false, error: error })
    );
  }

  _updateUser = () => {
    const { updateUser, valid, name, description } = this.props;
    if (valid) {
      updateUser({
        name,
        description
      }, () => {
        //TODO errorCallback
      });
    }
  }

  render() {
    let { props, state } = this;

    if (state.loading) {
      return <Spinner text="Loading user info" />;
    }
    if (state.error) {
      return <div>{ state.error }</div>;
    }

    return (
      <Form>
        <Field
          name="name"
          type="text"
          component={ renderInputField }
          label="Name"
          onBlur={ this._updateUser }
          validate={ validateName }
        />
        <Field
          name="description"
          component={ renderTextAreaField }
          label="Description"
          rows="1"
          onBlur={ this._updateUser }
          validate={ validateDescription }
        />
      </Form>
    );
  }
}

function mapStateToProps(state, ownProps) {
  let user = state.users ? state.users[getUserId()] : undefined;
  let selector = formValueSelector('Profile');
  return {
    user: user,
    initialValues: {
      name: user ? user.name : '',
      description: user ? user.description : ''
    },
    name: selector(state, 'name'),
    description: selector(state, 'description')
  };
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
      updateUser: errorCallback => dispatch(updateUser(errorCallback)),
      fetchUser: (successCallback, errorCallback) => dispatch(fetchUser(getUserId(), successCallback, errorCallback))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(reduxForm({
  enableReinitialize: true,
  form:'Profile'
})(Profile));
