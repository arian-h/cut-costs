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
  }

  _acceptInvitationErrorCallback = () => {
    //TODO: fix this
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
      var invitationsListData = _.map(invitations, invitation =>
        ({
          id: invitation.id,
          inviterId: invitation.inviter.id,
          inviter: invitation.inviter.name,
          groupId: invitation.group.id,
          group: invitation.group.name
        })
      );
      let columns = [
        () => <span>Inviter</span>,
        () => <span>Group</span>
      ];
      let rowConfig = [
        invitation => <Link to={ '/user/' + invitation.inviterId }>{ invitation.name }</Link>,
        invitation => <Link to={ '/group/' + invitation.groupId }>{ invitation.group }</Link>,
        invitation => {
          return <Button onClick={this._onAccept.bind(this, invitation.id)}>Accept</Button>;
        },
        invitation => {
          return <Button onClick={this._onReject.bind(this, invitation.id)}>Reject</Button>;
        }
      ];

    return ( _.isEmpty(invitations) ? <div>Not invited to any group yet!</div>
      : <DataTable data={_.values(invitationsListData)} columns={ columns } rowConfig={ rowConfig }/>
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
