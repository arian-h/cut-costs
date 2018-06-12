import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm, formValueSelector } from 'redux-form';
import { Link } from 'react-router-dom';
import _ from 'lodash';
import { Form, Button, Icon, Modal } from 'semantic-ui-react'

import DataTable from '../platform/data_table';
import { updateGroup, fetchGroup, deleteExpense, inviteUser } from '../../actions';
import { validateName, validateDescription } from '../../helpers/group_utils';
import { renderInputField, renderTextAreaField } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';
import NewExpense from '../expense/new_expense';
import NewInvitation from '../invitation/new_invitation';
import Spinner from '../platform/spinner';
import MemberList from './list_member';

class ShowGroup extends Component {
  constructor(props) {
    super(props);
    this.groupId = this.props.match.params.id;
    this.state = {
      loading: true,
      error: null,
      showNewExpenseModal: false
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

  render() {
    let {props, state} = this;

    if (state.loading) {
      return <Spinner text="Loading group" />;
    }
    if (state.error) {
      return <div>{state.error}</div>;
    }

    let group = props.group;
    let expenses = group.expenses;

    let columns = [
      () => <span>Expense</span>,
      () => <span>Amount</span>,
      () => {}
    ];
    let rowConfig = [
      expense => <Link to={ '/expense/' + expense.id }>{ expense.title } </Link>,
      expense => <span>{ expense.amount }</span>,
      expense => {
        if (this._expenseDeleteActionEnabled(expense.id)) {
          return <Button onClick={this._onExpenseDelete.bind(this, expense.id)}>Delete</Button>;
        }
      }
    ];

    return (
      <div>
        <Modal open={ state.showNewExpenseModal } onClose={ this._closeExpenseListModal } closeIcon>
            <NewExpense groupId={this.groupId} isAdmin={group.isAdmin} onClose={ this._closeExpenseListModal } />
        </Modal>
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
        {/*TODO use label with admin's profile pic instead of a <p> */}
        <Button icon='money' content='Add Expense' labelPosition='right' floated='right' onClick={this._addExpense} />
        <MemberList groupId={ this.groupId } isAdmin={ group.isAdmin } />
        {
          expenses.length > 0 ?
            <DataTable data={_.values(expenses)} columns={ columns } rowConfig={ rowConfig }/>
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
