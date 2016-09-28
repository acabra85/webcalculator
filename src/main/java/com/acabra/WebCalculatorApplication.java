package com.acabra;

import com.acabra.calculator.CalculatorManager;
import com.acabra.calculator.resources.WebCalculatorResource;
import com.acabra.calculator.view.RenderType;
import com.acabra.calculator.view.WebCalculatorRenderFactory;
import com.acabra.health.TemplateHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * @author acabra
 * @version 2016-09-27
 */
public class WebCalculatorApplication extends Application<WebCalculatorConfiguration> {

    public static void main(String [] args) throws Exception {
        new WebCalculatorApplication().run(args);
    }

    @Override
    public String getName(){
        return "Web-Calculator";
    }

    @Override
    public void initialize(Bootstrap<WebCalculatorConfiguration> bootstrap) {
        bootstrap.addBundle(new Java8Bundle());
    }

    @Override
    public void run(WebCalculatorConfiguration configuration, Environment environment) throws Exception {

        final FilterRegistration.Dynamic cors = environment.servlets()
                .addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        final JerseyEnvironment env = environment.jersey();
        CalculatorManager calculatorManager = new CalculatorManager(WebCalculatorRenderFactory.createRenderer(RenderType.HTML));
        env.register(new WebCalculatorResource(calculatorManager));

        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

    }
}
