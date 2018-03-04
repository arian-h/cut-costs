import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import _ from 'lodash';
//TODO FIX THE FORM!!!
import DataTable, { TEXT_CELL } from '../platform/data_table';
import Modal from '../platform/modal';
import { fetchExpense} from '../../actions';
// import { validateName, validateDescription } from '../../helpers/group_utils';
// import { renderField, validate } from '../../helpers/form_utils';
import { getUserId } from '../../helpers/user_utils';
import NewSharer from '../expense/new_sharer';

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
    this.props.fetchExpense(this.expenseId,
      () => {
        this.setState({ loading: false });
      },
      error => this.setState({loading:false, error: error})
    );
  }

  // _updateGroupNameDesc = () => { // only updates group name or description on the event of onBlur
  //   const { updateGroup } = this.props;
  //   updateGroup({
  //     id: this.groupId,
  //     ...this.props.values
  //   }, () => {
  //
  //   });
  //   //update the form
  // }

  _addSharer = () => {
    this.setState({ showNewSharerModal: true });
  }

  _onRemoveSharer = sharerId => {
    this.props.removeSharer(sharerId, this.expenseId, this._removeSharerErrorCallback);
  }

  _sharerRemoveActionEnabled = sharerId => {
    return this.props.expenses[this.expenseId].isOwner;
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

    let expense = props.expenses[this.expenseId];
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
        {/* <form>
          <Field
            name="name"
            fieldType="input"
            type="text"
            component={renderField}
            label="Name"
            validate={validateName}
            value="helloooo"
          />
        </form> */}
        <p>Name : {expense.title}</p>
        <p>Description : {expense.description}</p>
        <p>Admin Name: {expense.owner.name}</p>
        <DataTable className="sharer-table" data={_.values(sharers)} configs={sharersTableConfig} actions={sharersTableAction}/>
        <button onClick={this._addSharer}>Add Sharer</button>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    expenses: state.expenses
  };
}

const mapDispatchToProps = dispatch => {
    return {
        fetchExpense: (expenseId, successCallback, errorCallback) => dispatch(fetchExpense(expenseId, successCallback, errorCallback)),
        removeSharer: (sharerId, expenseId, errorCallback) => dispatch(removeSharer(sharerId, expenseId, errorCallback))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(reduxForm({
  // validate,
  //a unique id for this form
  form:'ShowExpense'
})(ShowExpense));
