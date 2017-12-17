import { LOGIN_USER, LOGOUT_USER} from '../actions';
import _ from 'lodash';

export default function(state={}, action) {
  switch(action.type) {
    // case LOGIN_USER:
    //   const {status} = action.payload;
    //   if (status === 200) {
    //     const {headers:{authorization}} = action.payload;
    //     localStorage.setItem('jwt_token', authorization);
    //   }
    //   return state;
    case LOGOUT_USER:
      localStorage.removeItem('jwt_token');
      return state;
    default:
      return state;
  }
}
