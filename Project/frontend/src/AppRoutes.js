import React from 'react';
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import Login from './pages/Login';
import HomePage from './pages/HomePage';
import HomeTeacher from './components/teacher/HomeTeacher';
import HomeStudent from './components/student/HomeStudent';
import StudentManagement from './components/admin/StudentManagement';
import AddStudent from './components/admin/AddStudent';
import EditStudent from './components/admin/EditStudent';
import EditTeacher from './components/admin/EditTeacher';
import AddTeacher from './components/admin/AddTeacher';
import TeacherManagement from './components/admin/TeacherManagement';
import ViewScore from './components/admin/ViewScore';
import EditScore from './components/admin/EditScore';
import ResetPassword from './pages/ResetPassword';

function AppRoutes({ loggedIn, role, setLoggedIn, setRole, onLoginSuccess }) {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        setLoggedIn(false);
        setRole('');
        navigate('/login');
    };

    return (
        <Routes>
            {!loggedIn ? (
                <>
                    <Route path="/login" element={<Login onLoginSuccess={onLoginSuccess} />} />
                    <Route path="/reset-password" element={<ResetPassword />} />
                    <Route path="*" element={<Navigate to="/login" />} />
                </>
            ) : (
                <>
                    <Route path="/" element={
                        role === 'ADMIN' ? <HomePage onLogout={handleLogout} /> :
                            role === 'TEACHER' ? <HomeTeacher onLogout={handleLogout} /> :
                                <HomeStudent onLogout={handleLogout} />
                    } />
                    <Route path="/students" element={<StudentManagement onLogout={handleLogout} />} />
                    <Route path="/students/add" element={<AddStudent onLogout={handleLogout} />} />
                    <Route path="/students/edit/:id" element={<EditStudent onLogout={handleLogout} />} />
                    <Route path="/students/score/view/:id" element={<ViewScore onLogout={handleLogout} />} />
                    <Route path="/students/score/edit/:id" element={<EditScore onLogout={handleLogout} />} />
                    <Route path="/teachers" element={<TeacherManagement onLogout={handleLogout} />} />
                    <Route path="/teachers/add" element={<AddTeacher onLogout={handleLogout} />} />
                    <Route path="/teachers/edit/:id" element={<EditTeacher onLogout={handleLogout} />} />
                    <Route path="*" element={<Navigate to="/" />} />
                </>
            )}
        </Routes>
    );
}

export default AppRoutes;
