import React, { Component } from 'react';
import { Search } from 'semantic-ui-react';
import _ from 'lodash';

class SearchBar extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      results: [],
      term: ''
    };
  }

  componentWillMount() {
    this._resetComponent()
  }

  _resetComponent = () => this.setState({ loading: false, results: [], term: '' })

  _handleResultSelect = (e, { result }) => console.log(result)

  _handleSearchChange = (e, { value: term }) => {
    if (term.length < 1) return this._resetComponent();
    this.props.searchHandler(term, results => this.setState({ results, loading: false}));
    this.setState({ loading: true, term });
  }

  _searchCallback = results => {
    this.setState({ results: results });
  }

  render() {
    const { loading, results, term } = this.state;
    if (results.length > 0) {
      results.forEach(result => {
        result._id = result.id; // id is overriden by the Search component
      })
    }
    return (<Search
      loading={ loading }
      onResultSelect={ this.handleResultSelect }
      onSearchChange={ _.debounce(this._handleSearchChange, 500, { leading: true }) }
      results={ results }
      value={ term }
      {...this.props}
    />);
  }
}

export default SearchBar;
