import React from 'react';
import styled from 'styled-components';
import { BookOutlined } from '@ant-design/icons';

const LogoWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 24px;
  font-weight: bold;
  color: #1890ff;
`;

const LogoIcon = styled(BookOutlined)`
  font-size: 32px;
`;

export const Logo = () => (
    <LogoWrapper>
        <LogoIcon />
        <span>Library MS</span>
    </LogoWrapper>
); 