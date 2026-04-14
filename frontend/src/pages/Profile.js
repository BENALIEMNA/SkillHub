import React, { useState, useEffect } from 'react';
import api from '../utils/api';
import { useAuth } from '../context/AuthContext';
import { formatDate } from '../utils/helpers';

const SKILL_OPTIONS = [
  'JavaScript', 'Python', 'HTML/CSS', 'React', 'Node.js',
  'Graphic Design', 'UI/UX', 'Photoshop', 'Figma',
  'Content Writing', 'Copywriting', 'SEO',
  'Data Entry', 'Excel', 'Research',
  'Video Editing', 'Photography', 'Social Media',
  'Translation', 'Tutoring', 'Math', 'Science',
];

export default function Profile() {
  const { user, updateUser } = useAuth();
  const [form, setForm] = useState({ name: user?.name || '', bio: user?.bio || '', skills: user?.skills || [] });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const { data } = await api.get('/users/me');
        setForm({ name: data.name, bio: data.bio || '', skills: data.skills || [] });
      } catch {}
    };
    fetchProfile();
  }, []);

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
    setLoading(true);
    setError('');
    setSuccess('');
    try {
      const { data } = await api.put('/users/me', form);
      updateUser({ ...user, ...data });
      setSuccess('Profile updated successfully!');
    } catch {
      setError('Failed to update profile');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '2rem 0 4rem', minHeight: 'calc(100vh - 64px)' }}>
      <div className="container">
        <div style={{ maxWidth: 640, margin: '0 auto' }}>
          <h1 className="page-title">My Profile</h1>
          <p className="page-subtitle">Update your info so clients can learn more about you</p>

          <div className="card">
            {/* Avatar display */}
            <div style={{ textAlign: 'center', marginBottom: '1.5rem' }}>
              <div style={{
                width: 80, height: 80, borderRadius: '50%',
                background: 'linear-gradient(135deg, var(--primary), var(--secondary))',
                color: 'white', display: 'flex', alignItems: 'center', justifyContent: 'center',
                fontSize: '2rem', fontWeight: 700, margin: '0 auto 0.5rem',
              }}>
                {form.name.charAt(0).toUpperCase()}
              </div>
              <div style={{ color: 'var(--text-light)', fontSize: '0.875rem' }}>
                Member since {formatDate(user?.created_at || new Date())}
              </div>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="prof-name">Full Name *</label>
                <input
                  id="prof-name"
                  type="text"
                  className="form-control"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  required
                />
              </div>

              <div className="form-group">
                <label>Email</label>
                <input className="form-control" value={user?.email} disabled style={{ opacity: 0.7 }} />
              </div>

              <div className="form-group">
                <label htmlFor="prof-bio">About Me</label>
                <textarea
                  id="prof-bio"
                  className="form-control"
                  placeholder="Tell others about yourself, your studies, and what you can help with..."
                  value={form.bio}
                  onChange={(e) => setForm({ ...form, bio: e.target.value })}
                  rows={4}
                />
              </div>

              <div className="form-group">
                <label>My Skills</label>
                <div className="skills-grid" style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem', marginTop: '0.5rem' }}>
                  {SKILL_OPTIONS.map((skill) => (
                    <button
                      key={skill}
                      type="button"
                      className={`skill-chip ${form.skills.includes(skill) ? 'active' : ''}`}
                      onClick={() => toggleSkill(skill)}
                      style={{
                        padding: '0.375rem 0.875rem',
                        borderRadius: '999px',
                        fontSize: '0.8125rem',
                        border: form.skills.includes(skill) ? 'none' : '1.5px solid var(--border)',
                        background: form.skills.includes(skill) ? 'var(--primary)' : 'white',
                        color: form.skills.includes(skill) ? 'white' : 'var(--text-light)',
                        cursor: 'pointer',
                        transition: 'all 0.2s',
                        fontFamily: 'inherit',
                        fontWeight: 500,
                      }}
                    >
                      {skill}
                    </button>
                  ))}
                </div>
              </div>

              {error && <p className="error-text mb-4">{error}</p>}
              {success && <p style={{ color: 'var(--secondary)', marginBottom: '1rem' }}>{success}</p>}

              <button type="submit" className="btn btn-primary w-full" disabled={loading}>
                {loading ? 'Saving...' : 'Save Profile'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
