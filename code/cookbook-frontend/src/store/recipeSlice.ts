import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import api from '../services/api';
import { Recipe } from '../types';

export const fetchRecipes = createAsyncThunk('recipes/fetchRecipes', async () => {
    const response = await api.get<Recipe[]>('/recipes');
    return response.data;
});

const recipeSlice = createSlice({
    name: 'recipes',
    initialState: {
        items: [] as Recipe[],
        loading: false,
        error: null as string | null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchRecipes.pending, (state) => {
                state.loading = true;
            })
            .addCase(fetchRecipes.fulfilled, (state, action) => {
                state.items = action.payload;
                state.loading = false;
            })
            .addCase(fetchRecipes.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch recipes';
            });
    },
});

export default recipeSlice.reducer;
