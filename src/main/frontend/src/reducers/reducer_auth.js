import { LOGIN_USER, LOGOUT_USER} from '../actions';

export default function(state={}, action) {
  switch(action.type) {
    case LOGOUT_USER:
      localStorage.removeItem('jwt_token');
      return state;
    default:
      return state;
  }
}
