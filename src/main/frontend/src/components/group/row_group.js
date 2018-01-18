import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { deleteGroup } from '../../actions';

class GroupRow extends Component {

  render() {
    const { group, onDelete } = this.props;
    let deleteButton = null;
    if (group.isAdmin) {
      deleteButton = <button
        type="button" onCLick={onDelete.bind(this)}
        className="btn btn-danger pull-xs-right"
        // onClick={this.onDeleteClick.bind(this)}
      >
        Delete
      </button>;
    }
    return (
      <li className="list-group-item">
        <Link to={`/groups/${group.id}`}>{group.name}</Link>
        <span>{group.description}</span>
        <span>{group.numberOfMembers}</span>
        <span>{group.numberOfExpenses}</span>
        {deleteButton}
      </li>
    );
  }
}

export default connect(null, { deleteGroup })(GroupRow);
