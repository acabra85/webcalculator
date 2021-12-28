package com.acabra;

import com.acabra.calculator.WebCalculatorManager;
import com.acabra.calculator.job.WebCalculatorHistoryCleanerPolicy;
import com.acabra.calculator.job.WebCalculatorJobManager;
import com.acabra.calculator.resources.WebCalculatorResource;
import com.acabra.calculator.view.RenderType;
import com.acabra.calculator.view.WebCalculatorRenderFactory;
import com.acabra.health.TemplateHealthCheck;
import com.acabra.mmind.MMindResource;
import com.acabra.roulette.resource.RouletteResource;
import com.acabra.shared.CommonExecutorService;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;

/**
 * @author acabra
 * @version 2016-09-27
 */
public class WebCalculatorApplication extends Application<WebCalculatorConfiguration> {


    /**
     * Provides configuration for Cross Origin Requests
     *
     * @param environment the DropWizzard's enviroment
     */
    private void configureCORS(Environment environment) {
        final FilterRegistration.Dynamic cors = environment.servlets()
                .addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    /**
     * Allows static files to be served as resources
     *
     * @param bootstrap
     */
    private void provideResolutionForStaticAssets(Bootstrap<WebCalculatorConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html", "html"));
    }

    public static void main(String[] args) throws Exception {
        new WebCalculatorApplication().run(args);
    }

    @Override
    public String getName() {
        return "Web-Calculator";
    }

    @Override
    public void initialize(Bootstrap<WebCalculatorConfiguration> bootstrap) {
        provideResolutionForStaticAssets(bootstrap);
    }

    @Override
    public void run(WebCalculatorConfiguration configuration, Environment environment) throws Exception {

        configureCORS(environment);

        environment.jersey().setUrlPattern("/api/*");

        WebCalculatorManager webCalculatorManager = new WebCalculatorManager(WebCalculatorRenderFactory.createRenderer(RenderType.HTML));

        CommonExecutorService commonExecutorService = new CommonExecutorService();
        environment.jersey().register(new WebCalculatorResource(webCalculatorManager));
        environment.jersey().register(new RouletteResource(commonExecutorService));
        environment.jersey().register(new MMindResource(commonExecutorService));

        registerHealthChecks(configuration, environment);

        startJobManager(webCalculatorManager);

    }

    private void registerHealthChecks(WebCalculatorConfiguration configuration, Environment environment) {
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
    }

    private void startJobManager(WebCalculatorManager webCalculatorManager) {
        WebCalculatorHistoryCleanerPolicy policyCleaner = new WebCalculatorHistoryCleanerPolicy(ChronoUnit.MINUTES, 10);
        WebCalculatorJobManager webCalculatorJobManager = new WebCalculatorJobManager(webCalculatorManager, policyCleaner);
        webCalculatorJobManager.start();
    }

}
