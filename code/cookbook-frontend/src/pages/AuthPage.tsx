import { useState } from 'react';
import { Tabs, Tab, Box, Typography } from '@mui/material';
import Login from '../components/Auth/Login';
import Register from '../components/Auth/Register';

const AuthPage = () => {
    const [tab, setTab] = useState(0);

    return (
        <Box
            sx={{
                minHeight: '100vh',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                background: 'linear-gradient(135deg, #fff3e0, #ffffff)',
                padding: 2,
            }}
        >
            <Box
                sx={{
                    maxWidth: 400,
                    width: '100%',
                    backgroundColor: 'white',
                    padding: 4,
                    borderRadius: 2,
                    boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
                }}
            >
                <Typography variant="h5" align="center" gutterBottom sx={{ fontWeight: 600 }}>
                    Cookbook
                </Typography>
                <Tabs value={tab} onChange={(_, newValue) => setTab(newValue)} centered sx={{ mb: 3 }}>
                    <Tab label="Входааа" /> {/* Исправлено с "Входll" на "Вход" */}
                    <Tab label="Регистрация" />
                </Tabs>
                {tab === 0 ? <Login /> : <Register />}
            </Box>
        </Box>
    );
};

export default AuthPage;