import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm, formValueSelector } from 'redux-form';
import _ from 'lodash';
import DataTable from '../platform/data_table';
import { Modal, Form, Button } from 'semantic-ui-react';
import { fetchExpense, updateExpense, removeSharer } from '../../actions';
import { validateName, validateDescription, validateAmount } from '../../helpers/expense_utils';
import { renderInputField, validate, renderTextAreaField } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';
import NewSharer from '../expense/new_sharer';
import Spinner from '../platform/spinner';

class ShowExpense extends Component {
  constructor(props) {
    super(props);
    this.expenseId = this.props.match.params.id;
    this.state = {
      loading: true,
      error: null,
      showNewSharerModal: false
    };
  }

  componentDidMount() {
    this.props.fetchExpense(
      () => {
        this.setState({ loading: false });
      },
      error => this.setState({ loading:false, error: error })
    );
  }

  _updateExpense = () => {
    const { updateExpense, valid, title, amount, description, expense } = this.props;
    if (valid && expense.isOwner) {
      updateExpense({
        title,
        amount,
        description
      }, () => {
        //TODO errorCallback
      });
    }
  }

  _addSharer = () => {
    this.setState({ showNewSharerModal: true });
  }

  _onRemoveSharer = sharerId => {
    this.props.removeSharer(sharerId, this._removeSharerErrorCallback);
  }

  _sharerRemoveActionEnabled = () => {
    return this.props.expense.isOwner;
  }

  _removeSharerErrorCallback = () => {
    //TODO to be complete
  }

  _closeNewSharerModal = () => {
    this.setState({ showNewSharerModal: false });
  }

  render() {
    let {props, state} = this;

    if (state.loading) {
      return <Spinner text="Loading expense details"/>;
    }
    if (state.error) {
      return <div>{state.error}</div>;
    }
    let expense = props.expense;
    let sharers = expense.sharers;

    let sharersTableConfig = [
      {
        name: 'name',
        label: 'Name'
      }
    ];
    let sharersTableAction = [{
      isEnabled: this._sharerRemoveActionEnabled.bind(this),
      action: this._onRemoveSharer.bind(this),
      label: 'Remove'
    }];

    return (
      <div className="show-expense">
        <Modal open={ state.showNewSharerModal } onClose={ this._closeNewSharerModal } closeIcon>
          <NewSharer expenseId={ this.expenseId } onClose={ this._addSharer } />
        </Modal>
        <Form>
          <Field
            name="title"
            validate={ validateName }
            type="text"
            component={ renderInputField }
            label="Title"
            onBlur={ this._updateExpense }
          />
          <Field
            name="description"
            validate={ validateDescription }
            component={ renderTextAreaField }
            label="Description"
            rows="2"
            onBlur={ this._updateExpense }
          />
          <Field
            name="amount"
            validate={ validateAmount }
            type="text"
            component={ renderInputField }
            label="Amount"
            onBlur={ this._updateExpense }
          />
        </Form>
        <p>Owner name: { expense.owner.name }</p>
        <DataTable className="sharer-table" data={ _.values(sharers) } configs={ sharersTableConfig } actions={ sharersTableAction }/>
        <Button onClick={ this._addSharer } content="Add Sharer" />
      </div>
    );
  }
}

function mapStateToProps(state, ownProps) {
  let expense = state.expenses ? state.expenses[ownProps.match.params.id] : undefined;
  let selector = formValueSelector('ShowExpense');
  return {
    expense: expense,
    initialValues: {
      title: expense ? expense.title : '',
      description: expense ? expense.description : '',
      amount: expense ? expense.amount : 0
    },
    title: selector(state, 'title'),
    description: selector(state, 'description'),
    amount: selector(state, 'amount')
  };
}

const mapDispatchToProps = (dispatch, ownProps) => {
  let expenseId = ownProps.match.params.id;
  return {
      fetchExpense: (successCallback, errorCallback) => dispatch(fetchExpense(expenseId, successCallback, errorCallback)),
      removeSharer: (sharerId, errorCallback) => dispatch(removeSharer(sharerId, expenseId, errorCallback)),
      updateExpense: (values, errorCallback) => dispatch(updateExpense(values, expenseId, errorCallback))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(reduxForm({
  enableReinitialize: true,
  form:'ShowExpense'
})(ShowExpense));
