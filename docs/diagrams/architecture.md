# 🏗️ Архитектура Cookbook

```mermaid
graph TB
    subgraph "👤 Пользователь"
        U[Пользователь]
    end

    %% ---------- Frontend ----------
    subgraph "🌐 Frontend (Blazor WebAssembly)"
        U --> |HTTPS| BA[Blazor App<br/>Cookbook]
        BA --> |REST API| GLB
    end

    %% ---------- Gateway ----------
    GLB[Kestrel<br/>Web Server]

    %% ---------- Backend ----------
    subgraph "⚙️ ASP.NET Core Backend"
        GLB --> |/api/*| SB[ASP.NET Core]
        SB --> RC[Recipe<br/>Controller]
        SB --> UC[User<br/>Controller]
        SB --> CC[Category<br/>Controller]
        SB --> TC[Tag<br/>Controller]
        SB --> FC[Favorite<br/>Controller]
    end

    %% ---------- Services ----------
    subgraph "🔧 Сервисный слой"
        RC --> RS[Recipe Service]
        UC --> US[User Service]
        CC --> CS[Category Service]
        TC --> TS[Tag Service]
        FC --> FS[Favorite Service]
        
        RS --> PS[Parsing Service<br/>Web Import]
        RS --> CMS[Cooking Mode Service]
    end

    %% ---------- Repositories ----------
    subgraph "🗃️ Доступ к данным"
        RS --> RR[(Recipe<br/>Repository)]
        US --> UR[(User<br/>Repository)]
        CS --> CR[(Category<br/>Repository)]
        TS --> TR[(Tag<br/>Repository)]
        FS --> FR[(Favorite<br/>Repository)]
    end

    %% ---------- База данных ----------
    subgraph "🐘 PostgreSQL"
        RR --> DB[(PostgreSQL<br/>CookbookDB)]
        UR --> DB
        CR --> DB
        TR --> DB
        FR --> DB
    end

    %% ---------- Внешние связи ----------
    subgraph "🔗 Связи сущностей"
        RS -.-> |ManyToOne| US
        RS -.-> |ManyToOne| CS
        RS -.-> |ManyToMany| TS
        FS -.-> |ManyToOne| US
        FS -.-> |ManyToOne| RS
    end

    %% ---------- Deployment ----------
    subgraph "🐳 Deployment"
        BA -.-> |publish| STAT[Static Files<br/>Web Host]
        SB -.-> |container| DOCK[Docker<br/>Image]
        DB -.-> |volume| VOL[(Persistent<br/>Volume)]
    end

    classDef frontend fill:#61dafb,stroke:#282c34,color:#000
    classDef backend fill:#6db33f,stroke:#fff,color:#000
    classDef db fill:#336791,stroke:#fff,color:#fff
    classDef relations fill:#ff6b6b,stroke:#000,color:#000
    classDef deployment fill:#239aef,stroke:#fff,color:#fff

    class BA frontend
    class SB,RC,UC,CC,TC,FC,RS,US,CS,TS,FS,PS,CMS backend
    class DB,RR,UR,CR,TR,FR,VOL db
    class relations relations
    class STAT,DOCK deployment
