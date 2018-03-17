export const FETCH_GROUPS = 'fetch_groups';
export const DELETE_GROUP = 'delete_group';
export const CREATE_GROUP = 'create_group';
export const FETCH_GROUP = 'fetch_group';
export const FETCH_MEMBERS = 'fetch_members';
export const REMOVE_MEMBER = 'remove_member';
export const CREATE_EXPENSE = 'create_expense';
export const DELETE_EXPENSE_FROM_GROUP = 'delete_expense_from_group';
export const FETCH_EXPENSES = 'fetch_expenses';
export const DELETE_EXPENSE = 'delete_expense';
export const FETCH_EXPENSE = 'fetch_expense';
export const REMOVE_SHARER = 'remove_sharer';
export const REJECT_INVITATION = 'reject_invitation';
export const FETCH_INVITATIONS = 'fetch_invitations';
export const ACCEPT_INVITATION = 'accept_invitation';
export const FETCH_USER = 'fetch_user';

export function groupDeleted(response) {
  return {
     type: DELETE_GROUP,
     payload: response
  }
}

export function userFetched(user) {
  return {
     type: FETCH_USER,
     user
  }
}

export function groupsFetched(groups) {
  return {
     type: FETCH_GROUPS,
     payload: groups
  }
}

export function groupCreated(group) {
  return {
    type: CREATE_GROUP,
    payload: group
  }
}

export function groupFetched(group) {
  return {
     type: FETCH_GROUP,
     payload: group
  }
}

export function membersFetched(response, groupId) {
  return {
     type: FETCH_MEMBERS,
     payload: response,
     groupId
  }
}

export function memberRemoved(response) {
  return {
     type: REMOVE_MEMBER,
     payload: response
  }
}

export function expenseCreated(response, groupId) {
  return {
     type: CREATE_EXPENSE,
     payload: response,
     groupId
  }
}

export function expenseDeletedFromGroup(expenseId, groupId) {
  return {
     type: DELETE_EXPENSE_FROM_GROUP,
     expenseId,
     groupId
  }
}

export function expenseDeleted(expenseId) {
  debugger;
  return {
     type: DELETE_EXPENSE,
     expenseId
  }
}

export function expensesFetched(expenses) {
  return {
    type: FETCH_EXPENSES,
    expenses
  }
}

export function expenseFetched(expense) {
  return {
    type: FETCH_EXPENSE,
    expense
  }
}

export function sharerRemoved(sharerId, expenseId) {
  return {
    type: REMOVE_SHARER,
    expenseId,
    sharerId
  }
}

export function invitationRejected(invitationId) {
  return {
    type: REJECT_INVITATION,
    invitationId
  }
}

export function invitationAccepted(invitationId) {
  return {
    type: ACCEPT_INVITATION,
    invitationId
  }
}

export function invitationsFetched(invitations) {
  return {
    type: FETCH_INVITATIONS,
    invitations
  }
}
