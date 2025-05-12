import React, { useState, useEffect } from 'react';
import userApi from '../services/userApi';
import { useLocation, useNavigate } from 'react-router-dom';

const ResetPassword = () => {
    const [email, setEmail] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [step, setStep] = useState(1); // 1 for email input, 2 for new password
    const location = useLocation();
    const navigate = useNavigate();
    const token = new URLSearchParams(location.search).get('token');

    useEffect(() => {
        if (token) {
            setStep(2);
        }
    }, [token]);

    const handleRequestReset = async (e) => {
        e.preventDefault();
        try {
            await userApi.post('/auth/request-reset', null, {
                params: { email }
            });
            setMessage('Hướng dẫn đặt lại mật khẩu đã được gửi tới email của bạn');
            setError('');
        } catch (err) {
            setError('Không thể gửi email. Vui lòng kiểm tra lại.');
            setMessage('');
        }
    };

    const handleResetPassword = async (e) => {
        e.preventDefault();
        try {
            await userApi.post('/auth/reset-password', {
                token,
                newPassword
            });
            setMessage('Mật khẩu đã được đặt lại thành công');
            setError('');
            // Redirect to login after 2 seconds
            setTimeout(() => navigate('/login'), 2000);
        } catch (err) {
            setError('Không thể đặt lại mật khẩu. Vui lòng thử lại.');
            setMessage('');
        }
    };

    if (step === 1) {
        return (
            <div className="login-container">
                <form className="login-box" onSubmit={handleRequestReset}>
                    <h2 className="login-title">Quên mật khẩu</h2>
                    <input
                        className="login-input"
                        type="email"
                        placeholder="Nhập email của bạn"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <button className="login-button" type="submit">Gửi yêu cầu đặt lại mật khẩu</button>
                    {message && <p className="login-success">{message}</p>}
                    {error && <p className="login-error">{error}</p>}
                </form>
            </div>
        );
    }

    return (
        <div className="login-container">
            <form className="login-box" onSubmit={handleResetPassword}>
                <h2 className="login-title">Đặt lại mật khẩu</h2>
                <input
                    className="login-input"
                    type="password"
                    placeholder="Nhập mật khẩu mới"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    required
                />
                <button className="login-button" type="submit">Đặt lại mật khẩu</button>
                {message && <p className="login-success">{message}</p>}
                {error && <p className="login-error">{error}</p>}
            </form>
        </div>
    );
};

export default ResetPassword;
