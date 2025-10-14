# 📊 Диаграмма классов Cookbook

```mermaid
classDiagram
    class User {
        <<Entity>>
        +Guid Id
        +string Username
        +string Email
        +string PasswordHash
        +DateTime CreatedAt
        +List~Recipe~ Recipes
        +List~FavoriteRecipe~ FavoriteRecipes
        +User()
        +getId() Guid
        +getUsername() String
        +setUsername(String) void
        +getEmail() String
        +setEmail(String) void
    }

    class Recipe {
        <<Entity>>
        +Guid Id
        +string Title
        +string Description
        +int PrepTime
        +int CookTime
        +int Servings
        +string Difficulty
        +string ImageUrl
        +User Author
        +Category Category
        +List~RecipeStep~ Steps
        +List~Ingredient~ Ingredients
        +List~Tag~ Tags
        +List~FavoriteRecipe~ FavoritedBy
        +DateTime CreatedAt
        +DateTime UpdatedAt
        +Recipe()
        +getId() Guid
        +getTitle() String
        +setTitle(String) void
        +getDescription() String
        +setDescription(String) void
    }

    class RecipeStep {
        <<Entity>>
        +Guid Id
        +int StepNumber
        +string Instruction
        +int? TimerSeconds
        +Recipe Recipe
        +RecipeStep()
        +getId() Guid
        +getStepNumber() int
        +setStepNumber(int) void
        +getInstruction() String
        +setInstruction(String) void
    }

    class Ingredient {
        <<Entity>>
        +Guid Id
        +string Name
        +string Amount
        +string Unit
        +Recipe Recipe
        +Ingredient()
        +getId() Guid
        +getName() String
        +setName(String) void
        +getAmount() String
        +setAmount(String) void
    }

    class Category {
        <<Entity>>
        +Guid Id
        +string Name
        +string Description
        +List~Recipe~ Recipes
        +Category()
        +getId() Guid
        +getName() String
        +setName(String) void
    }

    class Tag {
        <<Entity>>
        +Guid Id
        +string Name
        +List~Recipe~ Recipes
        +Tag()
        +getId() Guid
        +getName() String
        +setName(String) void
    }

    class FavoriteRecipe {
        <<Entity>>
        +Guid Id
        +User User
        +Recipe Recipe
        +DateTime AddedAt
        +FavoriteRecipe()
        +getId() Guid
        +getUser() User
        +setUser(User) void
    }

    %% ---- Associations ----
    User "1" o-- "0..*" Recipe : authors
    User "1" o-- "0..*" FavoriteRecipe : favorites
    Recipe "1" o-- "1..*" RecipeStep : contains_steps
    Recipe "1" o-- "1..*" Ingredient : contains_ingredients
    Recipe "1" --> "1" Category : belongs_to_category
    Recipe "0..*" -- "0..*" Tag : tagged_with
    Recipe "1" o-- "0..*" FavoriteRecipe : favorited_by
    FavoriteRecipe "1" --> "1" User : user
    FavoriteRecipe "1" --> "1" Recipe : recipe
