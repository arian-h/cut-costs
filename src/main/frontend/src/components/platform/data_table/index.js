import React, {Component} from 'react';
import TableHeader from './table_header';
import TableRow from './table_row';
export const TEXT_CELL = "text";
export const IMAGE_CELL = "image";

class DataTable extends Component {
  render() {
    const { className, configs, data: entities } = this.props;
    let rows = [];
    _.forOwn(entities, function(entity, id) {
        let row = [];
        _.forOwn(configs, function(config, key) {
            let href;
            if (config.href) { // if there is a function to create href
              href = config.href(entity);
            }
            row.push({
              type: config.type, // TEXT_CELL or IMAGE_CELL
              value: entity[key],
              href: href
            });
        });
        rows.push(row);
    });
    return (
      <table className={className}>
        <TableHeader data={configs}/>
        <tbody>
          {_.map(rows, row => <TableRow data={row} />)}
        </tbody>
      </table>
    );
  }
}

export default DataTable;
