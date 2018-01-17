import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';

import { fetchGroups } from '../../actions';
import Modal from '../modal/modal';

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

  //returns modal if one exists in the props
  _getModal() {
    let modal = <noscript/>;
    let modalContent, modalClassName;
    if (this.props.modal) {
      modalContent = this.props.modal.content;
      modalClassName = this.props.modal.className;
      modal = <Modal content={modalContent} className={modalClassName}/>;
    }
    return modal;
  }

  render() {
    const { groups} = this.props;
    let modal = this._getModal();
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
        { modal }
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
        <p>SalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalamSalam</p>
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
