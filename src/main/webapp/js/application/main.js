define(['application/view/HelloWorldView'], 
function(HelloWorldView){
  'use strict';
  
  var helloWorldView = new HelloWorldView();
  helloWorldView.$el.appendTo(document.body);
  helloWorldView.render();
});