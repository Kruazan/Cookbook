# 📊 Диаграммы последовательностей Cookbook

### 1. Создание рецепта
```mermaid
sequenceDiagram
    participant User as Пользователь
    participant Frontend as Интерфейс
    participant RecipeController as RecipeController
    participant RecipeService as RecipeService
    participant CategoryService as CategoryService
    participant TagService as TagService
    participant RecipeRepo as RecipeRepository

    User->>Frontend: Заполняет форму рецепта
    Frontend->>RecipeController: POST /api/recipes {recipeData}
    RecipeController->>RecipeService: CreateRecipe(dto)
    
    RecipeService->>CategoryService: GetCategory(categoryId)
    CategoryService-->>RecipeService: Category
    
    RecipeService->>TagService: GetOrCreateTags(tagNames)
    TagService-->>RecipeService: List<Tag>
    
    RecipeService->>RecipeRepo: Save(recipe)
    RecipeRepo-->>RecipeService: Recipe с Id
    
    RecipeService-->>RecipeController: RecipeDto
    RecipeController-->>Frontend: 201 Created
    Frontend->>User: Рецепт успешно создан
