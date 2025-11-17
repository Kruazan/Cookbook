// src/types/index.ts

export interface AuthResponse {
    token: string;
    userId: number;
}

export interface StepIngredient {
    id: number;
    ingredientId: number;
    ingredientName: string;
    quantity: number;
}

export interface RecipeStep {
    id: number;
    stepOrder: number;
    processId: number;
    processName: string;
    duration: number;
    additionalNote: string;
    stepIngredients: StepIngredient[];
}

export interface Recipe {
    id: number;
    name: string;
    cookingTime: number;
    weight: number;
    calories: number;
    steps: RecipeStep[];
    createdById: number;
}

export interface RecipeFormData {
    title: string;
    ingredients: string;
    instructions: string;
}

// Определение типа ингредиента
export enum IngredientType {
    VEGETABLE = 'VEGETABLE',
    FRUIT = 'FRUIT',
    MEAT = 'MEAT',
    DAIRY = 'DAIRY',
    GRAIN = 'GRAIN',
    SPICE = 'SPICE',
    SEAFOOD = 'SEAFOOD',
    NUT = 'NUT',
    OIL = 'OIL',
    OTHER = 'OTHER',
}

export interface Ingredient {
    id: number;
    name: string;
    type: IngredientType;
}

// ← ДОБАВЛЯЕМ ЭТО:
export interface Process {
    id: number;
    name: string;
    userId: number;
}