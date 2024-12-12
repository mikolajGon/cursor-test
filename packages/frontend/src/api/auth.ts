import api from './axios';
import { LoginRequest, LoginResponse } from '../types/auth';

export const authApi = {
    login: async (credentials: LoginRequest): Promise<LoginResponse> => {
        const { data } = await api.post('/auth/login', credentials);
        return data;
    }
}; 