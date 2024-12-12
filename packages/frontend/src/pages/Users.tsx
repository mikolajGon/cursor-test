import React, { useState } from 'react';
import { Table, Card, Button, Space, Form, Input, Modal, message } from 'antd';
import { UserAddOutlined } from '@ant-design/icons';
import styled from 'styled-components';
import { ErrorBoundary } from '../components/ErrorBoundary';
import { useUsers, useCreateUser } from '../hooks/useUsers';


const StyledCard = styled(Card)`
    margin: 24px;
`;

interface UserFormData {
    username: string;
    email: string;
    fullName: string;
}

export const Users = () => {
    const { users, isLoading } = useUsers();
    const createUser = useCreateUser();
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [form] = Form.useForm<UserFormData>();

    const handleCreateUser = async (values: UserFormData) => {
        try {
            await createUser.mutateAsync(values);
            message.success('User created successfully');
            setIsModalVisible(false);
            form.resetFields();
        } catch (error) {
            message.error('Failed to create user');
        }
    };

    const columns = [
        {
            title: 'Username',
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: 'Full Name',
            dataIndex: 'fullName',
            key: 'fullName',
        },
    ];

    return (
        <ErrorBoundary>
            <StyledCard
                title="Users"
                extra={
                    <Button
                        type="primary"
                        icon={<UserAddOutlined />}
                        onClick={() => setIsModalVisible(true)}
                    >
                        Add User
                    </Button>
                }
            >
                <Table
                    dataSource={users?.data || []}
                    columns={columns}
                    loading={isLoading}
                    rowKey="id"
                />
                <Modal
                    title="Create User"
                    open={isModalVisible}
                    onCancel={() => setIsModalVisible(false)}
                    footer={null}
                >
                    <Form
                        form={form}
                        layout="vertical"
                        onFinish={handleCreateUser}
                    >
                        <Form.Item
                            name="username"
                            label="Username"
                            rules={[{ required: true, message: 'Username is required' }]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            name="email"
                            label="Email"
                            rules={[
                                { required: true, message: 'Email is required' },
                                { type: 'email', message: 'Invalid email format' }
                            ]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            name="fullName"
                            label="Full Name"
                            rules={[{ required: true, message: 'Full name is required' }]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item>
                            <Space>
                                <Button type="primary" htmlType="submit">
                                    Create
                                </Button>
                                <Button onClick={() => setIsModalVisible(false)}>
                                    Cancel
                                </Button>
                            </Space>
                        </Form.Item>
                    </Form>
                </Modal>
            </StyledCard>
        </ErrorBoundary>
    );
}; 