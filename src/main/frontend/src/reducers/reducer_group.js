import _ from 'lodash';
import { FETCH_GROUPS, CREATE_GROUP, DELETE_GROUP, FETCH_GROUP, CREATE_EXPENSE, DELETE_EXPENSE_FROM_GROUP } from '../actions/creators';

export default function(state={}, action) {
  switch(action.type) {
    case FETCH_GROUPS:
      return _.mapKeys(action.payload, 'id');
    case FETCH_GROUP:
      return {...state, [action.payload.id]: action.payload};
    case CREATE_GROUP:
      debugger;
      return { ...state, [action.payload.id]: action.payload};
    case DELETE_GROUP:
      return _.omit(state, action.payload.data);
    case CREATE_EXPENSE:
      return { ...state, [action.groupId]: { ...state[action.groupId], expenses: [...state[action.groupId].expenses.slice(0), action.payload]}};
    case DELETE_EXPENSE_FROM_GROUP:
      return { ...state, [action.groupId]: { ...state[action.groupId], expenses: _.filter(state[action.groupId].expenses, expense =>  expense.id !== action.expenseId)}};
    default:
      return state;
  }
}
