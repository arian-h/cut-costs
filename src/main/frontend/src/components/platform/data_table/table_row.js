import React, {Component} from 'react';
import _ from 'lodash';

import { TEXT_CELL, IMAGE_CELL } from './index';

class TableRow extends Component {
  render() {
    const { data } = this.props;
    return (
      <tr>
        {
          _.map(data, (cellData) => {
            let element;
            if (cellData.type === TEXT_CELL) {
              element = <span>{cellData.value}</span>;
            } else if (cellData.type === IMAGE_CELL) {
              element = <img src={cellData.value} />;
            }
            if (cellData.href) {
              element = <a href={cellData.href}>{element}</a>;
            }
            return <td>{element}</td>;
          })
        }
      </tr>
    );
  }
}

export default TableRow;
