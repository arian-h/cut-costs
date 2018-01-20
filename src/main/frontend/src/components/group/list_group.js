import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import _ from 'lodash';

import { fetchGroups, deleteGroup } from '../../actions';
import Modal from '../modal/modal';
import GroupRow from './row_group';

class GroupList extends Component {
  componentWillMount() {
    this.props.fetchGroups();
  }

  _onDelete = (groupId) => {
    this.props.deleteGroup(groupId);
  }

  //returns modal if one exists in the props
  _getModal() {
    const {props} = this;
    let modal = <noscript/>;
    if (props.modal) {
      const { modal: {content, className} } = props;
      modal = <Modal content={content} className={className} {...props}/>;
    }
    return modal;
  }

  render() {
    const { groups} = this.props;
    //TODO how to distinguish between the first time and no group ?
    if (_.isEmpty(groups)) {
      return <div>Something has gone wrong while fetching groups</div>;
    }
    return (
      <div>
        <div className="text-xs-right">
          <Link className="btn btn-primary" to="/group/new">
            New Group
          </Link>
        </div>
        { this._getModal() }
        <ul className="list-group">
          {
            _.map(this.props.groups, group =>
              <GroupRow group={group} onDelete={this._onDelete} key={group.id}/>
            )
          }
        </ul>
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
        fetchGroups: () => dispatch(fetchGroups()),
        deleteGroup: (id) => dispatch(deleteGroup(id))
    };
};
/* This is where action creator is connected to the component and
the redux store through mapStateToProps */
export default connect(mapStateToProps, mapDispatchToProps)(GroupList);
