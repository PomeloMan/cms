package io.cms.core.configure.command;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CMD执行类
 */
@Component
public class CommandRunner implements CommandLineRunner {

	private final Log logger = LogFactory.getLog(CommandRunner.class);
	private boolean debug = logger.isDebugEnabled();

	@Value("${command.web.openUrl}")
	private String openUrl;
	@Value("${command.web.openable}")
	private boolean openable;

	@Override
	public void run(String... arg0) throws Exception {
		if (openable) {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URI(openUrl));
				} catch (IOException | URISyntaxException e) {
					if (debug) {
						logger.debug(e.getMessage(), e);
					}
					logger.warn(e.getMessage());
				}
			} else {
				Runtime runtime = Runtime.getRuntime();
				try {
					// 启动运行浏览器打开相应网址
					runtime.exec("explorer " + openUrl);
				} catch (IOException e) {
					if (debug) {
						logger.debug(e.getMessage(), e);
					}
					logger.warn(e.getMessage());
				}
			}
		}
	}

}
