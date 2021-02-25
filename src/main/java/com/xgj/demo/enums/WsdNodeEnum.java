package com.xgj.demo.enums;

import lombok.Getter;

/**
 * @author gjXia
 * @date 2021/2/25 8:49
 */
public enum WsdNodeEnum {

    /**
     * 温湿度 节点配置
     */
    VD116(2, "cooler.设备 1.大车间室内平均温度", "avg_temp"),
    VD104(2, "cooler.设备 1.大车间室内湿度1", "hum_1"),
    VD112(2, "cooler.设备 1.大车间室内湿度2", "hum_2"),
    VD100(2, "cooler.设备 1.大车间室内温度1", "temp_1"),
    VD108(2, "cooler.设备 1.大车间室内温度2", "temp_2"),
    ;

    @Getter
    private final int namespaceIndex;

    @Getter
    private final String identifier;

    @Getter
    private final String nodeKey;

    WsdNodeEnum(int namespaceIndex, String identifier, String nodeKey) {
        this.namespaceIndex = namespaceIndex;
        this.identifier = identifier;
        this.nodeKey = nodeKey;
    }
}
