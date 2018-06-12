import _ from 'lodash';
import {
  FETCH_GROUPS,
  CREATE_GROUP,
  DELETE_GROUP,
  FETCH_GROUP,
  CREATE_EXPENSE,
  DELETE_EXPENSE_FROM_GROUP,
  UPDATE_GROUP,
  SUBSCRIBE_GROUP,
  UNSUBSCRIBE_GROUP,
  REMOVE_MEMBER
} from '../actions/creators';

export default function(state = {}, action) {
  switch (action.type) {
    case FETCH_GROUPS:
      return _.mapKeys(action.groups, 'id');
    case FETCH_GROUP:
      return { ...state,
        [action.group.id]: action.group
      };
    case CREATE_GROUP:
      return { ...state,
        [action.group.id]: action.group
      };
    case DELETE_GROUP:
      return _.omit(state, action.group);
    case UPDATE_GROUP:
      return { ...state,
        [action.group.id]: action.group
      };
    case CREATE_EXPENSE:
      return { ...state,
        [action.groupId]: { ...state[action.groupId],
          expenses: [...state[action.groupId].expenses.slice(0), action.expense]
        }
      };
    case DELETE_EXPENSE_FROM_GROUP:
      return { ...state,
        [action.groupId]: { ...state[action.groupId],
          expenses: _.filter(state[action.groupId].expenses, expense => expense.id !== action.expenseId)
        }
      };
    case SUBSCRIBE_GROUP:
      return { ...state,
        [action.groupId]: {...state[action.groupId], subscribed: true}
      };
    case UNSUBSCRIBE_GROUP:
      return { ...state,
        [action.groupId]: {...state[action.groupId], subscribed: false}
      };
    case REMOVE_MEMBER:
      debugger;
    default:
      return state;
  }
}
