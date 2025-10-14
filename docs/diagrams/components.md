# 📊 Диаграмма компонентов Cookbook

```mermaid
graph TB
    subgraph "👤 Клиентская машина"
        WB[<<component>> Веб-браузер]
    end

    WB --> |HTTP/HTTPS| WS[<<component>> Веб-сервер Kestrel]

    subgraph "🌐 Frontend"
        BA[<<component>> Blazor WebAssembly App]
    end

    subgraph "⚙️ Backend"
        SB[<<component>> ASP.NET Core API]
        RC[<<component>> Recipe Controller]
        UC[<<component>> User Controller]
        CC[<<component>> Category Controller]
        TC[<<component>> Tag Controller]
        CMS[<<component>> Cooking Mode Service]
        PS[<<component>> Parser Service]
    end

    subgraph "🗃️ Data Access"
        RR[<<component>> Recipe Repository]
        UR[<<component>> User Repository]
        CR[<<component>> Category Repository]
        TR[<<component>> Tag Repository]
    end

    subgraph "💾 Database"
        PG[<<component>> PostgreSQL]
    end

    %% Connections
    WS --> BA
    BA --> SB
    SB --> RC
    SB --> UC
    SB --> CC
    SB --> TC
    RC --> CMS
    RC --> PS
    RC --> RR
    UC --> UR
    CC --> CR
    TC --> TR
    RR --> PG
    UR --> PG
    CR --> PG
    TR --> PG

    classDef frontend fill:#61dafb,stroke:#282c34,color:#000
    classDef backend fill:#6db33f,stroke:#fff,color:#000
    classDef data fill:#336791,stroke:#fff,color:#fff
    classDef db fill:#754dbc,stroke:#fff,color:#fff

    class WB,BA frontend
    class SB,RC,UC,CC,TC,CMS,PS backend
    class RR,UR,CR,TR data
    class PG db
