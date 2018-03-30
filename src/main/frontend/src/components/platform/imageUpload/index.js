import React, { Component } from 'react';

class ImageUpload extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedFile: '',
      imagePreviewUrl: ''
    }
  }

  _fileChangedHandler = event => {
    let reader = new FileReader();
    let file = event.target.files[0];
    reader.onloadend = () => {
      this.setState({
        selectedFile: file,
        imagePreviewUrl: reader.result
      });
    };

    reader.readAsDataURL(file);
  }

  _uploadHandler = () => {
    debugger;
    console.log(this.state.selectedFile);
  }

  render() {
    const { imagePreviewUrl } = this.state;
    const { previewClassName, noPreviewClassName } = this.props;
    let imagePreview;
    if (imagePreviewUrl) {
      imagePreview = <img className={ previewClassName } src={imagePreviewUrl} />;
    } else {
      imagePreview = <div className={ noPreviewClassName }>Import image to preview</div>;
    }
    return (
      <div>
        <input type="file" onChange={this._fileChangedHandler}/>
        { imagePreview }
      </div>
    );
  }
}

export default ImageUpload;
