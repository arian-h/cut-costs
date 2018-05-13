import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { fetchGroups, deleteGroup } from '../../actions';
import Modal from '../platform/modal/modal';
import DataTable, { TEXT_CELL } from '../platform/data_table';
import { Grid, Button, Icon } from 'semantic-ui-react'

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
  }

  render() {
    const { props, state } = this;

    if (state.loading) {
      return <div>Loading groups...</div>;
    }
    if (state.error) {
      return <div>{this.props.error}</div>;
    }

    let configs = [
      {
        name: 'name',
        label: 'Group',
        href: group => '/group/' + group.id
      },
      {
        name: 'description',
        label: 'Description'
      },
      {
        name: 'numberOfExpenses',
        label: 'Expenses'
      },
      {
        name: 'numberOfMembers',
        label: 'Members'
      }
    ];
    let actions = [{
      isEnabled: this._deleteActionEnabled,
      action: this._onDelete,
      label: 'Delete',
      icon: 'delete'
    }];

    const { groups } = props;

    return (
      <div>
        {props.modal ?
          <Modal content={props.modal.content} className={props.modal.className} {...props}/>
          : <noscript/>}
        <Grid>
          <Grid.Row>
            <Grid.Column>
              <Button icon primary labelPosition='left' size='small' as={Link} to="/group/new">
                  <Icon name='group'></Icon>New Group
              </Button>
            </Grid.Column>
          </Grid.Row>
          <Grid.Row>
            {_.isEmpty(groups) ? <div>No group listed !</div>
              : <DataTable className="group-table" data={_.values(groups)} configs={configs} actions={actions}/>}
          </Grid.Row>
        </Grid>
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
        fetchGroups: (successCallback, errorCallback) => dispatch(fetchGroups(successCallback, errorCallback)),
        deleteGroup: id => dispatch(deleteGroup(id))
    };
};
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */
export default connect(mapStateToProps, mapDispatchToProps)(GroupList);
