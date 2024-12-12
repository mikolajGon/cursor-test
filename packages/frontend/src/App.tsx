import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Layout } from 'antd';
import styled from 'styled-components';
import { Books } from './pages/Books';
import { Users } from './pages/Users';
import { Login } from './pages/Login';
import { useAuth } from './hooks/useAuth';
import { Navigation } from './components/Navigation';
import { Logo } from './components/Logo';

const { Header, Content } = Layout;

const StyledLayout = styled(Layout)`
  min-height: 100vh;
`;

const StyledHeader = styled(Layout.Header)`
  display: flex;
  align-items: center;
  padding: 0 24px;
  background: #fff;
  width: 100%;
  position: fixed;
  z-index: 1;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
`;

const StyledContent = styled(Layout.Content)`
  padding: 24px;
  background: #f0f2f5;
  margin-top: 64px;
`;

const HeaderContent = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  gap: 48px;
`;

const LogoutButton = styled.button`
  background: #1890ff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    background: #40a9ff;
  }
`;

const queryClient = new QueryClient();

const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
    const { isAuthenticated } = useAuth();
    return isAuthenticated ? <>{children}</> : <Navigate to="/login" />;
};

export const App = () => {
    const { isAuthenticated, logout } = useAuth();

    return (
        <QueryClientProvider client={queryClient}>
            <StyledLayout>
                {isAuthenticated && (
                    <StyledHeader>
                        <Logo />
                        <Navigation />
                        <LogoutButton onClick={logout}>Logout</LogoutButton>
                    </StyledHeader>
                )}
                <StyledContent>
                    <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route
                            path="/"
                            element={
                                <ProtectedRoute>
                                    <div>Welcome to Library System</div>
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/books"
                            element={
                                <ProtectedRoute>
                                    <Books />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/users"
                            element={
                                <ProtectedRoute>
                                    <Users />
                                </ProtectedRoute>
                            }
                        />
                    </Routes>
                </StyledContent>
            </StyledLayout>
        </QueryClientProvider>
    );
}; 