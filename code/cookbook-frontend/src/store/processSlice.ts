// src/store/processSlice.ts
import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import api from '../services/api';

// Определяем типы
export interface Process {
    id: number;
    name: string;
    userId: number;
}

export interface ProcessCreateDTO {
    name: string;
}

interface ProcessState {
    items: Process[];
    loading: boolean;  // ← Правильно
    error: string | null;
}

const initialState: ProcessState = {
    items: [],
    loading: false,   // ← Теперь ок
    error: null,
};

// Thunks
export const fetchProcesses = createAsyncThunk('processes/fetchProcesses', async () => {
    const response = await api.get<Process[]>('/processes');
    return response.data;
});

export const createProcess = createAsyncThunk(
    'processes/createProcess',
    async (dto: ProcessCreateDTO) => {
        const response = await api.post<Process>('/processes', dto);
        return response.data;
    }
);

const processSlice = createSlice({
    name: 'processes',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            // fetchProcesses
            .addCase(fetchProcesses.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchProcesses.fulfilled, (state, action: PayloadAction<Process[]>) => {
                state.items = action.payload;
                state.loading = false;
            })
            .addCase(fetchProcesses.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch processes';
            })

            // createProcess
            .addCase(createProcess.pending, (state) => {
                state.error = null;
            })
            .addCase(createProcess.fulfilled, (state, action: PayloadAction<Process>) => {
                state.items.push(action.payload);
            })
            .addCase(createProcess.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to create process';
            });
    },
});

export default processSlice.reducer;