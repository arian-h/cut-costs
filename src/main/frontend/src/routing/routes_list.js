import GroupList from '../components/group/list_group';
import ExpenseList from '../components/expense/list_expense';
import Home from '../components/home';
import RegisterForm from '../components/auth/form_register';
import LoginForm from '../components/auth/form_login';
import NewGroup from '../components/group/new_group';
import ShowGroup from '../components/group/show_group';

export const RouteList = {
  'group': {
    'navPath': '/group', // used for component on the nav bar
    'component': GroupList
  },
  'group/new': {
    'component': GroupList,
    'props': {
      'modal': {
        'content': NewGroup,
        'className': 'new-group-modal'
      }
    }
  },
  'group\/[0-9]+': {
    'component': ShowGroup
  },
  'expense': {
    'navPath': '/expense',
    'component': ExpenseList
  },
  'home': {
    'navPath': '/',
    'component': Home
  },
  'register': {
    'component': RegisterForm
  },
  'login': {
    'component': LoginForm
  }
};

export function getNavPath(_pathname) {
  let route = _.find(_.keys(RouteList), key => _pathname.match(new RegExp(`^${key}$`, "ig")));
  if (!RouteList[route]) {
    route = 'home';
  }
  return route;
}
