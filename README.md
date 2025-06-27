# Pendant-Ver

A large versioning system for Binary Large Object with distributed file system and semantic MVCC,  Garbage Collection by reachability. Directly support for multipart upload, resumable transfers, backpressure, flow control, chunk compression, semantic deduplication, declarative infra with terraform and nixos. 

```
          ┌────────────────────────────┐
          │      Client (git-lfs)      │
          └────────────┬───────────────┘
                       │
              ┌────────▼───────────┐
              │    Carrier API     │ ← Entry point (REST/gRPC)
              └────────┬───────────┘
                       │
         ┌─────────────┼────────────────┐
         ▼                              ▼
 ┌───────────────┐             ┌────────────────┐
 │  Seaweedfs    │             │     Mini-DB    │
 │ (/chunks/)    │             │ (metadata+MVCC)│
 └─────┬─────────┘             └──────┬─────────┘
       │                              │
       ▼                              ▼
[Disk / SeaweedFS]              [FoundationDB]
       │                              │
       └────────────┬─────────────────┘
                    ▼
             Apache Pulsar (events)
```


```
                           [Client Git LFS / Frontend]
                                       │
                ┌─────────────────────┴────────────────────┐
                │            Quarkus Gateway               │
                │  /v1/blob/presign-upload | /commit       │
                └─────────────┬────────────────────────────┘
                              │
                  ┌───────────▼────────────┐
                  │ MetadataService        │
                  │ - FDB insert           │
                  │ - Pulsar publish       │
                  └───────────┬────────────┘
                              ▼
                          [Pulsar]

 ┌───────────────┬───────────────┬────────────────┬───────────────┐
 ▼               ▼               ▼                ▼               ▼
AuditLogSvc   IndexingSvc   BlobValidatorSvc   SnapshotMgr     GCService
(Journal)     (Views)        (hash check)       (CoW, tag)     (reachability)

                                              │
                                              ▼
                                     [Blob I/O Layer (C++)]
                                     - fsync, rename, mmap

```

# Flow

```
[Git LFS Client / Frontend]
        │
        ▼
POST /v1/blob/presign-upload  ──►  [PresignService]
                                 └─> Generate signed URL with expiration

Client PUT Blob ───────────────►  [SeaweedFS]
                                 (Validates JWT signature and saves the chunk)

POST /v1/blob/commit ─────────►  [MetadataService]
                                 ├─> Check availability on SeaweedFS
                                 ├─> Write metadata to FoundationDB
                                 └─> Produces `blob.committed` event

EVENT: blob.committed ─────────► [Pulsar Topic]
```

# Future own BlobFS

```
╔═════════════════════╗
║ Pulsar Consumers:   ║
║                     ║
║ - AuditLogService   ║◄─ Log into WORM, FDB audit logs
║ - IndexingService   ║◄─ Extract useful information for searching by HEAD/tenant
║ - GCService         ║◄─ Orchestrates retention, snapshots, I/O fsync ◄─┐
╚═════════════════════╝                                                  │
                                                                         │
                                                ┌──────────────────────┐ │
                                                │  Blob I/O Layer (C++)│◄┘
                                                │  - fsync, rename     │
                                                │  - mmap, CoW         │
                                                └──────────────────────┘
```