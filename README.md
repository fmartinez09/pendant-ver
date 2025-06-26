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