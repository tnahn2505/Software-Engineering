import React from 'react';
import { useNavigate } from 'react-router-dom';

const StudentHeader = ({ onLogout }) => {
    const navigate = useNavigate();

    return (
        <header className="app-header">
            <h2 className="app-title" onClick={() => navigate('/')}>
                NPK School
            </h2>
            <div className="nav-buttons">
                <button onClick={onLogout} className="logout-button">
                    Đăng xuất
                </button>
            </div>
        </header>
    );
};

export default StudentHeader; 