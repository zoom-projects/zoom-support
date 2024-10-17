package com.hb0730.zoom.sofa.rpc.core.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@Getter
@Setter
public class RpcConfigProperties {
    public static final String NAME = "name";
    public static final String VERSION = "version";
    public static final String ADDRESS = "address";
    
    private String name;
    private String version;
    private String address;
}
