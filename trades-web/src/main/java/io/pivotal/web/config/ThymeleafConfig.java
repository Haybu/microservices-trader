package io.pivotal.web.config;

//@Configuration
public class ThymeleafConfig {
	
	/*
	 * Spring Boot Autoconfig adding the SpringSecurityDialect bean automatically from
	 * 1.3.0.M4???? 
	 */
	/*
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}
	*/
	
	/*
	@Bean 
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setOrder(1);
		return resolver;
	}
	
	@Bean 
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		Set<IDialect> dialects = new HashSet<IDialect>();
        dialects.add(new SpringSecurityDialect());
        dialects.add(new LayoutDialect());
        engine.setAdditionalDialects(dialects);
		return engine;
	}
	
	@Bean 
	public ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		return resolver;
	}
	*/
}
