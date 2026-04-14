import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Auth.css';

const SKILL_OPTIONS = [
  'JavaScript', 'Python', 'HTML/CSS', 'React', 'Node.js',
  'Graphic Design', 'UI/UX', 'Photoshop', 'Figma',
  'Content Writing', 'Copywriting', 'SEO',
  'Data Entry', 'Excel', 'Research',
  'Video Editing', 'Photography', 'Social Media',
  'Translation', 'Tutoring', 'Math', 'Science',
];

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: '', email: '', password: '', bio: '', skills: [],
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const toggleSkill = (skill) => {
    setForm((prev) => ({
      ...prev,
      skills: prev.skills.includes(skill)
        ? prev.skills.filter((s) => s !== skill)
        : [...prev.skills, skill],
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.password.length < 6) return setError('Password must be at least 6 characters');
    setError('');
    setLoading(true);
    try {
      await register(form);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.error || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card auth-card-wide card">
        <div className="auth-header">
          <Link to="/" className="auth-logo">⚡ SkillHub</Link>
          <h1>Join SkillHub</h1>
          <p>Create your free student account</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="grid-2">
            <div className="form-group">
              <label htmlFor="name">Full Name *</label>
              <input
                id="name"
                type="text"
                name="name"
                className="form-control"
                placeholder="John Doe"
                value={form.name}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="email">Email *</label>
              <input
                id="email"
                type="email"
                name="email"
                className="form-control"
                placeholder="your@email.com"
                value={form.email}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="password">Password *</label>
            <input
              id="password"
              type="password"
              name="password"
              className="form-control"
              placeholder="At least 6 characters"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="bio">About You (Optional)</label>
            <textarea
              id="bio"
              name="bio"
              className="form-control"
              placeholder="Tell others about yourself, your studies, and your interests..."
              value={form.bio}
              onChange={handleChange}
              rows={3}
            />
          </div>

          <div className="form-group">
            <label>Your Skills (Optional — select all that apply)</label>
            <div className="skills-grid">
              {SKILL_OPTIONS.map((skill) => (
                <button
                  key={skill}
                  type="button"
                  className={`skill-chip ${form.skills.includes(skill) ? 'active' : ''}`}
                  onClick={() => toggleSkill(skill)}
                >
                  {skill}
                </button>
              ))}
            </div>
          </div>

          {error && <p className="error-text mb-4">{error}</p>}

          <button type="submit" className="btn btn-primary w-full" disabled={loading}>
            {loading ? 'Creating account...' : 'Create Account'}
          </button>
        </form>

        <p className="auth-footer">
          Already have an account?{' '}
          <Link to="/login">Login here</Link>
        </p>
      </div>
    </div>
  );
}
