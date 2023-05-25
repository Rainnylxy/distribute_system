//只在写入、删除数据时发挥作用，在写入、删除之前先写入Hlog
//Hlog生命周期： 构建-->滚动-->失效-->删除

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

//存储修改的元信息，包括写入日至时间等信息
public class HLogKey {
    public void initHLogKey(String tableName, // 修改表的名字
            Timestamp writeTime, // 写入日志时间
            long sequenceId, // 日志id
            List<UUID> clusterIds, // 集群id
            String regionName /* 修改的region */) {
    }
}

// 存储具体的修改的key-value信息
public class WALEdit implements Writable {
    private final ArrayList<KeyValue> kvs = new ArrayList<KeyValue>();
}

public class LogEntry {
    private WALEdit edit;
    private HLogKey key; // key: sequenceId, write time, region name, table name
}

public class HLog {
    ArrayList<LogEntry> logEntries = new ArrayList<LogEntry>();

    // Hlog滚动，定期删除过期的log，以一小时为例
    private long rollPeriod = 3600;

    // 提交日志修改，相当于先放在缓冲区中
    public void append() {
    };

    // 将日志写入磁盘中
    public void write() {
    };

    // 定时回滚日志，删除没有用的log
    public void rollWriter() {
    };

    // 删除旧日志
    public void cleanOldLogs() {
    };
}
