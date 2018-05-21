import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm, formValueSelector } from 'redux-form';
import _ from 'lodash';
import { Form } from 'semantic-ui-react';

import { updateUser, fetchUser } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils'; //TODO fix this
import { renderInputField, validate, renderTextAreaField } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';
import Spinner from '../platform/spinner';


const validators = [{
    field: 'name',
    validator: validateName
  },
  {
    field: 'description',
    validator: validateDescription
  }
];

class ShowUser extends Component {
  constructor(props) {
    super(props);
    this.userId = this.props.match.params.id || getUserId();
    this.state = {
      loading: true,
      error: null
    };
  }

  componentDidMount() {
    this.props.fetchUser(
      () => this.setState({loading: false}),
      error => this.setState({loading:false, error: error})
    );
  }

  _updateUser = () => {
    const { updateUser, valid, name, description, user } = this.props;
    if (valid && getUserId() === (user.id + '') ) { //TODO update id's on the backend to be String
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

    let user = props.user;

    return (
      <div className="show-user-info">
        <Form>
          <Field
            name="name"
            type="text"
            component={ renderInputField }
            label="Name"
            onBlur={ this._updateUser }
          />
          <Field
            name="description"
            component={ renderTextAreaField }
            label="Description"
            rows="1"
            onBlur={ this._updateUser }
          />
        </Form>
      </div>
    );
  }
}

function mapStateToProps(state, ownProps) {
  let userId = ownProps.match.params.id || getUserId();
  let user = state.users ? state.users[userId] : undefined;
  let selector = formValueSelector('ShowUser');
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
  let userId = ownProps.match.params.id || getUserId();
  return {
      updateUser: errorCallback => dispatch(updateUser(errorCallback)),
      fetchUser: (successCallback, errorCallback) => dispatch(fetchUser(userId, successCallback, errorCallback))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(reduxForm({
  validate,
  validators,
  enableReinitialize: true,
  form:'ShowUser'
})(ShowUser));
