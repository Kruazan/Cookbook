import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { RootState } from './index'; // Убедитесь, что RootState импортирован
import { Ingredient } from '../types';

export const fetchIngredients = createAsyncThunk<Ingredient[], void, { state: RootState }>(
    'ingredients/fetchIngredients',
    async (_, { getState }) => {
        const { auth: { token } } = getState(); // Получаем токен из состояния
        const response = await fetch('http://localhost:8080/api/ingredients', {
            method: 'GET', // Используем GET для получения списка
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        });
        if (!response.ok) {
            throw new Error(`Ошибка: ${response.status} - ${await response.text()}`);
        }
        const data = await response.json();
        return data as Ingredient[]; // Явно указываем тип возвращаемых данных
    }
);

const ingredientSlice = createSlice({
    name: 'ingredients',
    initialState: {
        items: [] as Ingredient[],
        loading: false,
        error: null as string | null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchIngredients.pending, (state) => {
                state.loading = true;
            })
            .addCase(fetchIngredients.fulfilled, (state, action) => {
                state.items = action.payload;
                state.loading = false;
            })
            .addCase(fetchIngredients.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch ingredients';
            });
    },
});

export default ingredientSlice.reducer;