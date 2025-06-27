package carrier.fdb;


import com.apple.foundationdb.Database;
import com.apple.foundationdb.FDB;
import carrier.model.CommitMetadata;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MetadataRepository {

    private final Database db = FDB.selectAPIVersion(710).open();

    public boolean saveCommit(CommitMetadata meta) {
        try {
            db.run(tr -> {
                String prefix = "/commits/" + meta.oid + "/";
                tr.set((prefix + "head").getBytes(), meta.head.getBytes());
                tr.set((prefix + "timestamp").getBytes(), meta.timestamp.getBytes());
                tr.set((prefix + "chunks").getBytes(), String.join(",", meta.chunks).getBytes());
                return null;
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}