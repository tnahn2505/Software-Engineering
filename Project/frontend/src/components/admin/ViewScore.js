import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import studentApi from '../../services/studentApi';
import Header from '../common/Header';
import '../../App.css'; // đảm bảo App.css chứa CSS của student-table

const ViewScore = ({ onLogout }) => {
    const { id } = useParams();
    const [student, setStudent] = useState(null);

    useEffect(() => {
        const fetchStudent = async () => {
            try {
                const res = await studentApi.get(`/scores/${id}`);
                setStudent(res.data);
            } catch (error) {
                console.error("Lỗi khi lấy điểm:", error);
                alert("Không thể lấy điểm học sinh.");
            }
        };
        fetchStudent();
    }, [id]);

    if (!student) return <p className="text-center mt-10">Đang tải dữ liệu điểm...</p>;

    return (
        <div>
            <Header onLogout={onLogout} />
            <div className="max-w-6xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
                <h2 className="text-2xl font-bold mb-6 text-center text-blue-700">
                    Bảng điểm học sinh
                </h2>
                <div className="overflow-x-auto">
                    <table className="student-table w-full text-center">
                        <thead>
                        <tr>
                            <th>Họ</th>
                            <th>Tên</th>
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
                            <td>{student.lastName}</td>
                            <td>{student.firstName}</td>
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
            </div>
        </div>
    );
};

export default ViewScore;
