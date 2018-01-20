import React, {Component} from 'react';
import _ from 'lodash';

class TableHeader extends Component {
  render() {
    const { data } = this.props;
    return (
      <thead>
        <tr>
          {_.map(data, cellData => <td>{cellData.label}</td>)}
        </tr>
      </thead>
    );
  }
}

export default TableHeader;
