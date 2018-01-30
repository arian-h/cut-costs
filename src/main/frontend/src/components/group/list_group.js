import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { fetchGroups, deleteGroup } from '../../actions';
import Modal from '../modal/modal';
import GroupRow from './row_group';
import DataTable, { TEXT_CELL } from '../platform/data_table';

class GroupList extends Component {
  componentWillMount() {
    this.props.fetchGroups();
  }

  //returns modal if one exists in the props
  _getModal = () => {
    const {props} = this;
    let modal = <noscript/>;
    if (props.modal) {
      const { modal: {content, className} } = props;
      modal = <Modal content={content} className={className} {...props}/>;
    }
    return modal;
  }

  _deleteActionEnabled = (id) => {
    return this.props.groups[id].isAdmin;
  };

  _onDelete = (groupId) => {
    this.props.deleteGroup(groupId);
  }

  render() {
    const { groups } = this.props;
    //TODO how to distinguish between the first time and no group ?
    //table config
    let configs = {
      'name': {
        label: 'Group',
        type: TEXT_CELL, // this can be either text, image
        href: group => '/api/group/' + group.id
      },
      'description': {
        label: 'Description',
        type: TEXT_CELL
      },
      'numberOfExpenses': {
        label: 'Expenses',
        type: TEXT_CELL
      },
      'numberOfMembers': {
        label: 'Members',
        type: TEXT_CELL
      }
    };
    let actions = [{
      isEnabled: this._deleteActionEnabled,
      action: this._onDelete,
      title: 'Delete'
    }];
    return (
      <div>
        <div className="text-xs-right">
          <Link className="btn btn-primary" to="/group/new">
            New Group
          </Link>
        </div>
        { this._getModal() }
        {
          _.isEmpty(groups) ? <div>No group listed !</div>
          : <DataTable className="group-table" data={groups} configs={configs} actions={actions}/>
        }
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

const mapDispatchToProps = (dispatch) => {
    return {
        fetchGroups: () => dispatch(fetchGroups()),
        deleteGroup: (id) => dispatch(deleteGroup(id))
    };
};
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */
export default connect(mapStateToProps, mapDispatchToProps)(GroupList);
