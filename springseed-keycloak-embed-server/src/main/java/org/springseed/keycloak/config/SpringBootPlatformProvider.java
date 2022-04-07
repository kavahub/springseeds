package org.springseed.keycloak.config;

import java.io.File;

import org.keycloak.platform.PlatformProvider;
import org.keycloak.services.ServicesLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * 继承{@link PlatformProvider}
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
public class SpringBootPlatformProvider implements PlatformProvider {

	Runnable shutdownHook;

	@Override
	public void onStartup(Runnable startupHook) {
		startupHook.run();
	}

	@Override
	public void onShutdown(Runnable shutdownHook) {
		this.shutdownHook = shutdownHook;
	}

	@Override
	public void exit(Throwable cause) {
		ServicesLogger.LOGGER.fatal(cause);
		exit(1);
	}

	private void exit(int status) {
		new Thread() {
			@Override
			public void run() {
				System.exit(status);
			}
		}.start();
	}

    @Override
    public File getTmpDirectory() {
        return createTempDir();
    }
    
	protected File createTempDir() {
        String tmpDirBase = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpDirBase, "keycloak-embed-server");
        boolean couldCreateDirs = tmpDir.mkdirs();
        if (couldCreateDirs || tmpDir.exists()) {
            log.debug("Using server tmp directory: {}", tmpDir.getAbsolutePath());
            return tmpDir;
        }

        throw new RuntimeException("Failed to create temp directory: " + tmpDir.getAbsolutePath());
    }
}
