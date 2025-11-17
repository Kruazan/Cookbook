import { useForm, SubmitHandler } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { TextField, Button, Box, Typography } from '@mui/material';
import { useAppDispatch } from '../../store'; // Добавляем диспатч
import { login } from '../../store/authSlice';
import { useNavigate } from 'react-router-dom';
import { useSnackbar } from 'notistack';

const schema = yup.object({
    username: yup.string().required('Имя пользователя обязательно').default(''),
    password: yup.string().required('Пароль обязателен').default(''),
});

type FormData = {
    username: string;
    password: string;
};

const Login = () => {
    const dispatch = useAppDispatch(); // Добавляем диспатч
    const navigate = useNavigate();
    const { enqueueSnackbar } = useSnackbar();
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<FormData>({
        resolver: yupResolver<FormData, any, FormData>(schema),
        defaultValues: {
            username: '',
            password: '',
        },
    });

    const onSubmit: SubmitHandler<FormData> = async (data) => {
        try {
            await dispatch(login(data)).unwrap();
            navigate('/home');
            enqueueSnackbar('Вход выполнен успешно!', { variant: 'success' });
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : 'Ошибка входа';
            console.error('Login error:', errorMessage);
            enqueueSnackbar(errorMessage, { variant: 'error' });
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ maxWidth: 400, mx: 'auto', mt: 4 }}>
            <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Вход в систему
            </Typography>
            <TextField
                label="Имя пользователя"
                fullWidth
                margin="normal"
                {...register('username')}
                error={!!errors.username}
                helperText={errors.username?.message}
            />
            <TextField
                label="Пароль"
                type="password"
                fullWidth
                margin="normal"
                {...register('password')}
                error={!!errors.password}
                helperText={errors.password?.message}
            />
            <Button type="submit" variant="contained" fullWidth sx={{ mt: 3, py: 1.5 }}>
                Войти
            </Button>
        </Box>
    );
};

export default Login;