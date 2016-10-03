package com.acabra.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author acabra
 * @created 2016-09-27
 * This class is used to validate the template received in form of yml file.
 */
public class TemplateHealthCheck extends HealthCheck {
    private final String template;

    public TemplateHealthCheck(String template) {
        this.template = template;
    }

    @Override
    protected HealthCheck.Result check() throws Exception {
        final String saying = String.format(template, "TEST");
        if (!saying.contains("TEST")) {
            return HealthCheck.Result.unhealthy("template doesn't include a name");
        }
        return HealthCheck.Result.healthy();
    }
}
