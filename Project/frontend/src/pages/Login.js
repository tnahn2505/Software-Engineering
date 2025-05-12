import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import userApi from '../services/userApi';

const Login = ({ onLoginSuccess }) => { // ✅ Nhận prop vào đây
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await userApi.post('/auth/login', { username, password });
            if (res.status === 200) {
                const { token, role } = res.data;
                onLoginSuccess(token, role);
                navigate('/');
            }
        } catch (err) {
            if (err.response?.status === 401) {
                setError('Sai tài khoản hoặc mật khẩu');
            } else {
                setError('Lỗi kết nối đến máy chủ');
            }
        }
    };

    return (
        <div className="login-container">
            <form className="login-box" onSubmit={handleLogin}>
                <h2 className="login-title">Đăng nhập</h2>
                <input
                    className="login-input"
                    type="text"
                    placeholder="Tên đăng nhập"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    className="login-input"
                    type="password"
                    placeholder="Mật khẩu"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button className="login-button" type="submit">Đăng nhập</button>
                {error && <p className="login-error">{error}</p>}
                <p className="login-forgot" onClick={() => navigate('/reset-password')}>
                    Quên mật khẩu?
                </p>
            </form>
        </div>
    );
};

export default Login;
