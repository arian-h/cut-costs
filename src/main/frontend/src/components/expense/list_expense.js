import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { getUserId } from '../../helpers/user_utils';
import { fetchExpenses, deleteExpense } from '../../actions';
import DataTable from '../platform/data_table';
import Spinner from '../platform/spinner';

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
      return <Spinner text="Loading expenses" />;
    }
    if (state.error) {
      return <div>{this.props.error}</div>;
    }

    let columns = [
        () => <span>Expense</span>,
        () => <span>Posted by</span>,
        () => <span>Group</span>,
        () => <span>Amount</span>,
        () => {}
    ];

    let rowConfig = [
      expense => <Link to={ '/expense/' + expense.id }>{ expense.title }</Link>,
      expense => <Link to='http://react.semantic-ui.com/assets/images/avatar/small/stevie.jpg'>{ expense.ownerName }</Link>,
      expense => <Link to={'/group/' + expense.groupId}>{ expense.groupName }</Link>,
      expense => <span>{ expense.amount }</span>,
      expense => {
          if (this._deleteActionEnabled(expense.id)) {
            return <Button onClick={this._onDelete.bind(this, expense.id)}>Delete</Button>;
          }
      }
    ];

    const { expenses } = props;

    return ( _.isEmpty(expenses) ? <div>No expense posted yet !</div> :
      <DataTable data={ _.values(expenses) } columns={ columns } rowConfig={ rowConfig }/>
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
