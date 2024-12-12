import React from 'react';
import { NavLink } from 'react-router-dom';
import { BookOutlined, UserOutlined, HomeOutlined } from '@ant-design/icons';
import { Menu } from 'antd';
import styled from 'styled-components';

const StyledMenu = styled(Menu)`
  &.ant-menu {
    display: flex;
    flex: 1;
    justify-content: flex-start;
    border-bottom: none;
    
    .ant-menu-item {
      flex: 0 1 auto;
      min-width: 100px;
      white-space: nowrap;
    }
    
    .ant-menu-overflow-item {
      flex: 0 0 auto;
    }
  }
`;

export const Navigation = () => {
    const items = [
        {
            key: '/',
            icon: <HomeOutlined />,
            label: <NavLink to="/">Home</NavLink>,
        },
        {
            key: '/books',
            icon: <BookOutlined />,
            label: <NavLink to="/books">Books</NavLink>,
        },
        {
            key: '/users',
            icon: <UserOutlined />,
            label: <NavLink to="/users">Users</NavLink>,
        },
    ];

    return (
        <StyledMenu 
            mode="horizontal" 
            items={items}
            overflowedIndicator={<>...</>}
        />
    );
}; 