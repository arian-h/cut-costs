import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { getUserId } from '../../helpers/user_utils';
import { fetchExpenses, deleteExpense } from '../../actions';
import Modal from '../platform/modal';
import DataTable, { TEXT_CELL } from '../platform/data_table';

class ExpenseList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      error: null
    };
  }

  componentDidMount() {
    this.props.fetchExpenses(
      () => this.setState({loading: false}),
      error => this.setState({loading:false, error: error})
    );
  }

  _deleteActionEnabled = id => {
    return this.props.expenses[id].ownerId.toString() === getUserId();
  };

  _onDelete = id => {
    this.props.deleteExpense(id, this._deleteExpenseErrorCallback);
  }

  _deleteExpenseErrorCallback = id => {
    // TODO: complete this part
    debugger;
  }

  render() {
    const { props, state } = this;

    if (state.loading) {
      return <div>Loading expenses...</div>;
    }
    if (state.error) {
      return <div>{this.props.error}</div>;
    }

    let configs = [
      {
        name: 'title',
        label: 'Title',
        type: TEXT_CELL,
        href: expense => '/expense/' + expense.id
      },
      {
        name: 'ownerName',
        label: 'Posted By',
        type: TEXT_CELL //TODO: add href later
      },
      {
        name: 'groupName',
        label: 'Group',
        type: TEXT_CELL,
        href: expense => '/group/' + expense.groupId
      },
      {
        name: 'amount',
        label: 'Amount',
        type: TEXT_CELL
      }
    ];
    let actions = [{
      isEnabled: this._deleteActionEnabled,
      action: this._onDelete,
      label: 'Delete'
    }];

    const { expenses } = props;

    return (
      <div>
        {
          props.modal ?
            <Modal content={props.modal.content} className={props.modal.className} {...props}/>
            : <noscript/>
        }
        {
          _.isEmpty(expenses) ? <div>No expense posted yet !</div>
          : <DataTable className="expense-table" data={_.values(expenses)} configs={configs} actions={actions}/>
        }
      </div>
    );
  }
}
/*this function works directly with the <Provider> placed inside
index.js (i.e. around the app)
*/
function mapStateToProps(state) {
  return { expenses: state.expenses };
}

const mapDispatchToProps = (dispatch) => {
    return {
        fetchExpenses: (fetchSuccessCallback, fetchErrorCallback) => dispatch(fetchExpenses(fetchSuccessCallback, fetchErrorCallback)),
        deleteExpense: (expenseId, deleteErrorCallback) => dispatch(deleteExpense(expenseId, undefined, deleteErrorCallback))
    };
};
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */
export default connect(mapStateToProps, mapDispatchToProps)(ExpenseList);
