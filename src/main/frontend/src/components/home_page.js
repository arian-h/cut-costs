import React, { Component } from 'react';
import { connect } from 'react-redux';
import NavBar from './nav_bar';

export default class HomePage extends Component {
  render() {
    debugger;
    return (
      <div>
        <NavBar/>
        <h1>Welcome to the homepage</h1>
      </div>
    );
  }
}
