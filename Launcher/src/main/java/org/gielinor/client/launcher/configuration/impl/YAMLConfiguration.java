package org.gielinor.client.launcher.configuration.impl;

import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import java.util.regex.Matcher;

import org.gielinor.client.launcher.configuration.Configuration;
import org.gielinor.client.launcher.net.impl.AutoCloseableURLConnection;
import org.yaml.snakeyaml.Yaml;

/**
 * Created by Mike on 2/13/2015.
 */
@Singleton
public class YAMLConfiguration implements Configuration {

	private final Yaml yaml;
	private URL configurationURL;
	private Map<String, Object> configurationMap;

	public YAMLConfiguration() {
		yaml = new Yaml();
		try {
			configurationURL = new URL("https://gielinor.org/client/gielinor.yml");
		} catch (MalformedURLException ignored) {
		}

		try {
			this.load();
		} catch (Exception e) {
			System.out.println("Failed to load YAML properties file!");
		}
	}

	@SuppressWarnings("unchecked")
	private void load() {
		try (AutoCloseableURLConnection urlConnection = new AutoCloseableURLConnection(configurationURL)) {
			urlConnection.get().setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
			try (ReadableByteChannel rbc = Channels.newChannel(urlConnection.getInputStream())) {
				configurationMap = (Map<String, Object>) yaml.load(Channels.newInputStream(rbc));
			} catch (IOException e) {
				System.err.println("Failed to load YAML properties file!");
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println("Failed to load YAML properties file!");
			e.printStackTrace();
		}
	}

	//TODO: This is probably the most unsafe method I've ever seen... :^)
	@Override
	public String getMapValueByKey(String parent, String key) {
		@SuppressWarnings("unchecked")
		Map<String, String> tempMap = (Map<String, String>) configurationMap.get(parent);
		if (parent.equalsIgnoreCase("paths")) {
			return System.getProperty("user.home") + tempMap.get(key).replaceAll("\\(FS\\)", Matcher.quoteReplacement(File.separator));
		}
		return tempMap.get(key);
	}

}