import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
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
    this.groupId = this.props.history.location.pathname.split("/")[2];
  }

  _fetchGroupErrorCallback = response => {
  }

  componentWillMount() {
    debugger;
    this.props.fetchGroup(this.groupId, this._fetchGroupErrorCallback);
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
    debugger;
    //what is the best practice here ?
    if (_.isEmpty(this.props.groups)) {
      return <div>Loading group ....</div>;
    }
    let group = this.props.groups[this.groupId];
    if (group.status) {
      return <div>{group.data.message}</div>;
    }
    return (
      <div className="show-group">
        <form>
          {_.map(FIELDS, renderField.bind(this))}
        </form>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return { groups: state.groups };
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
