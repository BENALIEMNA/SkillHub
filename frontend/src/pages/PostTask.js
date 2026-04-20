import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/api';
import { CATEGORIES, DIFFICULTIES } from '../utils/helpers';
import './PostTask.css';

export default function PostTask() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    title: '',
    description: '',
    category: '',
    budget: '',
    deadline: '',
    difficulty: 'easy',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!form.title || !form.description || !form.category || !form.budget || !form.deadline) {
      return setError('All fields are required');
    }
    if (Number(form.budget) <= 0) {
      return setError('Budget must be a positive amount');
    }

    setLoading(true);
    try {
      const { data } = await api.post('/tasks', { ...form, budget: Number(form.budget) });
      navigate(`/tasks/${data.id}`);
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to post task');
    } finally {
      setLoading(false);
    }
  };

  // Min date is today
  const today = new Date().toISOString().split('T')[0];

  return (
    <div className="post-task-page">
      <div className="container">
        <div className="post-task-container">
          <div className="post-task-header">
            <h1 className="page-title">Post a New Task</h1>
            <p className="page-subtitle">
              Describe what you need done and find a student to help you. Keep tasks simple and beginner-friendly!
            </p>
          </div>

          <div className="post-task-layout">
            <div className="card post-task-form">
              <form onSubmit={handleSubmit}>
                <div className="form-group">
                  <label htmlFor="title">Task Title *</label>
                  <input
                    id="title"
                    type="text"
                    name="title"
                    className="form-control"
                    placeholder="e.g., Design a logo for my student club"
                    value={form.title}
                    onChange={handleChange}
                    required
                    maxLength={100}
                  />
                  <span className="form-hint">{form.title.length}/100 characters</span>
                </div>

                <div className="form-group">
                  <label htmlFor="description">Description *</label>
                  <textarea
                    id="description"
                    name="description"
                    className="form-control"
                    placeholder="Describe what needs to be done, your requirements, and any helpful details..."
                    value={form.description}
                    onChange={handleChange}
                    required
                    rows={6}
                  />
                </div>

                <div className="grid-2">
                  <div className="form-group">
                    <label htmlFor="category">Category *</label>
                    <select
                      id="category"
                      name="category"
                      className="form-control"
                      value={form.category}
                      onChange={handleChange}
                      required
                    >
                      <option value="">Select a category</option>
                      {CATEGORIES.map((c) => (
                        <option key={c.value} value={c.value}>
                          {c.icon} {c.label}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="difficulty">Difficulty Level *</label>
                    <select
                      id="difficulty"
                      name="difficulty"
                      className="form-control"
                      value={form.difficulty}
                      onChange={handleChange}
                    >
                      {DIFFICULTIES.map((d) => (
                        <option key={d.value} value={d.value}>
                          {d.label} — {d.description}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

                <div className="grid-2">
                  <div className="form-group">
                    <label htmlFor="budget">Budget (USD) *</label>
                    <div className="input-prefix">
                      <span className="prefix">$</span>
                      <input
                        id="budget"
                        type="number"
                        name="budget"
                        className="form-control"
                        placeholder="25"
                        value={form.budget}
                        onChange={handleChange}
                        required
                        min="1"
                        max="500"
                        step="1"
                      />
                    </div>
                    <span className="form-hint">Recommended: $10–$100 for student tasks</span>
                  </div>

                  <div className="form-group">
                    <label htmlFor="deadline">Deadline *</label>
                    <input
                      id="deadline"
                      type="date"
                      name="deadline"
                      className="form-control"
                      value={form.deadline}
                      onChange={handleChange}
                      required
                      min={today}
                    />
                  </div>
                </div>

                {error && <p className="error-text mb-4">{error}</p>}

                <button type="submit" className="btn btn-primary btn-lg w-full" disabled={loading}>
                  {loading ? 'Posting Task...' : '🚀 Post Task'}
                </button>
              </form>
            </div>

            {/* Tips Sidebar */}
            <div className="post-task-tips">
              <div className="card tips-card">
                <h3>💡 Tips for a Great Task Post</h3>
                <ul>
                  <li>Be specific about what you need</li>
                  <li>Keep tasks simple and scoped</li>
                  <li>Set a fair budget for the work</li>
                  <li>Give enough time for students to complete</li>
                  <li>Choose "Beginner" if no experience needed</li>
                </ul>
              </div>

              <div className="card difficulty-guide">
                <h3>📊 Difficulty Guide</h3>
                {DIFFICULTIES.map((d) => (
                  <div key={d.value} className="difficulty-item">
                    <span className={`badge badge-${d.value}`}>{d.label}</span>
                    <span>{d.description}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
