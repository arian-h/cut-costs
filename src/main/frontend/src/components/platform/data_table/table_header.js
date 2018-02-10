import React, {Component} from 'react';
import _ from 'lodash';

class TableHeader extends Component {
  render() {
    const { data, actions } = this.props;
    return (
      <thead>
        <tr>
          {_.map(data, cellData => <td>{cellData.label}</td>)}
          { actions && actions.length && <td></td> }
        </tr>
      </thead>
    );
  }
}

export default TableHeader;
