import { LOGIN_USER, LOGOUT_USER} from '../actions';

const INITIAL_STATE = {};
export default function(state=INITIAL_STATE, action) {
  switch(action.type) {
    case LOGOUT_USER:
      localStorage.removeItem('jwt_token');
      return state;
    default:
      return state;
  }
}
