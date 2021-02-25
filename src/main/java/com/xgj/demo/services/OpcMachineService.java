package com.xgj.demo.services;

import com.xgj.demo.enums.WsdNodeEnum;
import com.xgj.demo.utils.WsdUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiConsumer;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

/**
 * @author gjXia
 * @date 2021/2/24 17:22
 */
@Slf4j
@Component
public class OpcMachineService implements CommandLineRunner {

    @Autowired
    private OpcService opcService;


    private final Map<String, NodeId> nodeIdMap = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        // 读取和设置节点
        for (int i = 0; i < WsdNodeEnum.values().length; i++) {
            WsdNodeEnum nodeEnum = WsdNodeEnum.values()[i];
            NodeId nodeId = new NodeId(nodeEnum.getNamespaceIndex(), nodeEnum.getIdentifier());
            nodeIdMap.put(nodeEnum.getNodeKey(), nodeId);
        }

        // 监视项创建请求
        List<MonitoredItemCreateRequest> reqList = new ArrayList<>();
        Iterator<NodeId> iterator = nodeIdMap.values().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            ReadValueId readValueId = new ReadValueId(iterator.next(), AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);
            MonitoringParameters monParam = new MonitoringParameters(
                    // 代号
                    uint(index++),
                    // 间隔
                    1000.0,
                    // 过滤值
                    null,
                    // 队列长度
                    uint(100),
                    // 覆盖旧值
                    true);
            MonitoredItemCreateRequest req = new MonitoredItemCreateRequest(
                    // 地址
                    readValueId,
                    // 模式
                    MonitoringMode.Reporting,
                    // 参数
                    monParam);

            reqList.add(req);
        }

        // 回调函数
        BiConsumer<UaMonitoredItem, Integer> callBack = (item, id) -> item.setValueConsumer((t, u) -> {
            try {
                // 节点判断
                if (item.getReadValueId().getNodeId().getIdentifier().toString().equals(WsdNodeEnum.VD116.getIdentifier())) {
                    // 读取到平均温度反馈
                    recordValue(WsdNodeEnum.VD116);

                } else if (item.getReadValueId().getNodeId().getIdentifier().toString().equals(WsdNodeEnum.VD100.getIdentifier())) {
                    // 读取到"大车间室内温度1"反馈
                    recordValue(WsdNodeEnum.VD100);

                } else if (item.getReadValueId().getNodeId().getIdentifier().toString().equals(WsdNodeEnum.VD108.getIdentifier())) {
                    // 读取到"大车间室内温度2"反馈
                    recordValue(WsdNodeEnum.VD108);

                } else if (item.getReadValueId().getNodeId().getIdentifier().toString().equals(WsdNodeEnum.VD104.getIdentifier())) {
                    // 读取到"大车间室内湿度1"反馈
                    recordValue(WsdNodeEnum.VD104);

                } else if (item.getReadValueId().getNodeId().getIdentifier().toString().equals(WsdNodeEnum.VD112.getIdentifier())) {
                    // 读取到"大车间室内湿度2"反馈
                    recordValue(WsdNodeEnum.VD112);
                }
            } catch (Exception e) {
                log.error("服务器错误：" + e.getMessage());
            }
        });

        // 订阅信号
        /*UaSubscription subscription = opcService.getClient().getSubscriptionManager().createSubscription(
                // 间隔
                1000.0, uint((long) 3600 * 24 * 365), uint((long) 3600 * 24 * 365), uint((long) 200), true, UByte.valueOf(1)).get();
        subscription.createMonitoredItems(TimestampsToReturn.Both,
                // 请求列表
                reqList,
                // 回调函数
                callBack).get();*/
    }

    /**
     * 将读取的值，写入到自定义集合中
     */
    private void recordValue(WsdNodeEnum nodeEnum) {
        String value = opcService.readValue(nodeIdMap.get(nodeEnum.getNodeKey()));
        WsdUtil.getInstance().add(nodeEnum.getNodeKey(), value);
    }
}
