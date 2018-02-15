import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { fetchGroups, deleteGroup } from '../../actions';
import Modal from '../modal/modal';
import DataTable, { TEXT_CELL } from '../platform/data_table';

class GroupList extends Component {
  componentDidMount() {
    this.props.fetchGroups();
  }

  //returns modal if one exists in the props
  _getNewGroupModal = () => {
    const { props } = this;
    if (!props.modal) {
      return null;
    }
    const { modal: {content, className} } = props;
    return <Modal content={content} className={className} {...props}/>;
  }

  _deleteActionEnabled = (id) => {
    return this.props.groups[id].data.isAdmin;
  };

  _onDelete = (groupId) => {
    this.props.deleteGroup(groupId);
  }

  render() {
    debugger;
    if (this.props.isLoading) {
      return <div>Loading groups...</div>;
    }
    if (this.props.errorFetching) {
      return <div>{this.props.errorFetching}</div>;
    }
    const { groups } = this.props;
    let configs = [
      {
        name: 'name',
        label: 'Group',
        type: TEXT_CELL, // this can be either text, image
        href: group => '/group/' + group.id
      },
      {
        name: 'description',
        label: 'Description',
        type: TEXT_CELL
      },
      {
        name: 'numberOfExpenses',
        label: 'Expenses',
        type: TEXT_CELL
      },
      {
        name: 'numberOfMembers',
        label: 'Members',
        type: TEXT_CELL
      }
    ];
    let actions = [{
      isEnabled: this._deleteActionEnabled,
      action: this._onDelete,
      label: 'Delete'
    }];
    return (
      <div>
        <div className="text-xs-right">
          <Link className="btn btn-primary" to="/group/new">
            New Group
          </Link>
        </div>
        { this._getNewGroupModal() }
        {
          _.isEmpty(groups) ? <div>No group listed !</div>
          : <DataTable className="group-table" data={_.map(groups, group => group.data)} configs={configs} actions={actions}/>
        }
      </div>
    );
  }
}
/*this function works directly with the <Provider> placed inside
index.js (i.e. around the app)
*/
function mapStateToProps(state) {
  return { groups: state.groups.data, isLoading: state.groups.isLoading, errorFetching: state.groups.errorFetching };
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
