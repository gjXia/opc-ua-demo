package com.xgj.demo.services;

import lombok.SneakyThrows;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

/**
 * @author gjXia
 * @date 2021/2/24 17:09
 */
@Service
public class OpcService {

    private static final String APPLICATION_NAME = "twxma opc-ua client";

    private static final String APPLICATION_URI = "urn:ca:twxma:client:ua";

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Value("${opc.ua.url}")
    private String opcUaUrl;

    private OpcUaClient client = null;

    public void init() {
        try {
            if (client == null) {
                logger.info("开始连接---OPC UA服务---");
                client = createClient();
                client.connect();
                logger.info("OPC UA服务连接成功。。。");
            }
        } catch (Exception e) {
            logger.error("OPC UA服务连接异常：", e);
        }
    }

    public OpcUaClient getClient() {
        return this.client;
    }

    /**
     * 创建对象
     */
    private OpcUaClient createClient() throws Exception {
        // opc地址
        String discoveryUrl = opcUaUrl;
        logger.info("URL of discovery endpoint = {}", discoveryUrl);

        List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints(discoveryUrl).get();

        logger.info("Available endpoints:");
        for (EndpointDescription endpointDescription : endpoints) {
            logger.info(endpointDescription.getEndpointUrl() + " " + endpointDescription.getSecurityPolicyUri());
        }

        EndpointDescription endpoint = chooseEndpoint(
                endpoints,
                SecurityPolicy.None,
                MessageSecurityMode.None
        );

        logger.info("Using endpoint: {} [{}, {}]", endpoint.getEndpointUrl(), endpoint.getSecurityPolicyUri(),
                endpoint.getSecurityMode());

        OpcUaClientConfig config = OpcUaClientConfig.builder()
                .setApplicationName(LocalizedText.english(APPLICATION_NAME))
                .setApplicationUri(APPLICATION_URI)
                .setEndpoint(endpoint)
                .setIdentityProvider(new AnonymousProvider())
                .setRequestTimeout(uint(5000))
                .build();

        return OpcUaClient.create(config);
    }


    private EndpointDescription chooseEndpoint(List<EndpointDescription> endpoints,
                                               SecurityPolicy minSecurityPolicy,
                                               MessageSecurityMode minMessageSecurityMode) {

        EndpointDescription bestFound = null;

        for (EndpointDescription endpoint : endpoints) {
            SecurityPolicy endpointSecurityPolicy;
            try {
                endpointSecurityPolicy = SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri());
            } catch (UaException e) {
                continue;
            }
            if (minSecurityPolicy.compareTo(endpointSecurityPolicy) <= 0) {
                if (minMessageSecurityMode.compareTo(endpoint.getSecurityMode()) <= 0) {
                    if (bestFound == null) {
                        bestFound = endpoint;
                        break;
                    }
                }
            }
        }
        if (bestFound == null) {
            throw new RuntimeException("no desired endpoints returned");
        } else {
            return bestFound;
        }
    }

    /**
     * 写值
     *
     * @param val
     * @param nodeId
     */
    public void writeValue(Short val, NodeId nodeId) {
        Variant var = new Variant(val);
        DataValue dv = new DataValue(var, null, null);
        this.getClient().writeValue(nodeId, dv);
    }

    /**
     * 读值
     *
     * @param nodeId
     */
    @SneakyThrows
    public String readValue(NodeId nodeId) {
        VariableNode node = this.getClient().getAddressSpace().createVariableNode(nodeId);
        DataValue value = node.readValue().get();
        return value.getValue().getValue().toString();
    }
}
