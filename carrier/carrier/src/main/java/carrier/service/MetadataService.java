package carrier.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import carrier.model.CommitMetadata;
import carrier.fdb.MetadataRepository;
import carrier.pulsar.BlobEventsProducer;

@ApplicationScoped
public class MetadataService {

    @Inject
    MetadataRepository fdbRepo;

    @Inject
    BlobEventsProducer events;

    public boolean commit(CommitMetadata meta) {
        boolean ok = fdbRepo.saveCommit(meta);
        if (ok) events.emitBlobCommitted(meta);
        return ok;
    }
}