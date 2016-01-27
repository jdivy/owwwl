requirejs.config({
  baseUrl: '/js/lib',
  
  paths: {
    'application': '../application',
    'requireJsConfiguration': '../requireJsConfiguration',
    'style': '../../css'
  },
  
  map: {
    '*': {
      'css': 'require-css/css'
    }
  },
  
  waitSeconds: 120
});

requirejs.onError = function(err) {
  // Make sure we only display a single alert (to prevent browser death by a thousand alerts)
  if (window.requireJsErrorDisplayed) return;
  window.requireJsErrorDisplayed = true;
  
  var msg = 'Error Loading Application\n';
  var failedId = err.requireModules && err.requireModules[0];
  
  if(failedId) {
    msg += 'Could not load resource: ' + failedId;
  } else {
    msg += err;
  }
  
  alert(msg);
};