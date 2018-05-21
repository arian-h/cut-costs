import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import Spinner from '../platform/spinner';
import { fetchExpenses } from '../../actions';
import DataTable from '../platform/data_table';
import { fetchInvitations, rejectInvitation, acceptInvitation } from '../../actions';


class InvitationList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      error: null
    };
  }

  componentDidMount() {
    this.props.fetchInvitations(
      () => this.setState({loading: false}),
      error => this.setState({loading:false, error: error})
    );
  }

  _rejectInvitationErrorCallback = () => {
    //TODO: fix this
    debugger;
  }

  _acceptInvitationErrorCallback = () => {
    //TODO: fix this
    debugger;
  }

  _onReject = invitationId => {
    this.props.rejectInvitation(invitationId, this._rejectInvitationErrorCallback);
  };

  _onAccept = invitationId => {
    this.props.acceptInvitation(invitationId, this._acceptInvitationErrorCallback);
  }

  render() {
      const { props, state } = this;

      if (state.loading) {
        return <Spinner text="Loading invitations" />;
      }
      if (state.error) {
        return <div>{ this.props.error }</div>;
      }
      const { invitations } = props;
      var invitationsListData = _.map(invitations, function(invitation){
        return {
          id: invitation.id,
          inviterId: invitation.inviter.id,
          inviter: invitation.inviter.name,
          groupId: invitation.group.id,
          group: invitation.group.name
        };
      });
      let configs = [
        {
          name: 'inviter',
          label: 'Inviter',
          href: invitation => '/user/' + invitation.inviterId
        },
        {
          name: 'group',
          label: 'Group',
          href: invitation => '/group/' + invitation.groupId
        }
      ];
      let actions = [{
        isEnabled: () => true,
        action: this._onReject,
        label: 'Reject'
      }
      ,
      {
        isEnabled: () => true,
        action: this._onAccept,
        label: 'Accept'
      }
    ];

    return ( _.isEmpty(invitations) ? <div>Not invited to any group yet!</div>
      : <DataTable className="invitation-table" data={_.values(invitationsListData)} configs={configs} actions={actions}/>
    );
  }
}
/*this function works directly with the <Provider> placed inside
index.js (i.e. around the app)
*/
function mapStateToProps(state) {
  return { invitations: state.invitations };
}

const mapDispatchToProps = dispatch => {
    return {
        fetchInvitations: (successCallback, errorCallback) => dispatch(fetchInvitations(successCallback, errorCallback)),
        acceptInvitation: (invitationId, errorCallback) => dispatch(acceptInvitation(invitationId, errorCallback)),
        rejectInvitation: (invitationId, errorCallback) => dispatch(rejectInvitation(invitationId, errorCallback))
    };
};
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */
export default connect(mapStateToProps, mapDispatchToProps)(InvitationList);
