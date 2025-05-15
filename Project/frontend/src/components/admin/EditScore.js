import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaSave, FaBook, FaUserGraduate } from 'react-icons/fa';
import studentApi from '../../services/studentApi';
import Header from '../common/Header';

const BEHAVIOR_OPTIONS = [
    { value: 'TOT', label: 'Tốt' },
    { value: 'KHA', label: 'Khá' },
    { value: 'TRUNG_BINH', label: 'Trung bình' },
    { value: 'YEU', label: 'Yếu' }
];

const EditScore = ({ onLogout }) => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [student, setStudent] = useState(null);
    const [scores, setScores] = useState({
        mathScore: '',
        physicsScore: '',
        chemistryScore: '',
        literatureScore: '',
        englishScore: '',
        behaviorScore: 'TOT'
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        const fetchStudent = async () => {
            try {
                const response = await studentApi.get(`/scores/${id}`);
                setStudent(response.data);
                if (response.data) {
                    setScores({
                        mathScore: response.data.mathScore || '',
                        physicsScore: response.data.physicsScore || '',
                        chemistryScore: response.data.chemistryScore || '',
                        literatureScore: response.data.literatureScore || '',
                        englishScore: response.data.englishScore || '',
                        behaviorScore: response.data.behaviorScore || 'TOT'
                    });
                }
                setLoading(false);
            } catch (error) {
                console.error('Error fetching student:', error);
                setErrors({ fetch: 'Không thể tải thông tin học sinh. Vui lòng thử lại sau.' });
                setLoading(false);
            }
        };
        fetchStudent();
    }, [id]);

    const validateScore = (value) => {
        if (value === '') return { isValid: true };
        const strValue = String(value); // Đảm bảo value là chuỗi
        const score = parseFloat(strValue);
        if (isNaN(score)) {
            return { isValid: false, message: 'Điểm phải là số' };
        }
        if (score < 0) {
            return { isValid: false, message: 'Điểm không được âm' };
        }
        if (score > 10) {
            return { isValid: false, message: 'Điểm không được lớn hơn 10' };
        }
        // Kiểm tra số thập phân
        if (strValue.includes('.') && strValue.split('.')[1].length > 1) {
            return { isValid: false, message: 'Điểm chỉ được có 1 chữ số thập phân' };
        }
        return { isValid: true };
    };

    const handleScoreChange = (e) => {
        const { name, value } = e.target;
        
        // Chỉ cho phép nhập số và dấu chấm
        if (value !== '' && !/^\d*\.?\d*$/.test(value)) {
            return;
        }

        setScores(prev => ({
            ...prev,
            [name]: value
        }));

        // Validate và set error
        const validation = validateScore(value);
        if (!validation.isValid) {
            setErrors(prev => ({
                ...prev,
                [name]: validation.message
            }));
        } else {
            setErrors(prev => {
                const newErrors = { ...prev };
                delete newErrors[name];
                return newErrors;
            });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validate all scores
        const scoreFields = ['mathScore', 'physicsScore', 'chemistryScore', 'literatureScore', 'englishScore'];
        const newErrors = {};
        
        scoreFields.forEach(field => {
            const validation = validateScore(scores[field]);
            if (!validation.isValid) {
                newErrors[field] = validation.message;
            }
        });

        if (!scores.behaviorScore) {
            newErrors.behaviorScore = 'Vui lòng chọn hạnh kiểm';
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        // Convert empty strings to null and string numbers to numbers
        const scoreData = {
            ...scores,
            mathScore: scores.mathScore === '' ? null : parseFloat(scores.mathScore),
            physicsScore: scores.physicsScore === '' ? null : parseFloat(scores.physicsScore),
            chemistryScore: scores.chemistryScore === '' ? null : parseFloat(scores.chemistryScore),
            literatureScore: scores.literatureScore === '' ? null : parseFloat(scores.literatureScore),
            englishScore: scores.englishScore === '' ? null : parseFloat(scores.englishScore)
        };

        try {
            await studentApi.put(`/updateScore/${id}`, scoreData);
            alert('✅ Cập nhật điểm thành công!');
            const role = localStorage.getItem('role');
            if (role === 'ADMIN') {
                navigate('/students');
            } else if (role === 'TEACHER') {
                navigate('/');
            } else {
                navigate('/');
            }
        } catch (error) {
            console.error('Error updating scores:', error);
            setErrors({ 
                submit: error.response?.data?.message || 'Có lỗi xảy ra khi cập nhật điểm' 
            });
        }
    };

    if (loading) {
        return <div className="score-form-loading">Đang tải thông tin...</div>;
    }

    if (errors.fetch) {
        return <div className="score-form-error">{errors.fetch}</div>;
    }

    return (
        <div>
            <Header onLogout={onLogout} />
            <div className="score-form-container">
                <h2 className="score-form-title">
                    <FaUserGraduate className="subject-icon" />
                    Cập nhật điểm cho học sinh: {student?.firstName} {student?.lastName}
                </h2>
                
                <form onSubmit={handleSubmit}>
                    <div className="score-form-grid">
                        <div className="score-form-group">
                            <label>
                                <FaBook className="subject-icon" />
                                Toán
                            </label>
                            <input
                                type="text"
                                name="mathScore"
                                value={scores.mathScore}
                                onChange={handleScoreChange}
                                placeholder="Nhập điểm từ 0-10"
                                className={errors.mathScore ? 'error' : ''}
                            />
                            {errors.mathScore && <span className="score-error-message">{errors.mathScore}</span>}
                        </div>

                        <div className="score-form-group">
                            <label>
                                <FaBook className="subject-icon" />
                                Lý
                            </label>
                            <input
                                type="text"
                                name="physicsScore"
                                value={scores.physicsScore}
                                onChange={handleScoreChange}
                                placeholder="Nhập điểm từ 0-10"
                                className={errors.physicsScore ? 'error' : ''}
                            />
                            {errors.physicsScore && <span className="score-error-message">{errors.physicsScore}</span>}
                        </div>

                        <div className="score-form-group">
                            <label>
                                <FaBook className="subject-icon" />
                                Hóa
                            </label>
                            <input
                                type="text"
                                name="chemistryScore"
                                value={scores.chemistryScore}
                                onChange={handleScoreChange}
                                placeholder="Nhập điểm từ 0-10"
                                className={errors.chemistryScore ? 'error' : ''}
                            />
                            {errors.chemistryScore && <span className="score-error-message">{errors.chemistryScore}</span>}
                        </div>

                        <div className="score-form-group">
                            <label>
                                <FaBook className="subject-icon" />
                                Văn
                            </label>
                            <input
                                type="text"
                                name="literatureScore"
                                value={scores.literatureScore}
                                onChange={handleScoreChange}
                                placeholder="Nhập điểm từ 0-10"
                                className={errors.literatureScore ? 'error' : ''}
                            />
                            {errors.literatureScore && <span className="score-error-message">{errors.literatureScore}</span>}
                        </div>

                        <div className="score-form-group">
                            <label>
                                <FaBook className="subject-icon" />
                                Anh
                            </label>
                            <input
                                type="text"
                                name="englishScore"
                                value={scores.englishScore}
                                onChange={handleScoreChange}
                                placeholder="Nhập điểm từ 0-10"
                                className={errors.englishScore ? 'error' : ''}
                            />
                            {errors.englishScore && <span className="score-error-message">{errors.englishScore}</span>}
                        </div>

                        <div className="score-form-group">
                            <label>
                                <FaUserGraduate className="subject-icon" />
                                Hạnh kiểm
                            </label>
                            <select
                                name="behaviorScore"
                                value={scores.behaviorScore}
                                onChange={(e) => setScores(prev => ({ ...prev, behaviorScore: e.target.value }))}
                                className={errors.behaviorScore ? 'error' : ''}
                            >
                                {BEHAVIOR_OPTIONS.map(option => (
                                    <option key={option.value} value={option.value}>
                                        {option.label}
                                    </option>
                                ))}
                            </select>
                            {errors.behaviorScore && <span className="score-error-message">{errors.behaviorScore}</span>}
                        </div>
                    </div>

                    {errors.submit && <div className="score-error-message text-center mb-3">{errors.submit}</div>}
                    
                    <button type="submit" className="score-submit-btn">
                        <FaSave />
                        Lưu điểm
                    </button>
                </form>
            </div>
        </div>
    );
};

export default EditScore;
