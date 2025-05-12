import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import studentApi from '../../services/studentApi';
import StudentHeader from '../common/StudentHeader';
import { FaUserGraduate, FaChartBar, FaKey } from 'react-icons/fa';
import ChangePasswordForm from './ChangePasswordForm';

const HomeStudent = ({ onLogout }) => {
    const [student, setStudent] = useState(null);
    const [activeTab, setActiveTab] = useState('scores');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchStudent = async () => {
            try {
                const token = localStorage.getItem('token');
                const res = await studentApi.get('/me', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setStudent(res.data);
            } catch (error) {
                console.error("Lỗi khi lấy dữ liệu học sinh:", error);
                alert("Không thể tải thông tin học sinh.");
                navigate('/login');
            }
        };
        fetchStudent();
    }, [navigate]);

    if (!student) return <p className="text-center mt-10">Đang tải dữ liệu...</p>;

    const ScoresTab = () => (
        <div className="overflow-x-auto">
            <h3 className="text-xl font-semibold mb-4 text-blue-700">
                <FaChartBar className="inline-block mr-2" />
                Bảng điểm học tập
            </h3>
            <table className="student-table w-full text-center">
                <thead>
                <tr>
                    <th>Toán</th>
                    <th>Lý</th>
                    <th>Hóa</th>
                    <th>Văn</th>
                    <th>Anh</th>
                    <th>Hạnh kiểm</th>
                    <th>Điểm TB</th>
                    <th>Học lực</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>{student.mathScore ?? '-'}</td>
                    <td>{student.physicsScore ?? '-'}</td>
                    <td>{student.chemistryScore ?? '-'}</td>
                    <td>{student.literatureScore ?? '-'}</td>
                    <td>{student.englishScore ?? '-'}</td>
                    <td>{student.behaviorScore ?? '-'}</td>
                    <td>{student.averageScore?.toFixed(2) ?? '-'}</td>
                    <td className="font-semibold text-blue-600">{student.academicPerformance ?? '-'}</td>
                </tr>
                </tbody>
            </table>
        </div>
    );

    const InfoTab = () => (
        <div className="student-info">
            <h3 className="text-xl font-semibold mb-4 text-blue-700">
                <FaUserGraduate className="inline-block mr-2" />
                Thông tin cá nhân
            </h3>
            <div className="grid grid-cols-2 gap-4">
                <div className="info-group">
                    <label>Họ:</label>
                    <span>{student.lastName}</span>
                </div>
                <div className="info-group">
                    <label>Tên:</label>
                    <span>{student.firstName}</span>
                </div>
                <div className="info-group">
                    <label>Email:</label>
                    <span>{student.email}</span>
                </div>
                <div className="info-group">
                    <label>Số điện thoại:</label>
                    <span>{student.phone}</span>
                </div>
                <div className="info-group">
                    <label>Địa chỉ:</label>
                    <span>{student.address}</span>
                </div>
                <div className="info-group">
                    <label>Lớp:</label>
                    <span>{student.className}</span>
                </div>
            </div>
        </div>
    );

    const renderContent = () => {
        switch (activeTab) {
            case 'scores':
                return <ScoresTab />;
            case 'info':
                return <InfoTab />;
            case 'password':
                return <ChangePasswordForm />;
            default:
                return <ScoresTab />;
        }
    };

    return (
        <div>
            <StudentHeader onLogout={onLogout} />
            <div className="max-w-6xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
                <div className="tab-buttons mb-6">
                    <button
                        className={`tab-button ${activeTab === 'scores' ? 'active' : ''}`}
                        onClick={() => setActiveTab('scores')}
                    >
                        <FaChartBar className="inline-block mr-2" />
                        Xem điểm
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

export default HomeStudent;
