import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGroups } from '../../actions';
import { Link } from 'react-router-dom';

class GroupList extends Component {
  componentWillMount() {
    this.props.fetchGroups();
  }

  renderGroupsList() {
    return _.map(this.props.groups, group => {
      let deleteButton = null;
      let editButton = null;
      if (group.isAdmin) {
        deleteButton = <button
          className="btn btn-danger pull-xs-right"
          // onClick={this.onDeleteClick.bind(this)}
        >
          Delete
        </button>;
        editButton = <button
          className="btn btn-danger pull-xs-right"
          // onClick={this.onDeleteClick.bind(this)}
        >
          Edit
        </button>;
      }
      return (
        <li className="list-group-item" key={group.id}>
          <Link to={`/groups/${group.id}`}>{group.name}</Link>
          <span>{group.description}</span>
          <span>{group.numberOfMembers}</span>
          <span>{group.numberOfExpenses}</span>
          {editButton}
          {deleteButton}
        </li>
      )
    });
  }

  render() {
    const { groups } = this.props;
    //TODO how to distinguish between the first time and no group ?
    // if (!groups) {
    //   return <div>Loading...</div>;
    // }
    return (
      <div>
        <div className="text-xs-right">
          <Link className="btn btn-primary" to="/group/new">
            New Group
          </Link>
        </div>
        <h3>Groups</h3>
        <ul className="list-group">
          {this.renderGroupsList()}
        </ul>
      </div>
    );
  }
}
/*this function works directly with the <Provider> placed inside
index.js (i.e. around the app)
*/
function mapStateToProps(state) {
  return { groups: state.groups };
}
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */
export default connect(mapStateToProps, {fetchGroups})(GroupList);
