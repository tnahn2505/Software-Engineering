import React, { useEffect, useState } from 'react';
import Header from '../components/common/Header';
import studentApi from '../services/studentApi';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts';

const HomePage = ({ onLogout }) => {
    const [chartData, setChartData] = useState([]);
    const [stats, setStats] = useState({ students: 0, teachers: 0 });

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const [studentsRes, teachersRes] = await Promise.all([
                    studentApi.get('/getStudents'),
                    studentApi.get('/getTeachers')
                ]);
                setStats({
                    students: studentsRes.data.length,
                    teachers: teachersRes.data.length
                });
            } catch (error) {
                console.error('Error fetching stats:', error);
            }
        };
        fetchStats();
    }, []);

    useEffect(() => {
        fetchStatistics();
    }, []);

    const fetchStatistics = async () => {
        try {
            const res = await studentApi.get('/statistics');
            const rawData = res.data;

            // Convert to recharts-friendly format
            const formatted = Object.entries(rawData).map(([className, performance]) => ({
                className,
                ...performance
            }));

            setChartData(formatted);
        } catch (error) {
            console.error('Lỗi lấy thống kê học lực:', error);
        }
    };

    return (
        <div>
            <Header onLogout={onLogout} />
        <div className="home-container">
            <div className="home-content">
                <h2>Thống kê học lực theo lớp</h2>

                {chartData.length > 0 ? (
                    <ResponsiveContainer width="100%" height={400}>
                        <BarChart
                            data={chartData}
                            margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
                        >
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="className" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Bar dataKey="Giỏi" fill="#4CAF50" />
                            <Bar dataKey="Khá" fill="#2196F3" />
                            <Bar dataKey="Trung Bình" fill="#FFC107" />
                            <Bar dataKey="Yếu" fill="#F44336" />
                        </BarChart>
                    </ResponsiveContainer>
                ) : (
                    <p>Đang tải thống kê...</p>
                )}
            </div>
        </div>
        </div>

    );
};

export default HomePage;
