import React from 'react';
import { Spin } from 'antd';
import styled from 'styled-components';

const LoadingWrapper = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 200px;
`;

export const PageLoading = () => (
    <LoadingWrapper>
        <Spin size="large" />
    </LoadingWrapper>
); 