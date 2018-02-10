import React, {Component} from 'react';
import _ from 'lodash';
import { Link } from 'react-router-dom';

import { TEXT_CELL, IMAGE_CELL } from './index';

class TableRow extends Component {
  render() {
    const { data, actions, id } = this.props;
    return (
      <tr>
        {
          _.map(data, cellData => {
            let element;
            if (cellData.type === TEXT_CELL) {
              element = <span>{cellData.value}</span>;
            } else if (cellData.type === IMAGE_CELL) {
              element = <img src={cellData.value} />;
            }
            if (cellData.href) {
              element = <Link to={cellData.href}>{element}</Link>;
            }
            return <td>{element}</td>;
          })
        }
        {
          actions && actions.length &&
            _.map(actions, (action) => {
                if (action.isEnabled && action.isEnabled(id)) {
                  return <td><button onClick={() => action.action(id)}>{action.label}</button></td>;
                } //TODO, make it a dropdown action menu
            })
        }
      </tr>
    );
  }
}

export default TableRow;
