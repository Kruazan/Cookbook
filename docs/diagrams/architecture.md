graph TB
    %% ---------- Пользователь ----------
    subgraph "👤 Пользователь"
        U[Пользователь]
    end

    %% ---------- Frontend ----------
    subgraph "🌐 Frontend"
        U --> |HTTPS| RA[React App / Razor Pages]
        RA --> |REST API| GLB
    end

    %% ---------- Gateway / LB ----------
    GLB[Nginx Load Balancer]

    %% ---------- Backend ----------
    subgraph "⚙️ ASP.NET Core Backend"
        GLB --> |/api/*| API[ASP.NET Core Controllers]
        API --> RC[RecipeController]
        API --> UC[UserController]
        API --> IC[ImportController]
        API --> AC[AuthController]
    end

    %% ---------- Services ----------
    subgraph "🔧 Сервисный слой"
        RC --> RS[RecipeService]
        UC --> US[UserService]
        IC --> IS[ImportService]
        AC --> AS[AuthService]
    end

    %% ---------- Repositories ----------
    subgraph "🗃️ Репозитории"
        RS --> RR[RecipeRepository]
        US --> UR[UserRepository]
        IS --> IR[ImportRepository]
    end

    %% ---------- База данных ----------
    subgraph "🐘 PostgreSQL"
        RR --> DB[(PostgreSQL)]
        UR --> DB
        IR --> DB
    end

    %% ---------- Deployment ----------
    subgraph "🐳 Deployment"
        RA -.-> STATIC[Static Files CDN]
        API -.-> DOCK[Docker Image]
        DB -.-> VOL[Persistent Volume]
    end

    %% ---------- Цветовые классы ----------
    classDef frontend fill:#61dafb,stroke:#282c34,color:#000
    classDef backend fill:#178600,stroke:#fff,color:#fff
    classDef service fill:#ffd966,stroke:#000,color:#000
    classDef repo fill:#9fc5e8,stroke:#000,color:#000
    classDef db fill:#336791,stroke:#fff,color:#fff
    classDef deployment fill:#239aef,stroke:#fff,color:#fff

    class RA,GLB frontend
    class API,RC,UC,IC,AC backend
    class RS,US,IS,AS service
    class RR,UR,IR repo
    class DB db
    class STATIC,DOCK,VOL deployment
