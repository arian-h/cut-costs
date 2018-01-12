import GroupList from '../components/group/list_group';
import ExpenseList from '../components/expense/list_expense';
import HomePage from '../components/home_page';

const RouteList = {
  'group': {
    'path': '/group',
    'component': GroupList
  },
  'expense': {
    'path': '/expense',
    'component': ExpenseList
  },
  'home': {
    'path': '/',
    'component': HomePage
  }
};

export default RouteList;
