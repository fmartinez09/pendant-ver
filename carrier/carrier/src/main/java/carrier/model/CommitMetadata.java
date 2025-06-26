package carrier.model;

import java.util.List;

public class CommitMetadata {
    public String oid;
    public String head;
    public List<String> chunks;
    public String timestamp;
    public List<String> parents;
    public String tenant;
}

