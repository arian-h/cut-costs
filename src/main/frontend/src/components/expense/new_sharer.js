import React, { Component } from 'react';

export default class NewSharer extends Component {
  //TODO: work on this component
  render() {
    return (
      <div>
        New sharer
        <button type="button" className="btn btn-primary" onClick={this.props.onClose}>Cancel</button>
      </div>
    );
  }
}
