// src/components/Recipe/RecipeList.tsx
import React, { useState } from 'react';
import {
    Card,
    CardContent,
    Typography,
    Box,
    IconButton,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { Recipe } from '../../types';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import api from '../../services/api';

interface RecipeListProps {
    recipes: Recipe[];
    onDelete?: (id: number) => void; // Опционально — обновить список
}

const RecipeList = ({ recipes, onDelete }: RecipeListProps) => {
    const navigate = useNavigate();
    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [deletingId, setDeletingId] = useState<number | null>(null);

    const handleDeleteClick = (id: number) => {
        setDeletingId(id);
        setDeleteDialogOpen(true);
    };

    const handleDeleteConfirm = async () => {
        if (!deletingId) return;

        try {
            await api.delete(`/recipes/${deletingId}`);
            if (onDelete) onDelete(deletingId);
            alert('Рецепт удалён!');
        } catch (err: any) {
            console.error(err);
            alert('Ошибка при удалении: ' + (err.response?.data?.message || err.message));
        } finally {
            setDeleteDialogOpen(false);
            setDeletingId(null);
        }
    };

    const handleDeleteCancel = () => {
        setDeleteDialogOpen(false);
        setDeletingId(null);
    };

    return (
        <Box>
            {recipes.map((recipe) => (
                <Card
                    key={recipe.id}
                    sx={{
                        mb: 2,
                        borderRadius: 8,
                        boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
                        transition: 'transform 0.2s',
                        '&:hover': {
                            transform: 'scale(1.02)',
                        },
                    }}
                >
                    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
                        {/* Название + время */}
                        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                            <Typography variant="h5" sx={{ flexGrow: 1, fontWeight: 600, fontSize: '1.5rem' }}>
                                {recipe.name}
                            </Typography>
                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                <AccessTimeIcon sx={{ mr: 0.5, fontSize: 20, color: '#666' }} />
                                <Typography variant="body1" color="text.secondary" sx={{ fontSize: '1rem' }}>
                                    {recipe.cookingTime} мин
                                </Typography>
                            </Box>
                        </Box>

                        {/* Вес и калории */}
                        <Typography variant="body1" color="text.secondary" sx={{ mb: 0.5, fontSize: '1rem' }}>
                            Вес: {recipe.weight} г
                        </Typography>
                        <Typography variant="body1" color="text.secondary" sx={{ fontSize: '1rem' }}>
                            Калорийность: {recipe.calories} ккал
                        </Typography>

                        {/* КНОПКИ: ЗАПУСК → РЕДАКТИРОВАТЬ → УДАЛИТЬ */}
                        <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2, gap: 1 }}>
                            {/* ЗАПУСК */}
                            <IconButton
                                onClick={() => navigate(`/cook-recipe/${recipe.id}`)}
                                sx={{
                                    color: '#000',
                                    '&:hover': { bgcolor: '#f5f5f5' },
                                }}
                            >
                                <PlayArrowIcon sx={{ fontSize: 28 }} />
                            </IconButton>

                            {/* РЕДАКТИРОВАТЬ */}
                            <IconButton
                                onClick={() => navigate(`/edit-recipe/${recipe.id}`)}
                                sx={{
                                    color: '#000',
                                    '&:hover': { bgcolor: '#f5f5f5' },
                                }}
                            >
                                <EditIcon sx={{ fontSize: 28 }} />
                            </IconButton>

                            {/* УДАЛИТЬ */}
                            <IconButton
                                onClick={() => handleDeleteClick(recipe.id)}
                                sx={{
                                    color: '#d32f2f',
                                    '&:hover': { bgcolor: '#ffebee' },
                                }}
                            >
                                <DeleteIcon sx={{ fontSize: 28 }} />
                            </IconButton>
                        </Box>
                    </CardContent>
                </Card>
            ))}

            {recipes.length === 0 && (
                <Typography variant="body1" align="center" sx={{ mt: 2, color: '#666' }}>
                    Нет рецептов для отображения
                </Typography>
            )}

            {/* ДИАЛОГ ПОДТВЕРЖДЕНИЯ УДАЛЕНИЯ */}
            <Dialog open={deleteDialogOpen} onClose={handleDeleteCancel}>
                <DialogTitle sx={{ color: '#d32f2f', fontWeight: 600 }}>
                    Удалить рецепт?
                </DialogTitle>
                <DialogContent>
                    <Typography>
                        Вы уверены, что хотите удалить рецепт "
                        <strong>
                            {recipes.find((r) => r.id === deletingId)?.name || ''}
                        </strong>
                        "? Это действие нельзя отменить.
                    </Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDeleteCancel} sx={{ color: '#666' }}>
                        Отмена
                    </Button>
                    <Button
                        onClick={handleDeleteConfirm}
                        variant="contained"
                        sx={{
                            backgroundColor: '#d32f2f',
                            '&:hover': { backgroundColor: '#b71c1c' },
                        }}
                    >
                        Удалить
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default RecipeList;