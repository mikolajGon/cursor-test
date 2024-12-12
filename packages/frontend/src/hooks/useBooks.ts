import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { protectedApi } from '../api/protected';
import { Book } from '../types';

export const useBooks = () => {
    return useQuery({
        queryKey: ['books'],
        queryFn: protectedApi.books.getAll,
    });
};

export const useBook = (id: number) => {
    return useQuery({
        queryKey: ['books', id],
        queryFn: async () => {
            const response = await protectedApi.books.getAll();
            if (!response) throw new Error('Failed to fetch books');
            const book = response.data.find((b: Book) => b.id === id);
            if (!book) throw new Error('Book not found');
            return book;
        },
    });
};

export const useCreateBook = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: async (book: Omit<Book, 'id'>) => {
            const response = await protectedApi.books.create(book);
            if (!response) throw new Error('Failed to create book');
            return response.data;
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['books'] });
        },
    });
}; 