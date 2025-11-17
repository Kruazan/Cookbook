import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState, useAppDispatch } from '../store';
import { fetchRecipes } from '../store/recipeSlice';
import { fetchIngredients } from '../store/ingredientSlice';
import { fetchProcesses, createProcess } from '../store/processSlice'; // ← ИМПОРТ
import RecipeList from '../components/Recipe/RecipeList';
import MainLayout from '../components/Layout/MainLayout';
import { useNavigate } from 'react-router-dom';
import { SelectChangeEvent } from '@mui/material/Select';
import {
    Box,
    Typography,
    TextField,
    Dialog,
    DialogTitle,
    DialogContent,
    Select,
    MenuItem,
    Button,
    List,
    ListItem,
    ListItemText,
    Chip,
} from '@mui/material';
import { Ingredient, IngredientType, Process } from '../types'; // ← Process добавлен в types

const HomePage = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const { token } = useSelector((state: RootState) => state.auth);
    const recipes = useSelector((state: RootState) => state.recipes.items);
    const ingredients = useSelector((state: RootState) => state.ingredients.items);
    const processes = useSelector((state: RootState) => state.processes.items); // ← ПОЛУЧАЕМ из стора

    const [searchTerm, setSearchTerm] = useState('');
    const [ingredientOpen, setIngredientOpen] = useState(false);
    const [processOpen, setProcessOpen] = useState(false);
    const [ingredientName, setIngredientName] = useState('');
    const [ingredientType, setIngredientType] = useState<IngredientType>(IngredientType.VEGETABLE);
    const [processName, setProcessName] = useState('');
    const handleRecipeDelete = (id: number) => {
        dispatch(fetchRecipes()); // обновляем список после удаления
    };

    useEffect(() => {
        if (!token) {
            navigate('/auth');
        } else {
            dispatch(fetchRecipes());
            dispatch(fetchIngredients());
            dispatch(fetchProcesses()); // ← ЗАГРУЖАЕМ операции
        }
    }, [token, dispatch, navigate]);

    const filteredRecipes = recipes.filter((recipe) =>
        recipe.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const handleIngredientOpen = () => setIngredientOpen(true);
    const handleIngredientClose = () => setIngredientOpen(false);
    const handleProcessOpen = () => setProcessOpen(true);
    const handleProcessClose = () => setProcessOpen(false);

    /* ------------------- ИНГРЕДИЕНТ ------------------- */
    const handleAddIngredient = async () => {
        if (!ingredientName.trim()) {
            alert('Пожалуйста, заполните название ингредиента');
            return;
        }

        const ingredientData = { name: ingredientName, type: ingredientType };

        try {
            const response = await fetch('http://localhost:8080/api/ingredients', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(ingredientData),
            });

            if (!response.ok) throw new Error(`Ошибка: ${response.status}`);

            dispatch(fetchIngredients()); // ← обновляем список
            setIngredientName('');
            setIngredientType(IngredientType.VEGETABLE);
            handleIngredientClose();
        } catch (error) {
            console.error(error);
            alert('Не удалось добавить ингредиент: ' + (error as Error).message);
        }
    };

    /* ------------------- ОПЕРАЦИЯ ------------------- */
    const handleAddProcess = async () => {
        if (!processName.trim()) {
            alert('Пожалуйста, заполните название операции');
            return;
        }

        try {
            // ← используем thunk вместо прямого fetch
            await dispatch(createProcess({ name: processName })).unwrap();

            setProcessName('');
            handleProcessClose();
        } catch (error: any) {
            console.error(error);
            alert('Не удалось добавить операцию: ' + (error.message ?? 'Неизвестная ошибка'));
        }
    };

    return (
        <MainLayout>
            <Box sx={{ p: 3 }}>
                <Typography variant="h5" gutterBottom sx={{ fontWeight: 600, mb: 3, color: '#333333' }}>
                    Мои рецепты
                </Typography>

                <TextField
                    label="Поиск по названию"
                    variant="outlined"
                    fullWidth
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    sx={{ mb: 3, width: '50%', minWidth: 400, '& .MuiOutlinedInput-root': { backgroundColor: '#FFFFFF' } }}
                />

                <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Box sx={{ width: '48%', minWidth: 400 }}>
                        <RecipeList recipes={filteredRecipes} onDelete={handleRecipeDelete} />
                    </Box>

                    <Box sx={{ width: '48%', minWidth: 400, display: 'flex', flexDirection: 'column', gap: 2 }}>
                        {/* Кнопки */}
                        <Box
                            onClick={handleIngredientOpen}
                            sx={{
                                backgroundColor: '#FFFFFF',
                                borderRadius: 8,
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                                p: 1.5,
                                textAlign: 'center',
                                cursor: 'pointer',
                                transition: 'transform 0.2s',
                                '&:hover': { transform: 'scale(1.02)', backgroundColor: '#E0E0E0' },
                            }}
                        >
                            <Typography variant="body1" sx={{ fontWeight: 500, color: '#333333' }}>
                                Добавить ингредиент
                            </Typography>
                        </Box>

                        <Box
                            onClick={handleProcessOpen}
                            sx={{
                                backgroundColor: '#FFFFFF',
                                borderRadius: 8,
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                                p: 1.5,
                                textAlign: 'center',
                                cursor: 'pointer',
                                transition: 'transform 0.2s',
                                '&:hover': { transform: 'scale(1.02)', backgroundColor: '#E0E0E0' },
                            }}
                        >
                            <Typography variant="body1" sx={{ fontWeight: 500, color: '#333333' }}>
                                Добавить операцию приготовления
                            </Typography>
                        </Box>

                        <Box
                            onClick={() => navigate('/create-recipe')}
                            sx={{
                                backgroundColor: '#FFFFFF',
                                borderRadius: 8,
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                                p: 1.5,
                                textAlign: 'center',
                                cursor: 'pointer',
                                transition: 'transform 0.2s',
                                '&:hover': { transform: 'scale(1.02)', backgroundColor: '#E0E0E0' },
                            }}
                        >
                            <Typography variant="body1" sx={{ fontWeight: 500, color: '#333333' }}>
                                Добавить рецепт
                            </Typography>
                        </Box>
                    </Box>
                </Box>

                {/* ================== ДИАЛОГ ИНГРЕДИЕНТА ================== */}
                <Dialog
                    open={ingredientOpen}
                    onClose={handleIngredientClose}
                    maxWidth={false}
                    PaperProps={{ style: { width: '33vw', height: '90vh' } }}
                >
                    <DialogTitle sx={{ backgroundColor: '#333333', color: '#FFFFFF', p: 2 }}>
                        Добавить новый ингредиент
                    </DialogTitle>
                    <DialogContent sx={{ p: 3, backgroundColor: '#FFFFFF', display: 'flex', flexDirection: 'column', height: '100%' }}>
                        <Box sx={{ flex: 1, mt: 2, mb: 2 }}>
                            <TextField
                                label="Название ингредиента"
                                variant="outlined"
                                fullWidth
                                value={ingredientName}
                                onChange={(e) => setIngredientName(e.target.value)}
                                sx={{ mb: 3, '& .MuiOutlinedInput-root': { backgroundColor: '#F5F5F5' } }}
                                InputLabelProps={{ style: { color: '#333333' } }}
                                InputProps={{ style: { color: '#333333' } }}
                            />

                            <Select
                                value={ingredientType}
                                onChange={(event: SelectChangeEvent<IngredientType>) => {
                                    setIngredientType(event.target.value as IngredientType);
                                }}
                                fullWidth
                                displayEmpty
                                sx={{ mb: 3, '& .MuiOutlinedInput-root': { backgroundColor: '#F5F5F5' } }}
                                renderValue={(value) => value || 'Выберите тип продукта'}
                            >
                                {Object.values(IngredientType).map((type) => (
                                    <MenuItem key={type} value={type} sx={{ color: '#333333' }}>
                                        {type}
                                    </MenuItem>
                                ))}
                            </Select>
                        </Box>

                        <Box sx={{ flex: 1, overflowY: 'auto', borderTop: '1px solid #E0E0E0', p: 2 }}>
                            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1.5 }}>
                                {ingredients.length > 0 ? (
                                    ingredients.map((ingredient) => (
                                        <Chip
                                            key={ingredient.id}
                                            label={ingredient.name}
                                            variant="outlined"
                                            sx={{
                                                borderRadius: 16,
                                                fontSize: '1.05rem',
                                                fontWeight: 600,
                                                color: '#000000',
                                                borderColor: '#cccccc',
                                                backgroundColor: '#ffffff',
                                                height: 38,
                                                '& .MuiChip-label': { padding: '0 16px' },
                                            }}
                                        />
                                    ))
                                ) : (
                                    <Typography variant="body2" color="text.secondary" sx={{ p: 1 }}>
                                        Нет ингредиентов
                                    </Typography>
                                )}
                            </Box>
                        </Box>

                        <Button
                            variant="contained"
                            fullWidth
                            onClick={handleAddIngredient}
                            sx={{ py: 1.5, mt: 2, backgroundColor: '#333333', color: '#FFFFFF', '&:hover': { backgroundColor: '#666666' } }}
                        >
                            Добавить
                        </Button>
                    </DialogContent>
                </Dialog>

                {/* ================== ДИАЛОГ ОПЕРАЦИИ ================== */}
                <Dialog
                    open={processOpen}
                    onClose={handleProcessClose}
                    maxWidth={false}
                    PaperProps={{ style: { width: '33vw', height: '90vh' } }}
                >
                    <DialogTitle sx={{ backgroundColor: '#333333', color: '#FFFFFF', p: 2 }}>
                        Добавить новую операцию
                    </DialogTitle>
                    <DialogContent sx={{ p: 3, backgroundColor: '#FFFFFF', display: 'flex', flexDirection: 'column', height: '100%' }}>
                        <Box sx={{ flex: 1, mt: 2, mb: 2 }}>
                            <TextField
                                label="Название операции"
                                variant="outlined"
                                fullWidth
                                value={processName}
                                onChange={(e) => setProcessName(e.target.value)}
                                sx={{ mb: 3, '& .MuiOutlinedInput-root': { backgroundColor: '#F5F5F5' } }}
                                InputLabelProps={{ style: { color: '#333333' } }}
                                InputProps={{ style: { color: '#333333' } }}
                            />
                        </Box>

                        {/* ← СПИСОК ОПЕРАЦИЙ (ЗАГЛУШКА УБРАНА) */}
                        <Box sx={{ flex: 1, overflowY: 'auto', borderTop: '1px solid #E0E0E0', p: 2 }}>
                            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1.5 }}>
                                {processes.length > 0 ? (
                                    processes.map((proc) => (
                                        <Chip
                                            key={proc.id}
                                            label={proc.name}
                                            variant="outlined"
                                            sx={{
                                                borderRadius: 16,
                                                fontSize: '1.05rem',
                                                fontWeight: 600,
                                                color: '#000000',
                                                borderColor: '#cccccc',
                                                backgroundColor: '#ffffff',
                                                height: 38,
                                                '& .MuiChip-label': { padding: '0 16px' },
                                            }}
                                        />
                                    ))
                                ) : (
                                    <Typography variant="body2" color="text.secondary" sx={{ p: 1 }}>
                                        Нет операций
                                    </Typography>
                                )}
                            </Box>
                        </Box>

                        <Button
                            variant="contained"
                            fullWidth
                            onClick={handleAddProcess}
                            sx={{ py: 1.5, mt: 2, backgroundColor: '#333333', color: '#FFFFFF', '&:hover': { backgroundColor: '#666666' } }}
                        >
                            Добавить
                        </Button>
                    </DialogContent>
                </Dialog>
            </Box>
        </MainLayout>
    );
};

export default HomePage;