// src/components/Layout/MainLayout.tsx
import React, { useState } from 'react';
import { Box, AppBar, Toolbar, Typography, IconButton, Menu, MenuItem } from '@mui/material';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import AccountCircle from '@mui/icons-material/AccountCircle';
import { RootState } from '../../store';

interface MainLayoutProps {
    children: React.ReactNode;
}

const MainLayout = ({ children }: MainLayoutProps) => {
    const { token } = useSelector((state: RootState) => state.auth);
    const navigate = useNavigate();
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

    // === Получаем имя пользователя из JWT ===
    const userName = token
        ? (() => {
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                return payload.sub || payload.username || 'Пользователь';
            } catch {
                return 'Пользователь';
            }
        })()
        : null;

    const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        handleClose();
        navigate('/auth');
    };

    return (
        <Box>
            {/* === ХЕДЕР === */}
            <AppBar position="static" sx={{ bgcolor: '#333' }}>
                <Toolbar>
                    <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: 600 }}>
                        Cookbook
                    </Typography>

                    {token && userName && (
                        <div>
                            <IconButton size="large" onClick={handleMenu} color="inherit">
                                <AccountCircle sx={{ fontSize: 32 }} />
                            </IconButton>

                            <Menu
                                anchorEl={anchorEl}
                                open={Boolean(anchorEl)}
                                onClose={handleClose}
                                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                                PaperProps={{
                                    sx: { mt: 1, boxShadow: '0 4px 12px rgba(0,0,0,0.1)' },
                                }}
                            >
                                <MenuItem disabled sx={{ fontWeight: 600, color: '#000', opacity: 1 }}>
                                    {userName}
                                </MenuItem>
                                <MenuItem
                                    onClick={handleLogout}
                                    sx={{
                                        color: '#d32f2f',
                                        fontWeight: 500,
                                        '&:hover': { bgcolor: '#ffebee' },
                                    }}
                                >
                                    Выйти
                                </MenuItem>
                            </Menu>
                        </div>
                    )}
                </Toolbar>
            </AppBar>

            {/* === КОНТЕНТ === */}
            <Box component="main" sx={{ p: 3 }}>
                {children}
            </Box>
        </Box>
    );
};

export default MainLayout;