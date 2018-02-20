import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

class MemberList extends Component {

  constructor(props) {
    super(props);
    this.groupId = this.props.match.params.id;
    this.state = {
      loading: true,
      error: null
    };
  }

  componentDidMount() {
    this.props.fetchMembers(
      this.groupId,
      () => this.setState({loading: false}),
      error => this.setState({loading:false, error: error})
    );
  }

  _deleteActionEnabled = groupId => {
    // TODO if current user is admin of group with groupId
  }

  _onDelete = (groupId, memberId) => {
    //TODO do we need groupId here or we can get it from this.groupId
    this.props.removeMember(groupId, memberId);
  }

  render() {
    const { props, state } = this;

    if (state.loading) {
      return <div>Loading members...</div>;
    }
    if (state.error) {
      return <div>{this.props.error}</div>;
    }

    let configs = [
      {
        name: 'name',
        label: 'Name',
        type: TEXT_CELL
      }
    ];
    let actions = [{
      isEnabled: this._removeMemberEnabled,
      action: this._onRemove,
      label: 'Remove'
    }];

    let members = { props };

    return (
      {
        _.isEmpty(groups) ? <div>No member listed !</div>
        : <DataTable className="member-table" data={_.values(members)} configs={configs} actions={actions}/>
      }
    );
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
          fetchMembers: (groupId, successfulCallback, unsuccessfulCallback) => dispatch(fetchMembers(groupId, successfulCallback, unsuccessfulCallback)),
          removeMember: (groupId, memberId) => dispatch(removeMember(groupId, memberId))
      };
  }
  /* This is where action creator is connected to the component and
  the redux store through mapStateToProps */
  export default connect(mapStateToProps, mapDispatchToProps)(MemberList);
}
