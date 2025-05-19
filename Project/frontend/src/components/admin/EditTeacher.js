import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import teacherApi from '../../services/teacherApi';
import Header from '../common/Header';

const SUBJECTS = [
    { value: 'TOAN', label: 'Toán' },
    { value: 'LY', label: 'Lý' },
    { value: 'HOA', label: 'Hóa' },
    { value: 'VAN', label: 'Văn' },
    { value: 'ANH', label: 'Anh' }
];

const CLASS_OPTIONS = [
    { value: '10A1', label: '10A1' },
    { value: '10A2', label: '10A2' },
    { value: '11A1', label: '11A1' },
    { value: '11A2', label: '11A2' },
    { value: '12A1', label: '12A1' },
    { value: '12A2', label: '12A2' },
];

function EditTeacher({ onLogout }) {
    const [form, setForm] = useState({
        fullName: '',
        address: '',
        email: '',
        phone: '',
        homeroomClass: '',
        subject: 'TOAN',
        teachingClasses: ['']
    });

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        const loadTeacher = async () => {
            try {
                const res = await teacherApi.get(`/${id}`);
                setForm({
                    ...res.data,
                    teachingClasses: res.data.teachingClasses && res.data.teachingClasses.length > 0 ? res.data.teachingClasses : ['']
                });
            } catch (err) {
                console.error('Lỗi tải dữ liệu giáo viên:', err);
                alert('Không tìm thấy giáo viên');
                navigate('/teachers');
            }
        };
        loadTeacher();
    }, [id, navigate]);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleHomeroomClassChange = (e) => {
        setForm({ ...form, homeroomClass: e.target.value });
    };

    const handleTeachingClassChange = (idx, value) => {
        const newClasses = [...form.teachingClasses];
        newClasses[idx] = value;
        setForm({ ...form, teachingClasses: newClasses });
    };

    const addTeachingClass = () => {
        setForm({ ...form, teachingClasses: [...form.teachingClasses, ''] });
    };

    const removeTeachingClass = (idx) => {
        setForm({
            ...form,
            teachingClasses: form.teachingClasses.filter((_, i) => i !== idx)
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const updateData = {
                ...form,
                teachingClasses: form.teachingClasses.filter(cls => cls)
            };
            await teacherApi.put(`/update/${id}`, updateData);
            alert('✅ Cập nhật giáo viên thành công!');
            navigate('/teachers');
        } catch (error) {
            console.error('❌ Lỗi cập nhật giáo viên:', error);
            alert('Có lỗi khi cập nhật. Vui lòng thử lại!');
        }
    };

    return (
        <div>
            <Header onLogout={onLogout} />
            <div className="form-container">
                <h2 className="form-title">Cập nhật giáo viên</h2>
                <form onSubmit={handleSubmit} className="teacher-form">
                    <input 
                        name="fullName" 
                        placeholder="Họ và tên" 
                        value={form.fullName} 
                        onChange={handleChange} 
                        required 
                        className="form-input"
                    />
                    <input 
                        name="address" 
                        placeholder="Địa chỉ" 
                        value={form.address} 
                        onChange={handleChange} 
                        required 
                        className="form-input"
                    />
                    <input 
                        name="email" 
                        placeholder="Email" 
                        value={form.email} 
                        onChange={handleChange} 
                        required 
                        className="form-input"
                    />
                    <input 
                        name="phone" 
                        placeholder="Số điện thoại" 
                        value={form.phone} 
                        onChange={handleChange} 
                        required 
                        className="form-input"
                    />
                    <label className="form-label">Lớp chủ nhiệm:</label>
                    <select
                        name="homeroomClass"
                        value={form.homeroomClass}
                        onChange={handleHomeroomClassChange}
                        required
                        className="form-input"
                        style={{ marginBottom: 12 }}
                    >
                        <option value="">--Chọn lớp chủ nhiệm--</option>
                        {CLASS_OPTIONS.map(cls => (
                            <option key={cls.value} value={cls.value}>{cls.label}</option>
                        ))}
                    </select>
                    <label className="form-label">Môn giảng dạy:</label>
                    <select
                        name="subject"
                        value={form.subject}
                        onChange={handleChange}
                        required
                        className="form-input"
                        style={{ marginBottom: 12 }}
                    >
                        {SUBJECTS.map(subject => (
                            <option key={subject.value} value={subject.value}>
                                {subject.label}
                            </option>
                        ))}
                    </select>
                    <label className="form-label">Các lớp giảng dạy:</label>
                    {form.teachingClasses.map((cls, idx) => (
                        <div key={idx} style={{ display: 'flex', alignItems: 'center', marginBottom: 8 }}>
                            <select
                                value={cls}
                                onChange={e => handleTeachingClassChange(idx, e.target.value)}
                                required
                                className="form-input"
                                style={{ width: 150 }}
                            >
                                <option value="">--Chọn lớp--</option>
                                {CLASS_OPTIONS.map(opt => (
                                    <option key={opt.value} value={opt.value}>{opt.label}</option>
                                ))}
                            </select>
                            {form.teachingClasses.length > 1 && (
                                <button type="button" onClick={() => removeTeachingClass(idx)} style={{ marginLeft: 8 }}>❌</button>
                            )}
                        </div>
                    ))}
                    <button
                        type="button"
                        onClick={addTeachingClass}
                        style={{ marginBottom: 12 }}
                    >
                        ➕ Thêm lớp giảng dạy
                    </button>
                    <button type="submit" className="btn submit-btn">💾 Lưu thay đổi</button>
                </form>
            </div>
        </div>
    );
}

export default EditTeacher;
