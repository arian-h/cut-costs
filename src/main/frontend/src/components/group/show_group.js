import React, { Component } from 'react';
      <Provider store={store}>
import { connect } from 'react-redux';
import { Field, reduxForm, formValueSelector } from 'redux-form';
import _ from 'lodash';
import DataTable, { TEXT_CELL } from '../platform/data_table';
import Modal from '../platform/modal/modal';
import { updateGroup, fetchGroup, deleteExpense, inviteUser } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderInputField, validate, renderTextAreaField } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';
import MemberList from './list_member';
import NewExpense from '../expense/new_expense';
import NewInvitation from '../invitation/new_invitation';

const validators = [{
    field: 'name',
    validator: validateName
  },
  {
    field: 'description',
    validator: validateDescription
  }
];

class ShowGroup extends Component {
  constructor(props) {
    super(props);
    this.groupId = this.props.match.params.id;
    this.state = {
      loading: true,
      error: null,
      showMemberListModal: false,
      showNewExpenseModal: false,
      showNewInvitationModal: false
    };
  }

  componentDidMount() {
    this.props.fetchGroup(
      () => this.setState({loading: false}),
      error => this.setState({loading:false, error: error})
    );
  }

  _updateGroup = () => {
    const { updateGroup, valid, name, description, group } = this.props;
    if (valid && group.isAdmin) {
      updateGroup({
        name,
        description
      }, () => {
          //TODO errorCallback
        }
      );
    }
  }

  _showMembers = () => {
    this.setState({showMemberListModal: true});
  }

  _closeMemberListModal = () => { //add th
    this.setState({showMemberListModal: false});
  }

  _addExpense = () => {
    this.setState({showNewExpenseModal: true});
  }

  _closeExpenseListModal = () => {
    this.setState({showNewExpenseModal: false});
  }

  _onExpenseDelete = expenseId => {
    this.props.deleteExpense(expenseId, this._deleteExpenseErrorCallback);
  }

  _expenseDeleteActionEnabled = userId => {
    return (getUserId() === userId.toString() || this.props.group.isAdmin);
  }

  _deleteExpenseErrorCallback = () => {
    //TODO to be complete
  }

  _inviteUser = () => {
      this.setState({showNewInvitationModal: true})
  }

  _closeNewInvitationModal = () => {
    this.setState({showNewInvitationModal: false})
  }

  render() {
    let {props, state} = this;

    if (state.loading) {
      return <div>Loading group ....</div>;
    }
    if (state.error) {
      return <div>{state.error}</div>;
    }

    let group = props.group;
    let expenses = group.expenses;

    let expenseConfigs = [
      {
        name: 'title',
        label: 'Title',
        type: TEXT_CELL
      },
      {
        name: 'amount',
        label: 'Amount',
        type: TEXT_CELL
      }
    ];
    let expenseActions = [{
      isEnabled: this._expenseDeleteActionEnabled.bind(this),
      action: this._onExpenseDelete.bind(this),
      label: 'Delete'
    }];

    return (
      <div className="show-group">
        {
          state.showMemberListModal ?
            <Modal
              content={MemberList}
              className="member-list-modal"
              onClose={this._closeMemberListModal}
              groupId={this.groupId}
              isAdmin={group.isAdmin}
            />
            : <noscript/>
        }
        {
          state.showNewExpenseModal ?
            <Modal
              content={NewExpense}
              className="new-expense-modal"
              onClose={this._closeExpenseListModal}
              isAdmin={group.isAdmin}
              groupId={this.groupId}
            />
            : <noscript/>
        }
        {
          state.showNewInvitationModal ?
            <Modal
              content={NewInvitation}
              className="new-invitation-modal"
              onClose={this._closeNewInvitationModal}
              groupId={this.groupId}
            />
            : <noscript/>
        }
        <form>
          <Field
            name="name"
            type="text"
            component={renderInputField}
            label="Name"
            onBlur={this._updateGroup}
          />
          <Field
            name="description"
            component={renderTextAreaField}
            label="Name"
            rows="2"
            onBlur={this._updateGroup}
          />
        </form>
        <p>Group Admin: {group.admin.name}</p>
        {
          expenses.length > 0 ?
          <DataTable className="member-table" data={_.values(expenses)} configs={expenseConfigs} actions={expenseActions}/>
          : <noscript/>
        }
        <button onClick={this._showMembers}>Show Members</button>
        <button onClick={this._addExpense}>Add Expense</button>
        <button onClick={this._inviteUser}>Invite User</button>
      </div>
    );
  }
}

function mapStateToProps(state, ownProps) {
  let group = state.groups ? state.groups[ownProps.match.params.id] : undefined;
  let selector = formValueSelector('ShowGroup');
  return {
    group: group,
    initialValues: {
      name: group ? group.name : '',
      description: group ? group.description : ''
    },
    name: selector(state, 'name'),
    description: selector(state, 'description')
  };
}

const mapDispatchToProps = (dispatch, ownProps) => {
  let groupId = ownProps.match.params.id;
  return {
      fetchGroup: (successCallback, errorCallback) => dispatch(fetchGroup(groupId, successCallback, errorCallback)),
      updateGroup: (values, errorCallback) => dispatch(updateGroup(values, groupId, errorCallback)),
      deleteExpense: (expenseId, errorCallback) => dispatch(deleteExpense(expenseId, groupId, errorCallback))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(reduxForm({
  validate,
  enableReinitialize: true,
  //a unique id for this form
  form:'ShowGroup'
})(ShowGroup));
