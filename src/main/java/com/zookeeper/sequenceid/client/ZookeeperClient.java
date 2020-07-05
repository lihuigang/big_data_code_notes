package com.zookeeper.sequenceid.client;

import com.zookeeper.sequenceid.sequence.ZkSequence;
import com.zookeeper.sequenceid.sequence.ZkSequenceEnum;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author john
 * @date 2020/4/27 - 10:57
 */
@Data
public class ZookeeperClient {
    private static Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);
    private String host;
    private String sequencePath;

    // 重试休眠时间
    private final int SLEEP_TIME_MS = 1000;
    // 最大重试1000次
    private final int MAX_RETRIES = 1000;
    //会话超时时间
    private final int SESSION_TIMEOUT = 30 * 1000;
    //连接超时时间
    private final int CONNECTION_TIMEOUT = 3 * 1000;

    //创建连接实例
    private CuratorFramework client = null;
    // 序列化集合
    private Map<String, ZkSequence> zkSequence = Maps.newConcurrentMap();

    public ZookeeperClient(String host, String sequencePath) {
        this.host = host;
        this.sequencePath = sequencePath;
    }

    @PostConstruct
    public void init() throws Exception {
        this.client = CuratorFrameworkFactory.builder()
                .connectString(this.host)
                .connectionTimeoutMs(CONNECTION_TIMEOUT)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES)).build();
        this.client.start();
        this.initZkSequence();
    }

    public void initZkSequence() {
        ZkSequenceEnum[] list = ZkSequenceEnum.values();
        for (int i = 0; i < list.length; i++) {
            String name = list[i].name();
            String path = this.sequencePath + name;
            ZkSequence seq = new ZkSequence(path, this.client);
            zkSequence.put(name, seq);
        }
    }

    /**
     * 生成SEQ
     *
     * @param name
     * @return
     * @throws Exception
     */
    public Long sequence(ZkSequenceEnum name) {
        try {
            ZkSequence seq = zkSequence.get(name.name());
            if (seq != null) {
                return seq.sequence();
            }
        } catch (Exception e) {
            //logger.error("获取[{}]Sequence错误:{}", name, e);
        }
        return null;
    }
}
