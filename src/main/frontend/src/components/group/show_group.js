import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm, formValueSelector } from 'redux-form';
import _ from 'lodash';
import DataTable from '../platform/data_table';
// import Modal from '../platform/modal/modal';
import { updateGroup, fetchGroup, deleteExpense, inviteUser } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderInputField, renderTextAreaField } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';
import MemberList from './list_member';
import NewExpense from '../expense/new_expense';
import NewInvitation from '../invitation/new_invitation';
import { Form, Button, Icon, Modal } from 'semantic-ui-react'

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

  _closeMemberListModal = () => {
    this.setState({showMemberListModal: false});
  }

  _addExpense = () => {
    this.setState({showNewExpenseModal: true});
  }

  _closeExpenseListModal = () => {
    this.setState({showNewExpenseModal: false});
  }

  _onExpenseDelete = expenseId => {
    debugger;
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
        label: 'Title'
      },
      {
        name: 'amount',
        label: 'Amount'
      }
    ];
    let expenseActions = [{
      isEnabled: this._expenseDeleteActionEnabled.bind(this),
      action: this._onExpenseDelete,
      label: 'Delete'
    }];

    return (
      <div className="show-group">
        <Modal open={ state.showMemberListModal } onClose={ this._closeMemberListModal } closeIcon>
          <MemberList groupId={ this.groupId } isAdmin={ group.isAdmin } />
        </Modal>
        <Modal open={ state.showNewExpenseModal } onClose={ this._closeExpenseListModal } closeIcon>
          {/* <Modal.Content> */}
            <NewExpense groupId={this.groupId} isAdmin={group.isAdmin} />
          {/* </Modal.Content> */}
        </Modal>
        {/*
        {
          state.showNewInvitationModal ?
            <Modal
              content={NewInvitation}
              className="new-invitation-modal"
              onClose={this._closeNewInvitationModal}
              groupId={this.groupId}
            />
            : <noscript/>
        } */}
        <Form error>
          <Field
            name="name"
            type="text"
            size="massive"
            transparent= { true }
            component={renderInputField}
            validate={ validateName }
            onBlur={this._updateGroup}
          />
          <Field
            name="description"
            component={renderTextAreaField}
            transparent={ true }
            rows='1'
            validate={ validateDescription }
            onBlur={this._updateGroup}
          />
        </Form>
        <p>Group Admin: {group.admin.name}</p>
        {/*TODO use label with admin's profile instead of a <p> */}
        <Button icon='users' content='Members' labelPosition='right' floated='right' onClick={this._showMembers}/>
        <Button icon='money' content='Add Expense' labelPosition='right' floated='right' onClick={this._addExpense}/>
        <Button icon='add user' content='Invite User' labelPosition='right' floated='right' onClick={this._inviteUser}/>
        {
          expenses.length > 0 ?
          <DataTable className="member-table" data={_.values(expenses)} configs={expenseConfigs} actions={expenseActions}/>
          : <noscript/>
        }
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
  enableReinitialize: true,
  //a unique id for this form
  form:'ShowGroup'
})(ShowGroup));
