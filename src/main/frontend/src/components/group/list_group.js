import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';
import { Grid, Button, Icon, Modal } from 'semantic-ui-react'

import { fetchGroups, deleteGroup, subscribeToGroup } from '../../actions';
import DataTable from '../platform/data_table';
import NewGroup from './new_group';
import Spinner from '../platform/spinner';

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

  _deleteActionEnabled = id => {
    return this.props.groups[id].isAdmin;
  };

  _onDelete = groupId => {
    this.props.deleteGroup(groupId);
  };

  _closeNewGroupModal = () => {
    this.setState({ showNewGroupModal: false });
  };

  _openNewGroupModal = () => {
    this.setState({ showNewGroupModal: true });
  }

  _onSubscribe = groupId => {
    this.props.subscribeToGroup(groupId, !this._subscribed(groupId));
  }

  _subscribed = groupId => {
    return this.props.groups[groupId].subscribed;
  }

  render() {
    const { props, state } = this;

    if (state.loading) {
      return <Spinner text="Loading groups" />;
    }
    if (state.error) {
      return <div>{this.props.error}</div>;
    }
    let columns = [
        () => <span>Group</span>,
        () => <span>Number of expenses</span>,
        () => <span>Number of members</span>,
        () => <span>Total expenses</span>,
        () => {},
        () => {}
    ];
    let rowConfig = [
      group => <Link to={ '/group/' + group.id }>{ group.name }</Link>,
      group => group.numberOfExpenses,
      group => group.numberOfMembers,
      group => group.totalAmount,
      group => {
        if (this._deleteActionEnabled(group.id)) {
          return <Button onClick={this._onDelete.bind(this, group.id)}>Delete</Button>;
        }
      },
      group => {
        if (this._subscribed(group.id)) {
          return <Button onClick={this._onSubscribe.bind(this, group.id)} icon basic className='subscribe-button'><Icon name='star' color='yellow'/></Button>;
        } else {
          return <Button onClick={this._onSubscribe.bind(this, group.id)} icon basic className='subscribe-button'><Icon name='star' color='grey'/></Button>;
        }
      }
    ];

    const { groups } = props;

    return ([
      <Modal open={ state.showNewGroupModal } onClose={ this._closeNewGroupModal } closeIcon>
        <NewGroup onClose={ this._closeNewGroupModal }/>
      </Modal>,
      <div>
        <Grid.Row>
          <Grid.Column>
            <Button icon primary labelPosition='left' size='small' onClick={ this._openNewGroupModal }>
                <Icon name='group'></Icon>New Group
            </Button>
          </Grid.Column>
        </Grid.Row>
        <Grid.Row>
          {_.isEmpty(groups) ? <div>No group listed !</div>
            : <DataTable paginated pageSize={ 4 } data={ _.values(groups) } rowConfig={ rowConfig } columns={ columns }/>}
        </Grid.Row>
      </div>
    ]);
  }
}
/*this function works directly with the <Provider> placed inside
index.js (i.e. around the app)
*/
function mapStateToProps(state) {
  return { groups: state.groups };
}

const mapDispatchToProps = dispatch => {
    return {
        fetchGroups: (successCallback, errorCallback) => dispatch(fetchGroups(successCallback, errorCallback)),
        deleteGroup: id => dispatch(deleteGroup(id)),
        subscribeToGroup: (groupId, subscribe) => dispatch(subscribeToGroup(groupId, subscribe))
    };
};
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */
export default connect(mapStateToProps, mapDispatchToProps)(GroupList);
