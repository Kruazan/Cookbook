import { configureStore } from '@reduxjs/toolkit';
import authReducer from './authSlice';
import recipeReducer from './recipeSlice';
import { useDispatch } from 'react-redux'; // Импортируем useDispatch
import ingredientReducer from './ingredientSlice';
import processReducer from './processSlice';

export const store = configureStore({
    reducer: {
        auth: authReducer,
        recipes: recipeReducer,
        ingredients: ingredientReducer,
        processes: processReducer,
    },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export const useAppDispatch = () => useDispatch<AppDispatch>(); // Создаём useAppDispatch
