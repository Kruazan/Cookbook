// src/pages/CookRecipePage.tsx
import { useEffect, useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    Box,
    Typography,
    Button,
    Chip,
    CircularProgress,
} from '@mui/material';
import MainLayout from '../components/Layout/MainLayout';
import api from '../services/api';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import PauseIcon from '@mui/icons-material/Pause';
import StopIcon from '@mui/icons-material/Stop';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import AccessTimeIcon from '@mui/icons-material/AccessTime';

interface StepIngredient {
    ingredientId: number;
    ingredientName: string;
    quantity: number;
}

interface StepData {
    id: number;
    processId: number;
    processName: string;
    duration: number; // ← В МИНУТАХ С БЭКА
    stepIngredients: StepIngredient[];
}

interface RecipeData {
    id: number;
    name: string;
    cookingTime: number; // ← В МИНУТАХ С БЭКА
    weight: number;
    calories: number;
    steps: StepData[];
}

const CookRecipePage = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [recipe, setRecipe] = useState<RecipeData | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    const [globalTime, setGlobalTime] = useState(0);
    const [isRunning, setIsRunning] = useState(false);
    const [currentStepIndex, setCurrentStepIndex] = useState(0);
    const [stepTimeLeft, setStepTimeLeft] = useState(0);

    const globalIntervalRef = useRef<NodeJS.Timeout | null>(null);
    const stepIntervalRef = useRef<NodeJS.Timeout | null>(null);

    // === МИНУТЫ → СЕКУНДЫ ===
    const minutesToSeconds = (minutes: number) => minutes * 60;

    useEffect(() => {
        if (!id) {
            setError(true);
            return;
        }

        const loadRecipe = async () => {
            try {
                setLoading(true);
                const { data } = await api.get<RecipeData>(`/recipes/${id}`);
                if (!data?.steps?.length) throw new Error('No steps');

                // ПРЕОБРАЗУЕМ МИНУТЫ В СЕКУНДЫ
                const convertedData = {
                    ...data,
                    cookingTime: minutesToSeconds(data.cookingTime),
                    steps: data.steps.map(step => ({
                        ...step,
                        duration: minutesToSeconds(step.duration),
                    })),
                };

                setRecipe(convertedData);
                setStepTimeLeft(convertedData.steps[0].duration);
            } catch {
                setError(true);
            } finally {
                setLoading(false);
            }
        };
        loadRecipe();
    }, [id]);

    // Глобальный таймер (в секундах)
    useEffect(() => {
        if (isRunning) {
            globalIntervalRef.current = setInterval(() => {
                setGlobalTime(prev => prev + 1);
            }, 1000);
        } else {
            if (globalIntervalRef.current) clearInterval(globalIntervalRef.current);
        }
        return () => {
            if (globalIntervalRef.current) clearInterval(globalIntervalRef.current);
        };
    }, [isRunning]);

    // Таймер шага
    useEffect(() => {
        if (!recipe || !isRunning || currentStepIndex >= recipe.steps.length) return;

        if (stepTimeLeft <= 0) {
            if (currentStepIndex < recipe.steps.length - 1) {
                setCurrentStepIndex(prev => prev + 1);
                setStepTimeLeft(recipe.steps[currentStepIndex + 1].duration);
            } else {
                setIsRunning(false);
            }
            return;
        }

        stepIntervalRef.current = setInterval(() => {
            setStepTimeLeft(prev => prev - 1);
        }, 1000);

        return () => {
            if (stepIntervalRef.current) clearInterval(stepIntervalRef.current);
        };
    }, [isRunning, stepTimeLeft, currentStepIndex, recipe]);

    const handleStartPause = () => setIsRunning(prev => !prev);

    const handleStop = () => {
        setIsRunning(false);
        setGlobalTime(0);
        setCurrentStepIndex(0);
        setStepTimeLeft(recipe?.steps[0]?.duration || 0);
    };

    const handleFinish = () => {
        if (window.confirm('Вы уверены, что хотите завершить приготовление?')) {
            navigate('/');
        }
    };

    // ФОРМАТ: MM:SS
    const formatTime = (seconds: number) => {
        const mins = Math.floor(seconds / 60).toString().padStart(2, '0');
        const secs = (seconds % 60).toString().padStart(2, '0');
        return `${mins}:${secs}`;
    };

    if (loading) {
        return (
            <MainLayout>
                <Box sx={{ p: 3, textAlign: 'center' }}>
                    <CircularProgress sx={{ color: '#666' }} />
                    <Typography sx={{ mt: 2, color: '#666' }}>Загрузка...</Typography>
                </Box>
            </MainLayout>
        );
    }

    if (error || !recipe) {
        return (
            <MainLayout>
                <Box sx={{ p: 3, textAlign: 'center' }}>
                    <Typography color="#000" sx={{ fontWeight: 600 }}>Рецепт не найден</Typography>
                    <Button onClick={() => navigate('/')} sx={{ mt: 2, color: '#000', borderColor: '#ccc' }} variant="outlined">
                        На главную
                    </Button>
                </Box>
            </MainLayout>
        );
    }

    return (
        <MainLayout>
            <Box sx={{ p: 3, color: '#000' }}>
                <Typography variant="h5" sx={{ mb: 3, fontWeight: 700, textAlign: 'center' }}>
                    Приготовление: {recipe.name}
                </Typography>

                {/* ИНФО — cookingTime в минутах, как в JSON */}
                <Box sx={{ mb: 4, display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
                    <Typography sx={{ fontWeight: 600, color: '#000' }}>Вес: {recipe.weight} г</Typography>
                    <Typography sx={{ fontWeight: 600, color: '#000' }}>Калорийность: {recipe.calories} ккал</Typography>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <AccessTimeIcon sx={{ fontSize: 20, color: '#666' }} />
                        <Typography sx={{ fontWeight: 600, color: '#000' }}>
                            {Math.floor(recipe.cookingTime / 60)} мин
                        </Typography>
                    </Box>
                </Box>

                {/* ГЛОБАЛЬНЫЙ ТАЙМЕР */}
                <Box sx={{ mb: 4, textAlign: 'center' }}>
                    <Typography variant="h3" sx={{ fontWeight: 800, color: '#000', letterSpacing: 2 }}>
                        {formatTime(globalTime)}
                    </Typography>
                    <Box sx={{ mt: 2, display: 'flex', justifyContent: 'center', gap: 2 }}>
                        <Button
                            variant="contained"
                            startIcon={isRunning ? <PauseIcon /> : <PlayArrowIcon />}
                            onClick={handleStartPause}
                            sx={{ bgcolor: '#000', color: '#fff', '&:hover': { bgcolor: '#333' } }}
                        >
                            {isRunning ? 'Пауза' : 'Старт'}
                        </Button>
                        <Button
                            variant="outlined"
                            startIcon={<StopIcon />}
                            onClick={handleStop}
                            sx={{ color: '#000', borderColor: '#000' }}
                        >
                            Стоп
                        </Button>
                        <Button
                            variant="contained"
                            startIcon={<CheckCircleIcon />}
                            onClick={handleFinish}
                            sx={{ bgcolor: '#666', color: '#fff', '&:hover': { bgcolor: '#999' } }}
                        >
                            Закончить
                        </Button>
                    </Box>
                </Box>

                {/* ШАГИ */}
                <Box sx={{ display: 'flex', gap: 3 }}>
                    <Box
                        sx={{
                            width: '55%',
                            backgroundColor: '#f8f8f8',
                            borderRadius: 2,
                            p: 2,
                            border: '2px dashed #ccc',
                            height: 600,
                            overflowY: 'auto',
                        }}
                    >
                        <Typography variant="h6" sx={{ mb: 2, fontWeight: 700, color: '#000' }}>
                            Шаги приготовления
                        </Typography>

                        <Box sx={{ pr: 1 }}>
                            {recipe.steps.map((step, index) => {
                                const isActive = index === currentStepIndex;
                                const isDone = index < currentStepIndex;
                                const timeLeft = isActive ? stepTimeLeft : step.duration;

                                return (
                                    <Box
                                        key={step.id}
                                        sx={{
                                            mb: 2,
                                            p: 2,
                                            backgroundColor: isDone ? '#eee' : isActive ? '#fff' : '#fafafa',
                                            borderRadius: 2,
                                            boxShadow: '0 2px 6px rgba(0,0,0,0.1)',
                                            border: `1px solid ${isActive ? '#000' : '#ddd'}`,
                                            opacity: isDone ? 0.6 : 1,
                                        }}
                                    >
                                        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                                <Box
                                                    sx={{
                                                        px: 2,
                                                        py: 0.5,
                                                        backgroundColor: '#000',
                                                        color: '#fff',
                                                        borderRadius: 1,
                                                        fontWeight: 700,
                                                        fontSize: '0.9rem',
                                                    }}
                                                >
                                                    {step.processName}
                                                </Box>
                                                {isDone && <CheckCircleIcon sx={{ color: '#000', fontSize: 20 }} />}
                                            </Box>

                                            <Typography
                                                variant="h6"
                                                sx={{ fontWeight: 800, color: isActive ? '#000' : '#333' }}
                                            >
                                                {formatTime(timeLeft)}
                                            </Typography>
                                        </Box>

                                        {step.stepIngredients.length > 0 && (
                                            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                                                {step.stepIngredients.map((item, idx) => (
                                                    <Chip
                                                        key={idx}
                                                        label={`${item.ingredientName}: ${item.quantity} г`}
                                                        size="small"
                                                        sx={{
                                                            backgroundColor: '#ddd',
                                                            color: '#000',
                                                            fontWeight: 600,
                                                            fontSize: '0.8rem',
                                                        }}
                                                    />
                                                ))}
                                            </Box>
                                        )}
                                    </Box>
                                );
                            })}
                        </Box>
                    </Box>

                    <Box sx={{ width: '45%' }} />
                </Box>
            </Box>
        </MainLayout>
    );
};

export default CookRecipePage;