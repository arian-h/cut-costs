import React, { Component } from 'react';
import { Label, Icon, Button } from 'semantic-ui-react';
import _ from 'lodash';

/* List of items that can either be short (with show more icon) or extended */
class   ItemList extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const { props, state } = this;
    const { items, limit, onRemove, showMore, removeEnabled, itemRenderer } = props;
    let labels = [];
    for (let index = 0; index < (limit ? Math.min(limit, items.length) : items.length); index++) {
      labels.push(itemRenderer(items[index]));
    }
    if (!_.isNil(showMore)) {
      labels.push(
        <Button icon onClick={ () => showMore() }>
          <Icon name='ellipsis horizontal' />
        </Button>
      );
    }
    return labels;
  }

}

export default ItemList;
