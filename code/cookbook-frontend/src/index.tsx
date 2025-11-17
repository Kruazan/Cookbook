// src/index.tsx
import React from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { store } from './store';
import App from './App';
import './index.css';
import { ThemeProvider } from '@mui/material/styles';
import theme from './theme';

// ОТКЛЮЧАЕМ StrictMode — DND-KIT НЕ ЛЮБИТ ЕГО
const root = createRoot(document.getElementById('root') as HTMLElement);

root.render(
    <Provider store={store}>
        <ThemeProvider theme={theme}>
            {/* УБРАЛИ <React.StrictMode> */}
            <App />
        </ThemeProvider>
    </Provider>
);