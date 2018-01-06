import React, { Component } from 'react';
import { connect } from 'react-redux';

export default class Group extends Component {
  componentDidMount() {
    this.props.fetchGroups();
  }

  static getTitle() {
    return 'Group';
  }

  render() {
    return (
      <div>Group</div>
    );
  }
}

export default connect(null, {fetchGroups})(Group);
