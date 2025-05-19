import React, { useState, useEffect } from 'react';
import studentApi from '../../services/studentApi';
import { useParams, useNavigate } from 'react-router-dom';
import Header from '../common/Header';

const EditStudent = ({ onLogout }) => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [form, setForm] = useState(null);

    useEffect(() => {
        const fetchStudent = async () => {
            try {
                const res = await studentApi.get(`/${id}`);
                setForm(res.data);
            } catch (error) {
                console.error('Lỗi lấy học sinh:', error);
                alert('Không tìm thấy học sinh');
                navigate('/student');
            }
        };
        fetchStudent();
    }, [id, navigate]);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const updatedData = {
                firstName: form.firstName,
                lastName: form.lastName,
                email: form.email,
                phone: form.phone,
                address: form.address,
                className: form.className
            };
            await studentApi.put(`/update/${id}`, updatedData);
            alert('✅ Cập nhật học sinh thành công!');
            navigate('/students'); // 🔄 fix điều hướng
        } catch (error) {
            console.error('❌ Lỗi cập nhật:', error);
            alert('Lỗi khi cập nhật học sinh. Vui lòng thử lại!');
        }
    };

    if (!form) return <p className="text-center mt-10">Đang tải dữ liệu...</p>;

    return (
        <div>
            <Header onLogout={onLogout} />
            <div className="form-container">
                <h2 className="form-title">Cập nhật học sinh</h2>
                <form onSubmit={handleSubmit} className="student-form">
                    <input name="firstName" value={form.firstName} onChange={handleChange} placeholder="Họ" required />
                    <input name="lastName" value={form.lastName} onChange={handleChange} placeholder="Tên" required />
                    <input name="email" value={form.email} onChange={handleChange} placeholder="Email" required />
                    <input name="phone" value={form.phone} onChange={handleChange} placeholder="Số điện thoại" />
                    <input name="address" value={form.address} onChange={handleChange} placeholder="Địa chỉ" />
                    <input name="className" value={form.className} onChange={handleChange} placeholder="Lớp" className="bg-gray-100" />
                    <button type="submit" className="btn submit-btn">💾 Lưu thay đổi</button>
                </form>
            </div>
        </div>
    );
};

export default EditStudent;
