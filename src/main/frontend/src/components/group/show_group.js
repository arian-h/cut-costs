import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import _ from 'lodash';
//TODO FIX THE FORM!!!
import DataTable, { TEXT_CELL } from '../platform/data_table';
import Modal from '../platform/modal';
import { updateGroup, fetchGroup, deleteExpense, inviteUser } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderField, validate } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';
import MemberList from './list_member';
import NewExpense from '../expense/new_expense';
import NewInvitation from './new_invitation';

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
  //TODO: work on the list_member component
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
    this.props.deleteExpense(expenseId, this.groupId, this._deleteExpenseErrorCallback);
  }

  _expenseDeleteActionEnabled = userId => {
    return (getUserId() === userId.toString() || this.props.groups[this.groupId].isAdmin);
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

    let group = props.groups[this.groupId];
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
            fieldType="input"
            type="text"
            component={renderField}
            label="Name"
            validate={validateName}
            value="helloooo"
          />
        </form>
        <p>Name : {group.name}</p>
        <p>Description : {group.description}</p>
        <p>Admin Name: {group.admin.name}</p>
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

function mapStateToProps(state) {
  return {
    groups: state.groups
  };
}

const mapDispatchToProps = (dispatch) => {
    return {
        fetchGroup: (groupId, successCallback, errorCallback) => dispatch(fetchGroup(groupId, successCallback, errorCallback)),
        updateGroup: (groupId, successCallback, errorCallback) => dispatch(updateGroup(groupId, successCallback, errorCallback)),
        deleteExpense: (expenseId, groupId, errorCallback) => dispatch(deleteExpense(expenseId, groupId, errorCallback))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(reduxForm({
  // validate,
  //a unique id for this form
  form:'ShowGroup'
})(ShowGroup));
