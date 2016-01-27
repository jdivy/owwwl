({
  appDir: '../',
  baseUrl: 'js/lib',
  mainConfigFile: './requireJsConfiguration.js',
  
  separateCSS: true,
  
  optimizeCss: 'standard',
  
  skipDirOptimize: true,
  
  preserveLicenseComments: false,
  
  modules: [{
    name: 'application',
    include: ['application/main']
  }]
})