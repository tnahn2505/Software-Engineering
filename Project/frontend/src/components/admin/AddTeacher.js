import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
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

function AddTeacher({ onLogout }) {
    const [teacher, setTeacher] = useState({
        fullName: '',
        email: '',
        phone: '',
        address: '',
        homeroomClass: '',
        subject: 'TOAN',
        teachingClasses: ['']
    });

    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setTeacher({ ...teacher, [name]: value });
    };

    const handleHomeroomClassChange = (e) => {
        setTeacher({ ...teacher, homeroomClass: e.target.value });
    };

    const handleTeachingClassChange = (idx, value) => {
        const newClasses = [...teacher.teachingClasses];
        newClasses[idx] = value;
        setTeacher({ ...teacher, teachingClasses: newClasses });
    };

    const addTeachingClass = () => {
        setTeacher({ ...teacher, teachingClasses: [...teacher.teachingClasses, ''] });
    };

    const removeTeachingClass = (idx) => {
        setTeacher({
            ...teacher,
            teachingClasses: teacher.teachingClasses.filter((_, i) => i !== idx)
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        // Validate email format
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test(teacher.email)) {
            alert("Email không hợp lệ. Vui lòng nhập đúng định dạng (ví dụ: example@gmail.com)");
            return;
        }

        // Validate phone number
        const phoneRegex = /^(0|\+84)(3|5|7|8|9)[0-9]{8}$/;
        if (teacher.phone && !phoneRegex.test(teacher.phone)) {
            alert("Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 hoặc +84 và có 10 chữ số");
            return;
        }

        // Validate full name
        const nameRegex = /^[A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*(?:[ ][A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*)*$/;
        if (!nameRegex.test(teacher.fullName)) {
            alert("Họ và tên không hợp lệ. Mỗi từ phải viết hoa chữ cái đầu");
            return;
        }

        try {
            const teacherData = {
                ...teacher,
                teachingClasses: teacher.teachingClasses.filter(cls => cls)
            };
            console.log('Teacher data being sent:', JSON.stringify(teacherData, null, 2));
            const response = await teacherApi.post('/addTeacher', teacherData);
            
            if (response.status === 200 && response.data) {
                console.log('Success response:', response.data);
                alert("✅ Thêm giáo viên thành công!");
                navigate('/teachers');
            } else {
                throw new Error('Không nhận được phản hồi từ server');
            }
        } catch (error) {
            console.error('Request that caused error:', JSON.stringify(teacher, null, 2));
            const errorMessage = error.response?.data?.error || error.response?.data?.message || error.message;
            console.error('Error details:', errorMessage);
            alert(`Lỗi khi thêm giáo viên: ${errorMessage}`);
        }
    };

    return (
        <div>
            <Header onLogout={onLogout} />
            <div className="form-container">
                <h2 className="form-title">Thêm giáo viên</h2>
                <form onSubmit={handleSubmit} className="student-form">
                    <input 
                        name="fullName" 
                        value={teacher.fullName} 
                        onChange={handleChange} 
                        placeholder="Họ và tên" 
                        required 
                        className="form-input"
                    />
                    <input 
                        name="email" 
                        value={teacher.email} 
                        onChange={handleChange} 
                        placeholder="Email" 
                        required 
                        className="form-input"
                    />
                    <input 
                        name="phone" 
                        value={teacher.phone} 
                        onChange={handleChange} 
                        placeholder="Số điện thoại" 
                        required 
                        className="form-input"
                    />
                    <input 
                        name="address" 
                        value={teacher.address} 
                        onChange={handleChange} 
                        placeholder="Địa chỉ" 
                        className="form-input"
                    />
                    <label className="form-label">Lớp chủ nhiệm:</label>
                    <select
                        name="homeroomClass"
                        value={teacher.homeroomClass}
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
                        value={teacher.subject}
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
                    {teacher.teachingClasses.map((cls, idx) => (
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
                            {teacher.teachingClasses.length > 1 && (
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
                    <button type="submit" className="btn submit-btn">➕ Thêm giáo viên</button>
                </form>
            </div>
        </div>
    );
}

export default AddTeacher;
