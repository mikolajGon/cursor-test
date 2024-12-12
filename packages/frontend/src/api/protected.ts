import api from './axios';
import { Book, User } from '../types';


const handleError = (error: unknown) => {
    if (error instanceof Error) {
        throw error;
    }
    throw new Error('An unexpected error occurred');
};

export const protectedApi = {
    books: {
        getAll: async () => {
            try {
                const response = await api.get<Book[]>('/books');
                return response;
            } catch (error) {
                handleError(error);
            }
        },
        create: async (book: Omit<Book, 'id'>) => {
            try {
                const response = await api.post<Book>('/books', book);
                return response;
            } catch (error) {
                handleError(error);
            }
        },
        update: async (id: number, book: Partial<Book>) => {
            try {
                const response = await api.put<Book>(`/books/${id}`, book);
                return response;
            } catch (error) {
                handleError(error);
            }
        },
        delete: async (id: number) => {
            try {
                const response = await api.delete<void>(`/books/${id}`);
                return response;
            } catch (error) {
                handleError(error);
            }
        },
    },
    users: {
        getAll: async () => {
            try {
                const response = await api.get<User[]>('/users');
                return response;
            } catch (error) {
                handleError(error);
            }
        },
        create: async (user: Omit<User, 'id'>) => {
            try {
                const response = await api.post<User>('/users', user);
                return response;
            } catch (error) {
                handleError(error);
            }
        },
        update: async (id: number, user: Partial<User>) => {
            try {
                const response = await api.put<User>(`/users/${id}`, user);
                return response;
            } catch (error) {
                handleError(error);
            }
        },
        delete: async (id: number) => {
            try {
                const response = await api.delete<void>(`/users/${id}`);
                return response;
            } catch (error) {
                handleError(error);
            }
        },

        getById: async (id: number) => {
            try {
                const response = await api.get(`/users/${id}`);
                return response;
            } catch (error) {
                handleError(error);
            }
        },
    },
}; 