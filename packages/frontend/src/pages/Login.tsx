import React from 'react';
import { Form, Input, Button, Card, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import styled from 'styled-components';
import { useLogin } from '../hooks/useAuth';
import { LoginRequest } from '../types/auth';

const LoginWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f0f2f5;
`;

const StyledCard = styled(Card)`
  width: 100%;
  max-width: 400px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
`;

const Logo = styled.h1`
  text-align: center;
  color: #1890ff;
  margin-bottom: 24px;
`;

export const Login = () => {
    const login = useLogin();

    const onFinish = async (values: LoginRequest) => {
        try {
            await login.mutateAsync(values);
        } catch (error) {
            message.error('Login failed. Please check your credentials.');
        }
    };

    return (
        <LoginWrapper>
            <StyledCard>
                <Logo>Library Management System</Logo>
                <Form
                    name="login"
                    initialValues={{ remember: true }}
                    onFinish={onFinish}
                    layout="vertical"
                >
                    <Form.Item
                        name="username"
                        rules={[{ required: true, message: 'Please input your username!' }]}
                    >
                        <Input
                            prefix={<UserOutlined />}
                            placeholder="Username"
                            size="large"
                        />
                    </Form.Item>

                    <Form.Item
                        name="password"
                        rules={[{ required: true, message: 'Please input your password!' }]}
                    >
                        <Input.Password
                            prefix={<LockOutlined />}
                            placeholder="Password"
                            size="large"
                        />
                    </Form.Item>

                    <Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                            size="large"
                            block
                            loading={login.isPending}
                        >
                            Log in
                        </Button>
                    </Form.Item>
                </Form>
            </StyledCard>
        </LoginWrapper>
    );
}; 