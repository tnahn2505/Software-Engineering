import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Header from '../common/Header';
import studentApi from '../../services/studentApi';
import teacherApi from '../../services/teacherApi';
import { FaUserGraduate, FaChartBar, FaKey } from 'react-icons/fa';
import ChangePasswordForm from '../student/ChangePasswordForm';

const HomeTeacher = ({ onLogout }) => {
    const [students, setStudents] = useState([]);
    const [teacher, setTeacher] = useState(null);
    const [activeTab, setActiveTab] = useState('scores');

    useEffect(() => {
        if (activeTab === 'scores') {
            const fetchStudents = async () => {
                try {
                    const response = await studentApi.get('/byClass/10A1');
                    setStudents(response.data);
                } catch (error) {
                    console.error('Error fetching students:', error);
                }
            };
            fetchStudents();
        }
        if (activeTab === 'info') {
            const fetchTeacher = async () => {
                try {
                    const token = localStorage.getItem('token');
                    const res = await teacherApi.get('/me', {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });
                    setTeacher(res.data);
                } catch (error) {
                    console.error('Lỗi khi lấy thông tin giáo viên:', error);
                }
            };
            fetchTeacher();
        }
    }, [activeTab]);

    const ScoresTab = () => (
        <div className="overflow-x-auto">
            <table className="student-table w-full text-center">
                <thead>
                <tr>
                    <th>Họ</th>
                    <th>Tên</th>
                    <th>Lớp</th>
                    <th>Xem điểm</th>
                    <th>Sửa điểm</th>
                </tr>
                </thead>
                <tbody>
                {students.map((student) => (
                    <tr key={student.studentNumber}>
                        <td>{student.lastName}</td>
                        <td>{student.firstName}</td>
                        <td>{student.className}</td>
                        <td>
                            <Link to={`/students/score/view/${student.studentNumber}`} className="text-blue-600 hover:underline">
                                👁️ Xem
                            </Link>
                        </td>
                        <td>
                            <Link to={`/students/score/edit/${student.studentNumber}`} className="text-green-600 hover:underline">
                                ✏️ Sửa
                            </Link>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            {students.length === 0 && (
                <p className="text-center mt-4 text-gray-500">Không có học sinh nào trong lớp bạn giảng dạy.</p>
            )}
        </div>
    );

    const InfoTab = () => (
        <div className="teacher-info">
            <h3 className="text-xl font-semibold mb-4 text-blue-700">
                <FaUserGraduate className="inline-block mr-2" />
                Thông tin cá nhân
            </h3>
            {teacher ? (
                <div className="grid grid-cols-2 gap-4">
                    <div className="info-group">
                        <label>Họ và tên:</label>
                        <span>{teacher.fullName}</span>
                    </div>
                    <div className="info-group">
                        <label>Email:</label>
                        <span>{teacher.email}</span>
                    </div>
                    <div className="info-group">
                        <label>Số điện thoại:</label>
                        <span>{teacher.phone}</span>
                    </div>
                    <div className="info-group">
                        <label>Địa chỉ:</label>
                        <span>{teacher.address}</span>
                    </div>
                    <div className="info-group">
                        <label>Lớp chủ nhiệm:</label>
                        <span>{teacher.homeroomClass}</span>
                    </div>
                    <div className="info-group">
                        <label>Môn giảng dạy:</label>
                        <span>{teacher.subject}</span>
                    </div>
                    <div className="info-group">
                        <label>Lớp giảng dạy:</label>
                        <span>{teacher.teachingClasses ? teacher.teachingClasses.join(', ') : ''}</span>
                    </div>
                </div>
            ) : (
                <p>Đang tải thông tin cá nhân...</p>
            )}
        </div>
    );

    const ChangePasswordTab = () => (
        <ChangePasswordForm />
    );

    const renderContent = () => {
        switch (activeTab) {
            case 'scores':
                return <ScoresTab />;
            case 'info':
                return <InfoTab />;
            case 'password':
                return <ChangePasswordTab />;
            default:
                return <ScoresTab />;
        }
    };

    return (
        <div>
            <Header onLogout={onLogout} />
            <div className="max-w-6xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
                <div className="tab-buttons mb-6">
                    <button
                        className={`tab-button ${activeTab === 'scores' ? 'active' : ''}`}
                        onClick={() => setActiveTab('scores')}
                    >
                        <FaChartBar className="inline-block mr-2" />
                        Xem điểm học sinh
                    </button>
                    <button
                        className={`tab-button ${activeTab === 'info' ? 'active' : ''}`}
                        onClick={() => setActiveTab('info')}
                    >
                        <FaUserGraduate className="inline-block mr-2" />
                        Thông tin cá nhân
                    </button>
                    <button
                        className={`tab-button ${activeTab === 'password' ? 'active' : ''}`}
                        onClick={() => setActiveTab('password')}
                    >
                        <FaKey className="inline-block mr-2" />
                        Đổi mật khẩu
                    </button>
                </div>
                {renderContent()}
            </div>
        </div>
    );
};

export default HomeTeacher;
