import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';

import { addSharer } from '../../actions';
import { getUserId } from '../../helpers/user_utils';
import { Modal, Button, Form } from 'semantic-ui-react';
import { renderInputField } from '../../helpers/form_utils';

class NewSharer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null
    }
  }

  _onSubmit(values) {
    this.props.addSharer(values.sharerId, () => this.props.onClose(), error => this.setState({ error: error }));
  }

  render() {
    const { handleSubmit } = this.props;

    return ([
      <Modal.Header>
        Add Sharer
      </Modal.Header>,
      <Modal.Content>
        <Form onSubmit={ handleSubmit(this._onSubmit.bind(this)) } id="add-sharer-form" closeIcon>
          <Field name="sharerId" label="Sharer id" type="text" component={ renderInputField }/>
          { this.state.error ? <span>{ this.state.error }</span> : <noscript/> }
        </Form>
      </Modal.Content>,
      <Modal.Actions>
        <Button type="submit" form="add-sharer-form" content="Add Sharer" />
      </Modal.Actions>
    ]);
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
      addSharer: (sharerId, successCallback, errorCallback) => dispatch(addSharer(sharerId, ownProps.expenseId, successCallback, errorCallback))
  };
};

export default connect(null, mapDispatchToProps)(reduxForm({
  //a unique id for this form
  form: 'NewSharer'
})(NewSharer));
