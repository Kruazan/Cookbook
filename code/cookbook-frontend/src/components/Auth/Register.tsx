import { useForm, SubmitHandler } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { TextField, Button, Box, Typography } from '@mui/material';
import { useAppDispatch } from '../../store';
import { register as registerAction } from '../../store/authSlice';
import { useNavigate } from 'react-router-dom';
import { useSnackbar } from 'notistack';

const schema = yup.object({
    username: yup.string().required('Имя пользователя обязательно').default(''),
    email: yup.string().email('Неверный email').required('Email обязателен').default(''),
    password: yup.string().min(6, 'Минимум 6 символов').required('Пароль обязателен').default(''),
});

type FormData = {
    username: string;
    email: string;
    password: string;
};

const Register = () => {
    const dispatch = useAppDispatch();
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
            email: '',
            password: '',
        },
    });

    const onSubmit: SubmitHandler<FormData> = async (data) => {
        try {
            await dispatch(registerAction(data)).unwrap();
            navigate('/home');
            enqueueSnackbar('Регистрация успешна!', { variant: 'success' });
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : 'Ошибка регистрации';
            console.error('Register error:', errorMessage);
            enqueueSnackbar(errorMessage, { variant: 'error' });
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ maxWidth: 400, mx: 'auto', mt: 4 }}>
            <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Регистрация
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
                label="Email"
                type="email"
                fullWidth
                margin="normal"
                {...register('email')}
                error={!!errors.email}
                helperText={errors.email?.message}
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
                Зарегистрироваться
            </Button>
        </Box>
    );
};

export default Register;