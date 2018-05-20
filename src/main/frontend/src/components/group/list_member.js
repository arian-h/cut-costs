import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import DataTable from '../platform/data_table';
import { fetchMembers, removeMember } from '../../actions';
import { getUserId } from '../../helpers/user_utils';
import { Modal } from 'semantic-ui-react';

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
      return <div>Loading members...</div>;
    }

    if (state.error) {
      return <div>{props.error}</div>;
    }

    let configs = [
      {
        name: 'name',
        label: 'Name'
      }
    ];
    let actions = [{
      isEnabled: this._removeActionEnabled.bind(this),
      action: this._onRemove.bind(this),
      label: 'Remove'
    }];

    let members = props.members[props.groupId];
    return ([
        <Modal.Header>
          Members list
        </Modal.Header>,
        <Modal.Content>
          {
            _.isEmpty(members) ? <div>No member listed !</div> :
              <div>
                  <DataTable className="member-table" data={ _.values(members) } configs={ configs } actions={ actions }/>
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
