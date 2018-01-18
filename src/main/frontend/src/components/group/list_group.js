import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';

import { fetchGroups } from '../../actions';
import Modal from '../modal/modal';
import GroupRow from './row_group';

class GroupList extends Component {
  componentWillMount() {
    this.props.fetchGroups();
  }

  _onDelete() {
    this.props.deleteGroup();
  }

  //returns modal if one exists in the props
  _getModal() {
    let props = this.props;
    let modal = <noscript/>;
    let modalContent, modalClassName;
    if (props.modal) {
      modalContent = props.modal.content;
      modalClassName = props.modal.className;
      modal = <Modal content={modalContent} className={modalClassName} {...props}/>;
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
    let groupList = _.map(this.props.groups, group => {
      return <GroupRow group={group} onDelete={this._onDelete} key={group.id}/>;
    });
    return (
      <div>
        <div className="text-xs-right">
          <Link className="btn btn-primary" to="/group/new">
            New Group
          </Link>
        </div>
        { modal }
        <ul className="list-group">
          {groupList}
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
