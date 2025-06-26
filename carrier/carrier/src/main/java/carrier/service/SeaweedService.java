package carrier.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.nio.file.*;
import java.io.IOException;

@ApplicationScoped
public class SeaweedService {

    public boolean storeChunk(String oid, String chunkN, byte[] data) {
        try {
            Path path = Paths.get("/mnt/seaweed/chunks", oid, "chunk-" + chunkN);
            Files.createDirectories(path.getParent());
            try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
                fos.write(data);
                fos.flush();
                FileDescriptor fd = fos.getFD();
                fd.sync();  // fsync real
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}