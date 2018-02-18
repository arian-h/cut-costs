import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { fetchGroups, deleteGroup } from '../../actions';
import Modal from '../modal/modal';
import DataTable, { TEXT_CELL } from '../platform/data_table';

class GroupList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      error: null
    };
  }

  componentDidMount() {
    this.props.fetchGroups(
      () => this.setState({loading: false}),
      error => this.setState({loading:false, error: error})
    );
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

  _deleteActionEnabled = id => {
    return this.props.groups[id].isAdmin;
  };

  _onDelete = groupId => {
    this.props.deleteGroup(groupId);
  }

  render() {
    if (this.state.loading) {
      return <div>Loading groups...</div>;
    }
    if (this.state.error) {
      return <div>{this.props.error}</div>;
    }

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

    const { groups } = this.props;
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
          : <DataTable className="group-table" data={_.values(groups)} configs={configs} actions={actions}/>
        }
      </div>
    );
  }
}
/*this function works directly with the <Provider> placed inside
index.js (i.e. around the app)
*/
function mapStateToProps(state) {
  debugger;
  return { groups: state.groups };
}

const mapDispatchToProps = (dispatch) => {
    return {
        fetchGroups: (successfulCallback, unsuccessfulCallback) => dispatch(fetchGroups(successfulCallback, unsuccessfulCallback)),
        deleteGroup: id => dispatch(deleteGroup(id))
    };
};
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */
export default connect(mapStateToProps, mapDispatchToProps)(GroupList);
