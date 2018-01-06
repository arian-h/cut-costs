import Group from './group';
import Expense from './expense';
import HomePage from './home_page';

const nav = {
  'group': {
    'path': '/group',
    'component': Group
  },
  'expense': {
    'path': '/expense',
    'component': Expense
  },
  'home': {
    'path': '/',
    'component': HomePage
  }
};

export default nav;
