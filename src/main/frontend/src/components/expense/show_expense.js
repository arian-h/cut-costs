import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm, formValueSelector } from 'redux-form';
import _ from 'lodash';
import DataTable, { TEXT_CELL } from '../platform/data_table';
import Modal from '../platform/modal';
import { fetchExpense, updateExpense } from '../../actions';
import { validateName, validateDescription, validateAmount } from '../../helpers/expense_utils';
import { renderField, validate } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';
import NewSharer from '../expense/new_sharer';

const validators = [{
    field: 'title',
    validator: validateName
  },
  {
    field: 'description',
    validator: validateDescription
  },
  {
    field: 'amount',
    validator: validateAmount
  }
];

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
      error => this.setState({loading:false, error: error})
    );
  }

  _updateExpense = () => {
    const { updateExpense, valid, title, amount, description, expense } = this.props;
    let r = expense.isOwner;
    debugger;
    if (valid && expense.isOwner) {
      updateExpense({
        title,
        amount,
        description
      }, this.props.expenseId,
        () => {
            //TODO errorCallback
        }
      );
    }
  }

  _addSharer = () => {
    this.setState({ showNewSharerModal: true });
  }

  _onRemoveSharer = sharerId => {
    this.props.removeSharer(sharerId, this._removeSharerErrorCallback);
  }

  _sharerRemoveActionEnabled = sharerId => {
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
      return <div>Loading expense ....</div>;
    }
    if (state.error) {
      return <div>{state.error}</div>;
    }
    let expense = props.expense;
    let sharers = expense.sharers;

    let sharersTableConfig = [
      {
        name: 'name',
        label: 'Name',
        type: TEXT_CELL
      }
    ];
    let sharersTableAction = [{
      isEnabled: this._sharerRemoveActionEnabled.bind(this),
      action: this._onRemoveSharer.bind(this),
      label: 'Remove'
    }];
    return (
      <div className="show-expense">
        {
          state.showNewSharerModal ?
            <Modal
              content={NewSharer}
              className="new-sharer-modal"
              onClose={this._closeNewSharerModal}
              expenseId={this.expenseId}
            />
            : <noscript/>
        }
        <form>
          <Field
            name="title"
            fieldType="input"
            type="text"
            component={renderField}
            label="Title"
            onBlur={this._updateExpense}
          />
          <Field
            name="description"
            fieldType="textarea"
            component={renderField}
            label="Description"
            rows="2"
            onBlur={this._updateExpense}
          />
          <Field
            name="amount"
            fieldType="input"
            component={renderField}
            label="Amount"
            onBlur={this._updateExpense}
          />
        </form>
        <p>Owner Name: {expense.owner.name}</p>
        <DataTable className="sharer-table" data={_.values(sharers)} configs={sharersTableConfig} actions={sharersTableAction}/>
        <button onClick={this._addSharer}>Add Sharer</button>
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
  validate,
  //a unique id for this form
  validators,
  enableReinitialize: true,
  form:'ShowExpense'
})(ShowExpense));
