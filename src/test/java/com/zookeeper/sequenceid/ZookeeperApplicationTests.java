package com.zookeeper.sequenceid;

import com.zookeeper.sequenceid.sequence.Sequences;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZookeeperApplicationTests {

    // 第一步，注入Sequences
    @Autowired
    private Sequences sequences;

    @Test
    void contextLoads() {
        for (int i = 0; i < 10; i++) {
            System.out.println("sequenceApCollection生成的自增id为:" + sequences.sequenceApCollection());
        }
    }

}