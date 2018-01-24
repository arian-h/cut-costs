import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import _ from 'lodash';

import { updateGroup } from '../../actions';
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
        this.updateGroupInfo();
      }
    }
  }
}

// onSubmit={handleSubmit(this._onUpdate.bind(this))}
class ShowGroup extends Component {
  constructor(props) {
    super(props);
    this.groupId = this.props.history.location.pathname.split("/")[2];
  }

  componentWillMount() {
    //TODO fetch the data for this group and update the state of this form
  }

  updateGroupInfo = () => {
    const { updateGroup } = this.props;
    updateGroup({
      id: this.groupId,
      ...this.props.values
    }, () => {
      debugger;
    });
    //update the form
  }

  render() {
    return (
      <div className="show-group">
        <form>
          {_.map(FIELDS, renderField.bind(this))}
        </form>
      </div>
    );
  }
}

export default reduxForm({
  validate,
  //a unique id for this form
  form:'ShowGroup',
  fields: _.keys(FIELDS),
  fields_def: FIELDS
})(
  connect(null, { updateGroup })(ShowGroup)
);
