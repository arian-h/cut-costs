import React, { Component } from 'react';
import { connect } from 'react-redux';

export default class HomePage extends Component {
  static getTitle() {
    return 'Home';
  }
  render() {
    return (
      <div>
        <h1>Welcome to the homepage</h1>
      </div>
    );
  }
}
