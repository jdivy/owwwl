
@Configuration
class DispatcherConfig extends WebMvcConfigurationSupport {
  
  @Autowired
  ObjectMapper objectMapper
  
  @Bean
  static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
    return new PropertySourcesPlaceholderConfigurer()
  }
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry){
    super.addResourceHandlers(registry)
    registry.addResourceHandler("/css/**").addResourceLocations("/css/")
    registry.addResourceHandler("/img/**").addResourceLocations("/img/")
    registry.addResourceHandler("/js/**").addResourceLocations("/js/")
    registry.addResourceHandler("/font/**").addResourceLocations("/font/").setCachePeriod(60 * 60 * 24 * 365)
    
    // even without a favicon, this prevents us from getting error from the dispatcher servlet when the browser tries to fetch the favicon
    registry.addResourceHandler("/favicon.ico").addResourceLocations("/img/favicon.ico")
    registry.addResourceHandler("/robots.txt").addResourceLocations("/")
    
    // Make the resource handler have precedence over all controllers. We do this so
    // that RequestMappings like "/{mappingVariable}" don't match resources like index.html.
    registry.setOrder(-1)
  }
  
  @Override
  public ViewResolver viewResolver(){
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver()
    viewResolver.setPrefix("/WEB-INF/jsp/")
    viewResolver.setSuffix(".jsp")
    return viewResolver
  }
}