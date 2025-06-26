package carrier.pulsar;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.inject.Inject;

@ApplicationScoped
public class BlobEventsProducer {

    @Inject
    @Channel("blob-uploaded")
    Emitter<String> emitter;

    public void emitBlobUploaded(String oid, String chunk) {
        emitter.send("{\"oid\":\"" + oid + "\", \"chunk\":\"" + chunk + "\"}");
    }
}