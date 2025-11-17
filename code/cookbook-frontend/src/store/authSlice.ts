import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import api from '../services/api';
import { AuthResponse } from '../types';

export const login = createAsyncThunk('auth/login', async ({ username, password }: { username: string; password: string }) => {
    const response = await api.post<AuthResponse>('/auth/login', { username, password });
    return response.data;
});

export const register = createAsyncThunk('auth/register', async ({ username, password, email }: { username: string; password: string; email: string }) => {
    const response = await api.post<AuthResponse>('/auth/register', { username, password, email });
    return response.data;
});

const authSlice = createSlice({
    name: 'auth',
    initialState: {
        token: null as string | null,
        userId: null as number | null,
        loading: false,
        error: null as string | null,
    },
    reducers: {
        logout: (state) => {
            state.token = null;
            state.userId = null;
            localStorage.removeItem('token');
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(login.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(login.fulfilled, (state, action) => {
                state.token = action.payload.token;
                state.userId = action.payload.userId;
                state.loading = false;
                localStorage.setItem('token', action.payload.token);
            })
            .addCase(login.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Login failed';
            })
            .addCase(register.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(register.fulfilled, (state, action) => {
                state.token = action.payload.token;
                state.userId = action.payload.userId;
                state.loading = false;
                localStorage.setItem('token', action.payload.token);
            })
            .addCase(register.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Registration failed';
            });
    },
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;