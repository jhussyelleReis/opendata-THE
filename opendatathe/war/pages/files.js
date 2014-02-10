// Because we want to access DOM node,
// we initialize our script at page load.
$(function () {
var file = {
        dom    : $('input[name="file"]'),
        binary : null,
      };
 
  // We use the FileReader API to access our file content
  var reader = new FileReader();

  // Because de FileReader API is asynchronous, we need
  // to store it's result when it has finish to read the file
  reader.addEventListener("load", function () {
    file.binary = reader.result;
  });

  // At page load, if a file is already selected, we read it.
  if(file.dom.files[0]) {
    reader.readAsBinaryString(file.dom.files[0]);
  }

  // However, we will read the file once the user selected it.
  file.dom.addEventListener("change", function () {
    if(reader.readyState === FileReader.LOADING) {
      reader.abort();
    }
    
    reader.readAsBinaryString(file.dom.files[0]);
  });

  // The sendData function is our main function
  function sendData() {
    // At first, if there is a file selected, we have to wait it is read
    // If it is not, we delay the execution of the function
    if(!file.binary && file.dom.files.length > 0) {
      setTimeout(sendData, 10);
      return;
    }

    // To construct our multipart form data request,
    // We need an HMLHttpRequest instance
    var XHR      = new XMLHttpRequest();

    // We need a sperator to define each part of the request
    var boundary = "blob";

    // And we'll store our body request as a string.
    var data     = "";

    // So, if the user has selected a file
    if (file.dom.files[0]) {
      // We start a new part in our body's request
      data += "--" + boundary + "\r\n";

      // We said it's form data (it could be something else)
      data += 'content-disposition: form-data; '
      // We define the name of the form data
            + 'name="'         + file.dom.name          + '"; '
      // We provide the real name of the file
            + 'filename="'     + file.dom.files[0].name + '"\r\n';
      // We provide the mime type of the file
      data += 'Content-Type: ' + file.dom.files[0].type + '\r\n';

      // There is always a blank line between the meta-data and the data
      data += '\r\n';
      
      // We happen the binary data to our body's request
      data += file.binary + '\r\n';
    }

    // For text data, it's simpler
    // We start a new part in our body's request
    data += "--" + boundary + "\r\n";

    // We said it's form data and give it a name
    data += 'content-disposition: form-data; name="' + text.name + '"\r\n';
    // There is always a blank line between the meta-data and the data
    data += '\r\n';

    // We happen the text data to our body's request
    data += text.value + "\r\n";

    // Once we are done, we "close" the body's request
    data += "--" + boundary + "--";

    // We define what will happen if the data are successfully sent
    XHR.addEventListener('load', function(event) {
      alert('Yeah! Data sent and response loaded.');
    });

    // We define what will happen in case of error
    XHR.addEventListener('error', function(event) {
      alert('Oups! Something goes wrong.');
    });

    // We setup our request
    XHR.open('POST', '/upload');

    // We add the required HTTP header to handle a multipart form data POST request
    XHR.setRequestHeader('Content-Type','multipart/form-data; boundary=' + boundary);
    XHR.setRequestHeader('Content-Length', data.length);

    // And finally, We send our data.
    // Due to Firefox's bug 416178, it's required to use sendAsBinary() instead of send()
    return XHR.sendAsBinary(data);
  }

  // At least, We need to access our form
  var form   = document.getElementById("contrib_form");

  // to take over the submit event
//  form.addEventListener('submit', function (event) {
//    event.preventDefault();
//    sendData();
//  });
});