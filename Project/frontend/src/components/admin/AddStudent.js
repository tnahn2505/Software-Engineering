import React, { useState } from 'react';
import studentApi from '../../services/studentApi';
import { useNavigate, useLocation } from 'react-router-dom';
import Header from '../common/Header';

const AddStudent = ({ onLogout }) => {
    const navigate = useNavigate();
    const location = useLocation();
    const params = new URLSearchParams(location.search);
    const selectedClass = params.get('class') || '';

    // THAY THẾ toàn bộ useState form
    const [form, setForm] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        address: '',
        className: selectedClass
    });

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
    e.preventDefault();
    try {
        const token = localStorage.getItem('token');
        await studentApi.post('/addStudent', form, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        alert('✅ Thêm học sinh thành công!');
        navigate('/students');
    } catch (error) {
        console.error('Lỗi thêm học sinh:', error);
        alert('❌ Lỗi thêm học sinh!');
    }
};


    return (
        <div>
            <Header onLogout={onLogout} />

            <div className="form-container">
                <h2 className="form-title">Thêm học sinh</h2>

                <form onSubmit={handleSubmit} className="student-form">
                    <input
                        name="firstName"
                        placeholder="Họ"
                        value={form.firstName}
                        onChange={handleChange}
                        required
                    />
                    <input
                        name="lastName"
                        placeholder="Tên"
                        value={form.lastName}
                        onChange={handleChange}
                        required
                    />
                    <input
                        name="email"
                        placeholder="Email"
                        value={form.email}
                        onChange={handleChange}
                        required
                    />
                    <input
                        name="phone"
                        placeholder="Số điện thoại"
                        value={form.phone}
                        onChange={handleChange}
                        required
                    />
                    <input
                        name="address"
                        placeholder="Địa chỉ"
                        value={form.address}
                        onChange={handleChange}
                        required
                    />

                    <input name="className" value={form.className} readOnly className="readonly" />
                    <button type="submit" className="btn submit-btn">➕ Thêm</button>
                </form>

            </div>
        </div>
    );
};

export default AddStudent;
