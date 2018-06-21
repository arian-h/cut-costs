import _ from 'lodash';
import { FETCH_EXPENSES, DELETE_EXPENSE, FETCH_EXPENSE, REMOVE_SHARER, UPDATE_EXPENSE, ADD_SHARER } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case FETCH_EXPENSES:
      return _.mapKeys(action.expenses, 'id');
    case DELETE_EXPENSE:
      return _.omit(state, action.expenseId);
    case FETCH_EXPENSE:
      return {...state, [action.expense.id]: action.expense};
    case REMOVE_SHARER:
      return  {...state, [action.expenseId]: {...state[action.expenseId], sharers: _.filter(state[action.expenseId].sharers, sharer =>  sharer.id !== action.sharerId)}};
    case UPDATE_EXPENSE:
      return {...state, [action.expense.id]: action.expense};
    case ADD_SHARER:
      return {...state, [action.expenseId]: {...state[action.expenseId], sharers: [...state[action.expenseId].sharers.slice(0), action.sharer]}};
    default:
      return state;
  }
}
