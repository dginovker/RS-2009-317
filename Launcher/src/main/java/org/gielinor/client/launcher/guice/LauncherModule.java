package org.gielinor.client.launcher.guice;

import org.gielinor.client.launcher.configuration.Configuration;
import org.gielinor.client.launcher.configuration.impl.YAMLConfiguration;

import com.google.inject.AbstractModule;

/**
 * Created by Mike on 2/21/2015.
 */
public class LauncherModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Configuration.class).to(YAMLConfiguration.class);
    }
}