import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { fetchGroups, deleteGroup } from '../../actions';
import Modal from '../platform/modal/modal';
import DataTable, { TEXT_CELL } from '../platform/data_table';
import { Grid, Segment, Button } from 'semantic-ui-react'

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
        type: TEXT_CELL,
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

    const { groups } = props;

    return (
      <div>
        {
          props.modal ?
            <Modal content={props.modal.content} className={props.modal.className} {...props}/>
            : <noscript/>
        }
        <Grid>
          <Grid.Row>
            <Segment floated='right'>
              <Button primary as={Link} to="/group/new">
                {/* <Link className="btn btn-primary" to="/group/new"> */}
                  New Group
                {/* </Link> */}
              </Button>
            </Segment>
          </Grid.Row>
            <Segment>
              <Grid.Row>
                {
                  _.isEmpty(groups) ? <div>No group listed !</div>
                  : <DataTable className="group-table" data={_.values(groups)} configs={configs} actions={actions}/>
                }
              </Grid.Row>
            </Segment>
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
