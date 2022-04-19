package org.springseed.oss.local;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("springseeds.oss.local")
public class OSSProperties {
    private Path uploadRootPath = Paths.get(System.getProperty("user.dir"), ".springseed", "oss");
}
