import React, {Component} from 'react';
import _ from 'lodash';

import { TEXT_CELL, IMAGE_CELL } from './index';

class TableRow extends Component {
  render() {
    const { data, actions } = this.props;
    return (
      <tr>
        {
          _.map(data.row, (cellData) => {
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
        {
          _.map(actions, (action) => {
              if (action.isEnabled && action.isEnabled(data.id)) {
                return <td><button onClick={() => action.action(data.id)}>{action.title}</button></td>;
              } //TODO, make it a dropdown action menu
          })
        }
      </tr>
    );
  }
}

export default TableRow;
