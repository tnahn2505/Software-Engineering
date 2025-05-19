import React, { useState } from 'react';
import { FaKey } from 'react-icons/fa';
import axios from 'axios';

const ChangePasswordForm = () => {
    const [form, setForm] = useState({
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
    });
    const [message, setMessage] = useState({ type: '', text: '' });

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage({ type: '', text: '' });

        // Validate passwords
        if (form.newPassword !== form.confirmPassword) {
            setMessage({ type: 'error', text: 'Mật khẩu mới không khớp!' });
            return;
        }

        if (form.newPassword.length < 6) {
            setMessage({ type: 'error', text: 'Mật khẩu mới phải có ít nhất 6 ký tự!' });
            return;
        }

        try {
            const token = localStorage.getItem('token');
            await axios.post('http://localhost:8089/users/auth/change-password', 
                {
                    currentPassword: form.currentPassword,
                    newPassword: form.newPassword
                },
                {
                    headers: { Authorization: `Bearer ${token}` }
                }
            );
            
            setMessage({ type: 'success', text: 'Đổi mật khẩu thành công!' });
            setForm({ currentPassword: '', newPassword: '', confirmPassword: '' });
        } catch (error) {
            setMessage({ 
                type: 'error', 
                text: error.response?.data?.message || 'Có lỗi xảy ra khi đổi mật khẩu!'
            });
        }
    };

    return (
        <div className="change-password-section">
            <h3 className="text-xl font-semibold mb-4 text-blue-700">
                <FaKey className="inline-block mr-2" />
                Đổi mật khẩu
            </h3>
            <form onSubmit={handleSubmit} className="change-password-form">
                <div className="form-group">
                    <label>Mật khẩu hiện tại:</label>
                    <input
                        type="password"
                        name="currentPassword"
                        value={form.currentPassword}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Mật khẩu mới:</label>
                    <input
                        type="password"
                        name="newPassword"
                        value={form.newPassword}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Xác nhận mật khẩu mới:</label>
                    <input
                        type="password"
                        name="confirmPassword"
                        value={form.confirmPassword}
                        onChange={handleChange}
                        required
                    />
                </div>
                {message.text && (
                    <div className={`message ${message.type}`}>
                        {message.text}
                    </div>
                )}
                <button type="submit" className="change-password-btn">
                    Đổi mật khẩu
                </button>
            </form>
        </div>
    );
};

export default ChangePasswordForm; 