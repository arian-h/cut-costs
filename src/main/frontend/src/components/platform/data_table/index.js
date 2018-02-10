import React, {Component} from 'react';
import TableHeader from './table_header';
import TableRow from './table_row';
export const TEXT_CELL = "text";
export const IMAGE_CELL = "image";

class DataTable extends Component {
  render() {
    const { className, configs, data: entities, actions} = this.props;
    return (
      <table className={className}>
        <TableHeader data={configs} actions={actions}/>
        <tbody>
          {_.map(entities, entity =>
            <TableRow id={entity.id} actions={actions}
              data={_.map(configs, config =>
                !_.isUndefined(entity[config.name]) &&
                  {
                    type: config.type, // TEXT_CELL or IMAGE_CELL
                    value: entity[config.name],
                    href: config.href && config.href(entity)
                  }
                )
              }
            />
          )}
        </tbody>
      </table>
    );
  }
}

export default DataTable;
