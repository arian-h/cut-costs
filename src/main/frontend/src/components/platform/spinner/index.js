import React, { Component } from 'react';
import { Segment, Dimmer, Image, Loader } from 'semantic-ui-react'

class Spinner extends Component {
  render() {
    const { text } = this.props;
    return (
      <Segment>
        <Dimmer active>
          <Loader> { text } </Loader>
        </Dimmer>
        <Image src='/assets/images/wireframe/short-paragraph.png' />
      </Segment>
    );
  }
}

export default Spinner;
