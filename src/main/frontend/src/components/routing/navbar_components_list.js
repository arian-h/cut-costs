import GroupsList from '../group/list_groups';
import Expense from '../expense';
import HomePage from '../home_page';

const nav = {
  'group': {
    'path': '/group',
    'component': GroupsList,
    'title': 'Groups'
  },
  'expense': {
    'path': '/expense',
    'component': Expense,
    'title': 'Expenses'
  },
  'home': {
    'path': '/',
    'component': HomePage,
    'title': 'Home'
  }
};

export default nav;
