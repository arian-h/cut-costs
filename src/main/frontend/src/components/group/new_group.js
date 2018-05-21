import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import { Modal, Form, Button } from 'semantic-ui-react';

import { createGroup } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderInputField, renderTextAreaField } from '../../helpers/form_utils';

class NewGroup extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null
    }
  }

  _onSubmit(values) {
    this.props.createGroup(values, () => this.props.onClose(), error => this.setState({ error: error }));
  }

  render() {
    const { handleSubmit } = this.props;

    return ([
      <Modal.Header>
        Create Group
      </Modal.Header>,
      <Modal.Content>
        <Form onSubmit={ handleSubmit(this._onSubmit.bind(this)) } id="create-group-form" error>
          <Field name="name" validate={ validateName } label="Name" type="text" component={ renderInputField }/>
          <Field name="description" validate={ validateDescription } label="Description" type="text" component={ renderTextAreaField }/>
          { this.state.error ? <span>{this.state.error}</span> : <noscript/> }
        </Form>
      </Modal.Content>,
      <Modal.Actions>
        <Button type="submit" content="Create Group" form="create-group-form"/>
      </Modal.Actions>
    ]);
  }
}

export default connect(null, { createGroup })(reduxForm({
  //a unique id for this form
  form:'NewGroup'
})(NewGroup));
