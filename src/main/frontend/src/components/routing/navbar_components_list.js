import GroupList from '../group/list_group';
import ExpenseList from '../expense/list_expense';
import HomePage from '../home_page';

const nav = {
  'group': {
    'path': '/group',
    'component': GroupList,
    'navbarTitle': 'Groups'
  },
  'expense': {
    'path': '/expense',
    'component': ExpenseList,
    'navbarTitle': 'Expenses'
  },
  'home': {
    'path': '/',
    'component': HomePage,
    'navbarTitle': 'Home'
  }
};

export default nav;
