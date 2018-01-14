import GroupList from '../components/group/list_group';
import ExpenseList from '../components/expense/list_expense';
import HomePage from '../components/home_page';
import RegisterForm from '../components/auth/form_register';

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
  },
  'register': {
    'path': '/register',
    'component': RegisterForm
  }
};

export default RouteList;
