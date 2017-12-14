import { LOGIN_USER } from '../actions';
import _ from 'lodash';

export default function(state={}, action) {
  switch(action.type) {
    case LOGIN_USER:
      const {status, headers: {authorization}} = action.payload;
      console.log(status);
      console.log(authorization);
      return state;
    default:
      return state;
  }
}
