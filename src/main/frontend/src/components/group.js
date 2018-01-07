import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGroups } from '../actions';
import { Link } from 'react-router-dom';

class Group extends Component {
  componentDidMount() {
    this.props.fetchGroups();
  }

  static getTitle() {
    return 'Group';
  }

  renderGroups() {
    return _.map(this.props.groups, group => {
      return (
        <li className="list-group-item" key={group.id}>
          <Link to={`/groups/${group.id}`}>{group.name}</Link>
        </li>
      )
    });
  }

  render() {

    return (
      <div>
        <div className="text-xs-right">
          <Link className="btn btn-primary" to="/groups/new">
            Add a Group
          </Link>
        </div>
        <h3>Groups</h3>
        <ul className="list-group">
          {this.renderGroups()}
        </ul>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return { groups: state.groups };
}

export default connect(mapStateToProps, {fetchGroups})(Group);
