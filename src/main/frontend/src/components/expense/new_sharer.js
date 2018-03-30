import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';

import { addSharer } from '../../actions';
import { getUserId } from '../../helpers/user_utils';

class NewSharer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      inputValue: ''
    }
  }

  _onAddSharer() {
    this.props.addSharer(this.state.inputValue, () => this.props.onClose(), error => this.setState({error: error}));
  }

  _updateInputValue(evt) {
    this.setState({inputValue: evt.target.value});
  }

  render() {
    return (
      <div>
          <input type="text" value={this.state.userId} onChange={evt => this._updateInputValue(evt)} />
          <button type="submit" className="btn btn-primary" onClick={() => this._onAddSharer()}>Add Sharer</button>
          <button type="button" className="btn btn-primary" onClick={() => this.props.onClose()}>Cancel</button>
          { this.state.error ? <span>{this.state.error}</span> : <noscript/> }
      </div>
    );
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
      addSharer: (sharerId, successCallback, errorCallback) => dispatch(addSharer(sharerId, ownProps.expenseId, successCallback, errorCallback))
  };
};

export default connect(null, mapDispatchToProps)(NewSharer);
