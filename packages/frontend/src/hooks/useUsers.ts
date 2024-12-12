import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';


import { protectedApi } from '../api/protected';

export const useUsers = () => {
    const { data: users, isLoading } = useQuery({
        queryKey: ['users'],
        queryFn: protectedApi.users.getAll
    });

    return { users, isLoading };
};

export const useUser = (id: number) => {
    return useQuery({
        queryKey: ['users', id],
        queryFn: () => protectedApi.users.getById(id),
    });
};

export const useCreateUser = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn:  protectedApi.users.create,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['users'] });
        }
    });
}; 