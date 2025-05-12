import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Header from '../common/Header';
import studentApi from '../../services/studentApi';

const HomeTeacher = ({ onLogout }) => {
    const [students, setStudents] = useState([]);

    useEffect(() => {
        const fetchStudents = async () => {
            try {
                const response = await studentApi.get('/byClass/10A1');
                setStudents(response.data);
            } catch (error) {
                console.error('Error fetching students:', error);
            }
        };
        fetchStudents();
    }, []);

    return (
        <div>
            <Header onLogout={onLogout} />
            <div className="max-w-6xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
                <h2 className="text-2xl font-bold mb-6 text-center text-green-700">
                    Quản lý điểm học sinh lớp giảng dạy
                </h2>
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
                            <tr key={student.id}>
                                <td>{student.lastName}</td>
                                <td>{student.firstName}</td>
                                <td>{student.className}</td>
                                <td>
                                    <Link to={`/view-score/${student.id}`} className="text-blue-600 hover:underline">
                                        👁️ Xem
                                    </Link>
                                </td>
                                <td>
                                    <Link to={`/edit-score/${student.id}`} className="text-green-600 hover:underline">
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
            </div>
        </div>
    );
};

export default HomeTeacher;
