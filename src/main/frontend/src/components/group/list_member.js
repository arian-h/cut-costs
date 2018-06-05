import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';
import { Button } from 'semantic-ui-react';

import DataTable from '../platform/data_table';
import { fetchMembers, removeMember } from '../../actions';
import { getUserId } from '../../helpers/user_utils';
import { Modal } from 'semantic-ui-react';
import Spinner from '../platform/spinner';

class MemberList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      error: null,
      removeError: null
    };
  }

  componentDidMount() {
    this.props.fetchMembers(
      this.props.groupId,
      () => this.setState({loading: false}),
      error => this.setState({loading:false, error: error})
    );
  }

  _removeActionEnabled = memberId => {
    return (getUserId() != memberId.toString()) && this.props.isAdmin;
  }

  _onRemove = memberId => {
    this.props.removeMember(this.props.groupId, memberId, this._removeErrorCallback);
  }

  _removeErrorCallback = error => {
    this.setState({removeError: error});
  }

  render() {
    const { props, state } = this;

    if (state.loading) {
      return <Spinner text="Loading members" />;
    }

    if (state.error) {
      return <div>{props.error}</div>;
    }
    let columns = [
      () => <span>Member</span>,
      () => {}
    ];
    let rowConfig = [
      member => <span>{ member.name }</span>,
      member => {
        if (this._removeActionEnabled(member.id)) {
          return <Button onClick={this._onRemove.bind(this, member.id)}>Delete</Button>;
        }
      }
    ];

    let members = props.members[props.groupId];
    return ([
        <Modal.Header>
          Members list
        </Modal.Header>,
        <Modal.Content>
          {
            _.isEmpty(members) ? <div>No member listed !</div> :
              <div>
                  <DataTable className="member-table" data={ _.values(members) } columns={ columns } rowConfig={ rowConfig }/>
                  { state.removeError ? <span>{state.removeError}</span>:<noscript/> }
              </div>
          }
        </Modal.Content>
    ]);
  }

}

/*this function works directly with the <Provider> placed inside
index.js (i.e. around the app)
*/
function mapStateToProps(state) {
  return { members: state.members };
}

const mapDispatchToProps = (dispatch) => {
    return {
        fetchMembers: (groupId, successCallback, errorCallback) => dispatch(fetchMembers(groupId, successCallback, errorCallback)),
        removeMember: (groupId, memberId, successCallback, errorCallback) => dispatch(removeMember(groupId, memberId, successCallback, errorCallback))
    }
}
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */

export default connect(mapStateToProps, mapDispatchToProps)(MemberList);
