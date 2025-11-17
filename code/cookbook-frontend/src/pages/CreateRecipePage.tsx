// src/pages/CreateRecipePage.tsx
import { useState, useMemo, useEffect } from 'react';
import {
    Box,
    Typography,
    TextField,
    IconButton,
    Button,
    Chip,
    InputAdornment,
    CircularProgress,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import MainLayout from '../components/Layout/MainLayout';
import DeleteIcon from '@mui/icons-material/Delete';
import SearchIcon from '@mui/icons-material/Search';
import api from '../services/api';

import { fetchProcesses } from '../store/processSlice';
import { fetchIngredients } from '../store/ingredientSlice';
import { AppDispatch, RootState } from '../store';

// === Типы ===
interface IngredientChip {
    id: number;
    name: string;
}
interface ProcessChip {
    id: number;
    name: string;
}
interface RecipeStep {
    id: string;
    process: ProcessChip;
    duration: string;
    ingredients: { ingredient: IngredientChip; quantity: string }[];
}

const CreateRecipePage = () => {
    const dispatch = useDispatch<AppDispatch>();
    const navigate = useNavigate();
    const [steps, setSteps] = useState<RecipeStep[]>([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [name, setName] = useState('');
    const [weight, setWeight] = useState('');
    const [calories, setCalories] = useState('');

    // === Redux данные ===
    const { items: processes, loading: procLoading } = useSelector((state: RootState) => state.processes);
    const { items: ingredients, loading: ingLoading } = useSelector((state: RootState) => state.ingredients);

    // === Загрузка данных ===
    useEffect(() => {
        dispatch(fetchProcesses());
        dispatch(fetchIngredients());
    }, [dispatch]);

    // === Фильтрация ингредиентов ===
    const filteredIngredients = useMemo(() => {
        if (!searchQuery.trim()) return ingredients;
        const query = searchQuery.toLowerCase();
        return ingredients.filter((ing) =>
            ing.name.toLowerCase().includes(query)
        );
    }, [ingredients, searchQuery]);

    // === Общее время ===
    const totalCookingTime = steps.reduce((sum, step) => {
        const duration = parseInt(step.duration) || 0;
        return sum + duration;
    }, 0);

    // === Добавление операции ===
    const addProcess = (process: ProcessChip) => {
        setSteps((prev) => [
            ...prev,
            {
                id: `step-${Date.now()}-${Math.random()}`,
                process,
                duration: '',
                ingredients: [],
            },
        ]);
    };

    // === Добавление ингредиента ===
    const addIngredientToStep = (stepId: string, ingredient: IngredientChip) => {
        setSteps((prev) =>
            prev.map((s) =>
                s.id === stepId
                    ? {
                        ...s,
                        ingredients: [
                            ...s.ingredients,
                            { ingredient, quantity: '' },
                        ],
                    }
                    : s
            )
        );
    };

    // === Перемещение шага ===
    const moveStep = (index: number, direction: 'up' | 'down') => {
        if (
            (direction === 'up' && index === 0) ||
            (direction === 'down' && index === steps.length - 1)
        )
            return;

        const newSteps = [...steps];
        const [moved] = newSteps.splice(index, 1);
        const newIndex = direction === 'up' ? index - 1 : index + 1;
        newSteps.splice(newIndex, 0, moved);
        setSteps(newSteps);
    };

    // === СОХРАНЕНИЕ РЕЦЕПТА ===
    const handleSave = async () => {
        if (!name.trim() || !weight || !calories || steps.length === 0) {
            alert('Заполните все поля и добавьте хотя бы один шаг');
            return;
        }

        const payload = {
            name: name.trim(),
            cookingTime: totalCookingTime,
            weight: parseFloat(weight),
            calories: parseInt(calories),
            steps: steps.map((step) => ({
                processId: step.process.id,
                duration: parseInt(step.duration) || 0,
                additionalNote: '',
                stepIngredients: step.ingredients
                    .filter((i) => i.quantity)
                    .map((i) => ({
                        ingredientId: i.ingredient.id,
                        quantity: parseFloat(i.quantity),
                    })),
            })),
        };

        try {
            // ← ВАЖНО: ТОЛЬКО /recipes
            await api.post('/recipes', payload);
            alert('Рецепт успешно сохранён!');
            navigate('/');
        } catch (err: any) {
            console.error('Ошибка сохранения:', err);
            if (err.response?.status === 401) {
                alert('Сессия истекла. Перенаправляю на вход...');
                // Интерцептор уже сделает редирект
            } else {
                alert('Ошибка: ' + (err.response?.data?.message || err.message));
            }
        }
    };

    return (
        <MainLayout>
            <Box sx={{ p: 3, color: '#000' }}>
                <Typography variant="h5" sx={{ mb: 3, fontWeight: 600, textAlign: 'center', color: '#000' }}>
                    Создание рецепта
                </Typography>

                {/* ВВОД ГРАММОВ И ККАЛ */}
                <Box sx={{ mb: 4, display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
                    <TextField
                        label="Название"
                        size="small"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        sx={{ width: 200, '& .MuiInputBase-input': { color: '#000' } }}
                    />
                    <TextField
                        label="Вес (г)"
                        size="small"
                        type="number"
                        value={weight}
                        onChange={(e) => setWeight(e.target.value)}
                        sx={{ width: 120, '& .MuiInputBase-input': { color: '#000' } }}
                    />
                    <TextField
                        label="Калории"
                        size="small"
                        type="number"
                        value={calories}
                        onChange={(e) => setCalories(e.target.value)}
                        sx={{ width: 120, '& .MuiInputBase-input': { color: '#000' } }}
                    />
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Typography variant="body2" sx={{ color: '#666' }}>
                            Время:
                        </Typography>
                        <Typography variant="body1" sx={{ fontWeight: 600 }}>
                            {totalCookingTime} мин
                        </Typography>
                    </Box>
                </Box>

                <Box sx={{ display: 'flex', gap: 3, mb: 4 }}>
                    {/* ЛЕВАЯ ПАНЕЛЬ — ШАГИ */}
                    <Box
                        sx={{
                            width: '55%',
                            backgroundColor: '#f5f5f5',
                            borderRadius: 3,
                            p: 2,
                            border: '3px dashed #ccc',
                            height: 600,
                            overflowY: 'auto',
                            display: 'flex',
                            flexDirection: 'column',
                        }}
                    >
                        <Typography variant="h6" sx={{ mb: 2, fontWeight: 600, flexShrink: 0, color: '#000' }}>
                            Шаги рецепта
                        </Typography>

                        <Box sx={{ flex: 1, pr: 1 }}>
                            {steps.length === 0 ? (
                                <Typography color="#999" sx={{ textAlign: 'center', mt: 8 }}>
                                    {procLoading ? 'Загрузка...' : 'Выберите операцию справа'}
                                </Typography>
                            ) : (
                                steps.map((step, index) => (
                                    <Box
                                        key={step.id}
                                        sx={{
                                            mb: 2,
                                            p: 2,
                                            backgroundColor: '#fff',
                                            borderRadius: 3,
                                            boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
                                            border: '1px solid #eee',
                                            display: 'flex',
                                            alignItems: 'flex-start',
                                            gap: 2,
                                        }}
                                    >
                                        <Box sx={{ flex: 1 }}>
                                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                                                <Box
                                                    sx={{
                                                        px: 2,
                                                        py: 0.5,
                                                        backgroundColor: '#333',
                                                        color: '#fff',
                                                        borderRadius: 2,
                                                        fontWeight: 600,
                                                        fontSize: '0.95rem',
                                                    }}
                                                >
                                                    {step.process.name}
                                                </Box>

                                                <IconButton size="small" sx={{ color: '#000' }} onClick={() => moveStep(index, 'up')} disabled={index === 0}>
                                                    Up
                                                </IconButton>
                                                <IconButton size="small" sx={{ color: '#000' }} onClick={() => moveStep(index, 'down')} disabled={index === steps.length - 1}>
                                                    Down
                                                </IconButton>

                                                <IconButton size="small" sx={{ color: '#000' }} onClick={() => setSteps((prev) => prev.filter((s) => s.id !== step.id))}>
                                                    <DeleteIcon />
                                                </IconButton>
                                            </Box>

                                            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mb: 1 }}>
                                                {step.ingredients.map((item, idx) => (
                                                    <Box key={idx} sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                                                        <Chip
                                                            label={item.ingredient.name}
                                                            size="small"
                                                            sx={{ backgroundColor: '#666', color: '#fff' }}
                                                        />
                                                        <TextField
                                                            size="small"
                                                            value={item.quantity}
                                                            placeholder="100"
                                                            onChange={(e) =>
                                                                setSteps((prev) =>
                                                                    prev.map((s) =>
                                                                        s.id === step.id
                                                                            ? {
                                                                                ...s,
                                                                                ingredients: s.ingredients.map((ing, i) =>
                                                                                    i === idx ? { ...ing, quantity: e.target.value } : ing
                                                                                ),
                                                                            }
                                                                            : s
                                                                    )
                                                                )
                                                            }
                                                            sx={{ width: 70, '& .MuiInputBase-input': { color: '#000' } }}
                                                        />
                                                        <IconButton
                                                            size="small"
                                                            sx={{ color: '#000' }}
                                                            onClick={() =>
                                                                setSteps((prev) =>
                                                                    prev.map((s) =>
                                                                        s.id === step.id
                                                                            ? {
                                                                                ...s,
                                                                                ingredients: s.ingredients.filter((_, i) => i !== idx),
                                                                            }
                                                                            : s
                                                                    )
                                                                )
                                                            }
                                                        >
                                                            <DeleteIcon fontSize="small" />
                                                        </IconButton>
                                                    </Box>
                                                ))}
                                            </Box>

                                            <Box sx={{ mt: 1 }}>
                                                <Typography variant="caption" sx={{ color: '#666', mb: 0.5 }}>
                                                    Добавить ингредиент:
                                                </Typography>
                                                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                                                    {filteredIngredients.map((ing) => (
                                                        <Chip
                                                            key={ing.id}
                                                            label={ing.name}
                                                            size="small"
                                                            color="default"
                                                            variant="outlined"
                                                            clickable
                                                            onClick={() => addIngredientToStep(step.id, ing)}
                                                            sx={{ cursor: 'pointer', borderColor: '#ccc', color: '#000' }}
                                                        />
                                                    ))}
                                                </Box>
                                            </Box>
                                        </Box>

                                        <Box
                                            sx={{
                                                display: 'flex',
                                                flexDirection: 'column',
                                                alignItems: 'flex-end',
                                                justifyContent: 'flex-start',
                                                minWidth: 120,
                                            }}
                                        >
                                            <Typography variant="caption" sx={{ color: '#666', mb: 0.5 }}>
                                                Время (мин)
                                            </Typography>
                                            <TextField
                                                size="small"
                                                type="number"
                                                placeholder="10"
                                                value={step.duration}
                                                onChange={(e) =>
                                                    setSteps((prev) =>
                                                        prev.map((s) =>
                                                            s.id === step.id ? { ...s, duration: e.target.value } : s
                                                        )
                                                    )
                                                }
                                                sx={{
                                                    width: 80,
                                                    '& .MuiInputBase-input': { fontSize: '0.9rem', color: '#000' },
                                                    '& .MuiOutlinedInput-root': { borderColor: '#ccc' },
                                                }}
                                            />
                                        </Box>
                                    </Box>
                                ))
                            )}
                        </Box>
                    </Box>

                    {/* ПРАВАЯ ПАНЕЛЬ */}
                    <Box sx={{ width: '45%' }}>
                        <Box sx={{ mb: 3 }}>
                            <Typography variant="h6" sx={{ mb: 1, fontWeight: 600, color: '#000' }}>
                                Ингредиенты
                            </Typography>
                            {ingLoading ? (
                                <Box sx={{ display: 'flex', justifyContent: 'center', p: 2 }}>
                                    <CircularProgress size={20} />
                                </Box>
                            ) : (
                                <>
                                    <TextField
                                        fullWidth
                                        size="small"
                                        placeholder="Поиск..."
                                        value={searchQuery}
                                        onChange={(e) => setSearchQuery(e.target.value)}
                                        InputProps={{
                                            startAdornment: (
                                                <InputAdornment position="start">
                                                    <SearchIcon sx={{ color: '#666' }} />
                                                </InputAdornment>
                                            ),
                                        }}
                                        sx={{
                                            mb: 1,
                                            '& .MuiOutlinedInput-root': { borderColor: '#ccc' },
                                            '& .MuiInputBase-input': { color: '#000' },
                                        }}
                                    />
                                    <Box sx={{ maxHeight: 200, overflowY: 'auto', p: 1, bgcolor: '#f9f9f9', borderRadius: 2, border: '1px solid #eee' }}>
                                        {filteredIngredients.length === 0 ? (
                                            <Typography color="#999" fontSize="0.9rem">
                                                Ничего не найдено
                                            </Typography>
                                        ) : (
                                            filteredIngredients.map((ing) => (
                                                <Chip
                                                    key={ing.id}
                                                    label={ing.name}
                                                    size="small"
                                                    sx={{ m: 0.5, backgroundColor: '#ddd', color: '#000' }}
                                                />
                                            ))
                                        )}
                                    </Box>
                                </>
                            )}
                        </Box>

                        <Box>
                            <Typography variant="h6" sx={{ mb: 1, fontWeight: 600, color: '#000' }}>
                                Операции
                            </Typography>
                            {procLoading ? (
                                <Box sx={{ display: 'flex', justifyContent: 'center', p: 2 }}>
                                    <CircularProgress size={20} />
                                </Box>
                            ) : processes.length === 0 ? (
                                <Typography color="#999" fontSize="0.9rem">
                                    Нет операций
                                </Typography>
                            ) : (
                                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                                    {processes.map((proc) => (
                                        <Button
                                            key={proc.id}
                                            variant="outlined"
                                            size="small"
                                            onClick={() => addProcess(proc)}
                                            sx={{
                                                textTransform: 'none',
                                                fontWeight: 600,
                                                color: '#000',
                                                borderColor: '#ccc',
                                                '&:hover': { borderColor: '#999', backgroundColor: '#f5f5f5' },
                                            }}
                                        >
                                            {proc.name}
                                        </Button>
                                    ))}
                                </Box>
                            )}
                        </Box>
                    </Box>
                </Box>

                {/* КНОПКИ */}
                <Box sx={{ p: 3, display: 'flex', justifyContent: 'space-between' }}>
                    <Button
                        variant="outlined"
                        onClick={() => navigate('/')}
                        sx={{ color: '#000', borderColor: '#ccc', '&:hover': { borderColor: '#999' } }}
                    >
                        Back
                    </Button>
                    <Button
                        variant="contained"
                        onClick={handleSave}
                        sx={{ backgroundColor: '#333', color: '#fff', '&:hover': { backgroundColor: '#000' } }}
                    >
                        Save
                    </Button>
                </Box>
            </Box>
        </MainLayout>
    );
};

export default CreateRecipePage;