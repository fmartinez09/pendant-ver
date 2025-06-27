package carrier.pulsar;

import carrier.model.CommitMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.inject.Inject;

@ApplicationScoped
public class BlobEventsProducer {

    @Inject
    @Channel("blob-committed")
    Emitter<CommitMetadata> emitter;

    public void emitBlobCommitted(CommitMetadata meta) {
        emitter.send(meta);
    }

}