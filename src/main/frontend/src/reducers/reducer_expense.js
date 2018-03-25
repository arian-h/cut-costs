import _ from 'lodash';
import { FETCH_EXPENSES, DELETE_EXPENSE, FETCH_EXPENSE, REMOVE_SHARER, UPDATE_EXPENSE } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case FETCH_EXPENSES:
      return _.mapKeys(action.expenses, 'id');
    case DELETE_EXPENSE:
      return _.omit(state, action.expenseId);
    case FETCH_EXPENSE:
      return {...state, [action.expense.id]: action.expense};
    case REMOVE_SHARER:
      return state;
    case UPDATE_EXPENSE:
      return {...state, [action.expense.id]: action.expense};
    default:
      return state;
  }
}
