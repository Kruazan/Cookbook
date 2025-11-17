import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        primary: {
            main: '#388E3C', // Тёмно-зелёный, аптечный тон
            light: '#66BB6A', // Светло-зелёный
            dark: '#2F7D32', // Ещё более тёмный зелёный
            contrastText: '#FFFFFF', // Белый текст для контраста
        },
        secondary: {
            main: '#81C784', // Средний зелёный для второстепенных элементов
        },
        background: {
            default: '#F5F5F5', // Светло-серый фон для контраста с зелёным
            paper: '#FFFFFF', // Белый фон для карточек
        },
        text: {
            primary: '#212121', // Тёмный текст для читаемости
        },
    },
    typography: {
        fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", "Roboto", "Oxygen", "Ubuntu", "Cantarell", "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif',
    },
});

export default theme;