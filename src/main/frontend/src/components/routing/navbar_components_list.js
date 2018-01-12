import GroupsList from '../group/list_groups';
import Expense from '../expense';
import HomePage from '../home_page';

const nav = {
  'group': {
    'path': '/group',
    'component': GroupsList,
    'navbarTitle': 'Groups'
  },
  'expense': {
    'path': '/expense',
    'component': Expense,
    'navbarTitle': 'Expenses'
  },
  'home': {
    'path': '/',
    'component': HomePage,
    'navbarTitle': 'Home'
  }
};

export default nav;
