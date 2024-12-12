import { useMutation } from '@tanstack/react-query';
import { authApi } from '../api/auth';
import { LoginRequest } from '../types/auth';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

export const useLogin = () => {
    const navigate = useNavigate();

    return useMutation({
        mutationFn: (credentials: LoginRequest) => authApi.login(credentials),
        onSuccess: (data) => {
            localStorage.setItem('token', data.token);
            navigate('/', { replace: true });
        },
        onError: (error) => {
            console.error('Login failed:', error);
            localStorage.removeItem('token');
        }
    });
};

export const useAuth = () => {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    const isValidToken = (token: string | null): boolean => {
        if (!token) return false;
        try {
            const decoded = jwtDecode(token);
            const currentTime = Date.now() / 1000;
            return decoded.exp ? decoded.exp > currentTime : false;
        } catch {
            return false;
        }
    };

    const isAuthenticated = isValidToken(token);
    
    // Clear token if invalid
    if (!isAuthenticated && token) {
        localStorage.removeItem('token');
    }

    return {
        isAuthenticated,
        token,
        logout: () => {
            localStorage.removeItem('token');
            navigate('/login', { replace: true });
        },
    };
}; 