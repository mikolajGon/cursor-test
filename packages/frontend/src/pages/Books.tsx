import React, { useState } from 'react';
import { Table, Card, Button, Space, Modal, Form, Input, message, Alert } from 'antd';
import { BookOutlined, PlusOutlined, ReloadOutlined } from '@ant-design/icons';
import styled from 'styled-components';
import { useBooks, useCreateBook } from '../hooks/useBooks';
import { Book } from '../types';
import { PageLoading } from '../components/PageLoading';
import { ErrorBoundary } from '../components/ErrorBoundary';

const PageHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
`;

const Title = styled.h1`
  margin: 0;
  color: #1890ff;
`;

interface BookFormData extends Omit<Book, 'id'> {
    title: string;
    author: string;
    isbn: string;
}

export const Books = () => {
    const { data: books, isLoading, error, refetch } = useBooks();
    const createBook = useCreateBook();
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [form] = Form.useForm<BookFormData>();

    const columns = [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
            sorter: (a: Book, b: Book) => a.title.localeCompare(b.title),
        },
        {
            title: 'Author',
            dataIndex: 'author',
            key: 'author',
            sorter: (a: Book, b: Book) => a.author.localeCompare(b.author),
        },
        {
            title: 'ISBN',
            dataIndex: 'isbn',
            key: 'isbn',
        },
        {
            title: 'Actions',
            key: 'actions',
            render: (_: any, record: Book) => (
                <Space>
                    <Button type="link" icon={<BookOutlined />}>
                        Details
                    </Button>
                    <Button type="link">Edit</Button>
                    <Button type="link" danger>
                        Delete
                    </Button>
                </Space>
            ),
        },
    ];

    const handleAddBook = async (values: BookFormData) => {
        try {
            await createBook.mutateAsync({
                title: values.title,
                author: values.author,
                isbn: values.isbn,
            });
            message.success('Book added successfully');
            setIsModalVisible(false);
            form.resetFields();
        } catch (error) {
            message.error('Failed to add book');
        }
    };

    if (error) {
        return (
            <Alert
                message="Error"
                description={error instanceof Error ? error.message : 'Failed to load books'}
                type="error"
                showIcon
                action={
                    <Button icon={<ReloadOutlined />} onClick={() => refetch()}>
                        Retry
                    </Button>
                }
            />
        );
    }

    return (
        <ErrorBoundary>
            <div>
                <PageHeader>
                    <Title>Books Management</Title>
                    <Space>
                        <Button
                            icon={<ReloadOutlined />}
                            onClick={() => refetch()}
                            loading={isLoading}
                        >
                            Refresh
                        </Button>
                        <Button
                            type="primary"
                            icon={<PlusOutlined />}
                            onClick={() => setIsModalVisible(true)}
                        >
                            Add Book
                        </Button>
                    </Space>
                </PageHeader>

                <Card>
                    {isLoading ? (
                        <PageLoading />
                    ) : (
                        <Table
                            columns={columns}
                            dataSource={books?.data || []}
                            rowKey="id"
                            pagination={{
                                pageSize: 10,
                                showSizeChanger: true,
                                showTotal: (total) => `Total ${total} books`,
                            }}
                        />
                    )}
                </Card>

                <Modal
                    title="Add New Book"
                    open={isModalVisible}
                    onCancel={() => setIsModalVisible(false)}
                    footer={null}
                >
                    <Form
                        form={form}
                        layout="vertical"
                        onFinish={handleAddBook}
                        autoComplete="off"
                    >
                        <Form.Item
                            name="title"
                            label="Title"
                            rules={[{ required: true, message: 'Please input the title!' }]}
                        >
                            <Input />
                        </Form.Item>

                        <Form.Item
                            name="author"
                            label="Author"
                            rules={[{ required: true, message: 'Please input the author!' }]}
                        >
                            <Input />
                        </Form.Item>

                        <Form.Item
                            name="isbn"
                            label="ISBN"
                            rules={[
                                { required: true, message: 'Please input the ISBN!' },
                                {
                                    pattern: /^[0-9-]{10,13}$/,
                                    message: 'Please enter a valid ISBN!',
                                },
                            ]}
                        >
                            <Input />
                        </Form.Item>

                        <Form.Item>
                            <Space style={{ width: '100%', justifyContent: 'flex-end' }}>
                                <Button onClick={() => setIsModalVisible(false)}>Cancel</Button>
                                <Button
                                    type="primary"
                                    htmlType="submit"
                                    loading={createBook.isPending}
                                >
                                    Add Book
                                </Button>
                            </Space>
                        </Form.Item>
                    </Form>
                </Modal>
            </div>
        </ErrorBoundary>
    );
};
