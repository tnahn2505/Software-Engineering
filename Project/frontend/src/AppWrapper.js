import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import AppRoutes from './AppRoutes'; // Tách AppRoutes riêng để dễ quản lý

function AppWrapper() {
    const [loggedIn, setLoggedIn] = useState(false);
    const [role, setRole] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('token');
        const userRole = localStorage.getItem('role');
        if (token && userRole) {
            setLoggedIn(true);
            setRole(userRole);
        }
    }, []);

    const handleLoginSuccess = (token, userRole) => {
        localStorage.setItem('token', token);
        localStorage.setItem('role', userRole);
        setLoggedIn(true);
        setRole(userRole);
    };

    return (
        <Router>
            <AppRoutes
                loggedIn={loggedIn}
                role={role}
                setLoggedIn={setLoggedIn}
                setRole={setRole}
                onLoginSuccess={handleLoginSuccess}
            />
        </Router>
    );
}

export default AppWrapper;
