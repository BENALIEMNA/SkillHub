import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import api from '../utils/api';
import { useAuth } from '../context/AuthContext';
import { getCategoryIcon, getCategoryLabel, getDifficultyLabel, formatCurrency, formatDate, timeAgo } from '../utils/helpers';
import './TaskDetail.css';

export default function TaskDetail() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [task, setTask] = useState(null);
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [applying, setApplying] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [hasApplied, setHasApplied] = useState(false);
  const [showApplyForm, setShowApplyForm] = useState(false);

  const fetchTask = useCallback(async () => {
    try {
      const { data } = await api.get(`/tasks/${id}`);
      setTask(data);
    } catch (err) {
      setError('Task not found');
      console.error('Failed to load task:', err);
    } finally {
      setLoading(false);
    }
  }, [id]);

  const fetchApplications = useCallback(async () => {
    try {
      const { data } = await api.get(`/applications/task/${id}`);
      setApplications(data);
    } catch (err) {
      // Not the owner or not authenticated — applications are only visible to task owners
      if (err.response?.status !== 401 && err.response?.status !== 403) {
        console.error('Failed to load applications:', err);
      }
    }
  }, [id]);

  const checkApplied = useCallback(async () => {
    if (!user) return;
    try {
      const { data } = await api.get('/applications/my');
      setHasApplied(data.some((a) => a.task_id === parseInt(id)));
    } catch (err) {
      console.error('Failed to check applied status:', err);
    }
  }, [user, id]);

  useEffect(() => {
    fetchTask();
    fetchApplications();
    checkApplied();
  }, [fetchTask, fetchApplications, checkApplied]);

  const handleApply = async (e) => {
    e.preventDefault();
    if (!message.trim()) return setError('Please write a message');
    setApplying(true);
    setError('');
    try {
      await api.post('/applications', { task_id: parseInt(id), message });
      setSuccess('Application submitted successfully!');
      setHasApplied(true);
      setShowApplyForm(false);
      setMessage('');
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to submit application');
    } finally {
      setApplying(false);
    }
  };

  const handleAcceptReject = async (appId, status) => {
    try {
      await api.put(`/applications/${appId}`, { status });
      fetchApplications();
      fetchTask();
    } catch (err) {
      console.error('Failed to update application:', err);
      alert('Failed to update application');
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete this task?')) return;
    try {
      await api.delete(`/tasks/${id}`);
      navigate('/dashboard');
    } catch (err) {
      console.error('Failed to delete task:', err);
      alert('Failed to delete task');
    }
  };

  if (loading) return <div className="spinner"></div>;
  if (error && !task) return (
    <div className="container" style={{ padding: '3rem 0' }}>
      <div className="empty-state"><div className="icon">😕</div><p>{error}</p></div>
    </div>
  );

  const isOwner = user && task.client_id === user.id;
  const canApply = user && !isOwner && task.status === 'open' && !hasApplied;

  return (
    <div className="task-detail-page">
      <div className="container">
        <Link to="/tasks" className="back-link">← Back to Tasks</Link>

        <div className="task-detail-layout">
          {/* Main Content */}
          <div className="task-detail-main">
            <div className="card">
              <div className="task-detail-header">
                <div className="task-detail-meta">
                  <span className="task-category">
                    {getCategoryIcon(task.category)} {getCategoryLabel(task.category)}
                  </span>
                  <span className={`badge badge-${task.difficulty}`}>
                    {getDifficultyLabel(task.difficulty)}
                  </span>
                  <span className={`badge badge-${task.status}`}>
                    {task.status.replace('_', ' ')}
                  </span>
                </div>
                <h1 className="task-detail-title">{task.title}</h1>
                <div className="task-detail-submeta">
                  <span>Posted by <strong>{task.client_name}</strong></span>
                  <span>{timeAgo(task.created_at)}</span>
                </div>
              </div>

              <div className="task-detail-body">
                <h3>Description</h3>
                <p className="task-detail-description">{task.description}</p>
              </div>

              {isOwner && (
                <div className="owner-actions">
                  <Link to={`/tasks/${id}/edit`} className="btn btn-outline btn-sm">Edit Task</Link>
                  <button className="btn btn-danger btn-sm" onClick={handleDelete}>Delete Task</button>
                </div>
              )}
            </div>

            {/* Apply Section */}
            {!isOwner && task.status === 'open' && (
              <div className="card apply-section">
                {!user ? (
                  <div className="text-center">
                    <p>You need to be logged in to apply</p>
                    <Link to="/login" className="btn btn-primary mt-4">Login to Apply</Link>
                  </div>
                ) : hasApplied ? (
                  <div className="applied-notice">
                    ✅ <strong>You have already applied to this task</strong>
                    <p>The task owner will review your application.</p>
                  </div>
                ) : (
                  <>
                    {!showApplyForm ? (
                      <button className="btn btn-primary w-full" onClick={() => setShowApplyForm(true)}>
                        Apply for This Task
                      </button>
                    ) : (
                      <form onSubmit={handleApply}>
                        <h3 className="mb-4">Submit Your Application</h3>
                        <div className="form-group">
                          <label>Cover Message *</label>
                          <textarea
                            className="form-control"
                            placeholder="Explain why you're a good fit for this task, your relevant skills, and estimated completion time..."
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            rows={5}
                            required
                          />
                        </div>
                        {error && <p className="error-text mb-4">{error}</p>}
                        {success && <p style={{ color: 'var(--secondary)', marginBottom: '1rem' }}>{success}</p>}
                        <div className="flex gap-2">
                          <button type="submit" className="btn btn-primary" disabled={applying}>
                            {applying ? 'Submitting...' : 'Submit Application'}
                          </button>
                          <button type="button" className="btn btn-outline" onClick={() => { setShowApplyForm(false); setError(''); }}>
                            Cancel
                          </button>
                        </div>
                      </form>
                    )}
                  </>
                )}
              </div>
            )}

            {/* Applications (owner view) */}
            {isOwner && (
              <div className="card">
                <h3 className="mb-4">Applications ({applications.length})</h3>
                {applications.length === 0 ? (
                  <div className="empty-state">
                    <div className="icon">📬</div>
                    <p>No applications yet. Share your task to attract students!</p>
                  </div>
                ) : (
                  <div className="applications-list">
                    {applications.map((app) => (
                      <div key={app.id} className="application-item">
                        <div className="applicant-header">
                          <div className="applicant-avatar">
                            {app.freelancer_name.charAt(0).toUpperCase()}
                          </div>
                          <div>
                            <div className="applicant-name">{app.freelancer_name}</div>
                            <div className="applicant-email">{app.freelancer_email}</div>
                          </div>
                          <span className={`badge badge-${app.status}`}>{app.status}</span>
                        </div>
                        <p className="app-message">{app.message}</p>
                        {app.freelancer_skills?.length > 0 && (
                          <div className="applicant-skills">
                            {app.freelancer_skills.map((s) => (
                              <span key={s} className="tag">{s}</span>
                            ))}
                          </div>
                        )}
                        {app.status === 'pending' && task.status === 'open' && (
                          <div className="app-actions">
                            <button className="btn btn-secondary btn-sm" onClick={() => handleAcceptReject(app.id, 'accepted')}>
                              Accept
                            </button>
                            <button className="btn btn-danger btn-sm" onClick={() => handleAcceptReject(app.id, 'rejected')}>
                              Reject
                            </button>
                          </div>
                        )}
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}
          </div>

          {/* Sidebar */}
          <aside className="task-detail-sidebar">
            <div className="card sidebar-card">
              <div className="sidebar-budget">{formatCurrency(task.budget)}</div>
              <div className="sidebar-item">
                <span className="sidebar-label">📅 Deadline</span>
                <span className="sidebar-value">{formatDate(task.deadline)}</span>
              </div>
              <div className="sidebar-item">
                <span className="sidebar-label">📂 Category</span>
                <span className="sidebar-value">{getCategoryLabel(task.category)}</span>
              </div>
              <div className="sidebar-item">
                <span className="sidebar-label">⚡ Difficulty</span>
                <span className={`badge badge-${task.difficulty}`}>{getDifficultyLabel(task.difficulty)}</span>
              </div>
              <div className="sidebar-item">
                <span className="sidebar-label">📊 Status</span>
                <span className={`badge badge-${task.status}`}>{task.status.replace('_', ' ')}</span>
              </div>

              {canApply && (
                <button
                  className="btn btn-primary w-full mt-4"
                  onClick={() => {
                    setShowApplyForm(true);
                    document.querySelector('.apply-section')?.scrollIntoView({ behavior: 'smooth' });
                  }}
                >
                  Apply Now
                </button>
              )}
            </div>

            <div className="card sidebar-card">
              <h4 className="sidebar-section-title">About the Client</h4>
              <div className="client-info">
                <div className="client-avatar">
                  {task.client_name.charAt(0).toUpperCase()}
                </div>
                <div>
                  <div className="client-name">{task.client_name}</div>
                  <div className="client-email">{task.client_email}</div>
                </div>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </div>
  );
}
