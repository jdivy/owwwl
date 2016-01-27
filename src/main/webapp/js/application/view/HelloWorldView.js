define(['underscore',
        'jquery',
        'text!./hello-world-view.html',
        'css!./hello-world-view.css'],
function(_,
         $,
         templateHtml) {
  'use strict';
  
  function HelloWorldView() {
    this.$el = $('<div class="hello-world-view"></div>');
    this.renderTemplate = _.template(templateHtml);
  }
  
  HelloWorldView.prototype.render = function(){
    this.$el.html(this.renderTemplate());
  };
  
  return HelloWorldView;
})