import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../utils/api';
import { useAuth } from '../context/AuthContext';
import { getCategoryLabel, getCategoryIcon, formatCurrency, formatDate, timeAgo } from '../utils/helpers';
import './Dashboard.css';

export default function Dashboard() {
  const { user } = useAuth();
  const [postedTasks, setPostedTasks] = useState([]);
  const [myApplications, setMyApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('posted');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [postedRes, appsRes] = await Promise.all([
          api.get('/tasks/my/posted'),
          api.get('/applications/my'),
        ]);
        setPostedTasks(postedRes.data);
        setMyApplications(appsRes.data);
      } catch (err) {
        console.error('Failed to load dashboard data:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return <div className="spinner"></div>;

  return (
    <div className="dashboard-page">
      <div className="container">
        {/* Header */}
        <div className="dashboard-header card">
          <div className="dashboard-user">
            <div className="dashboard-avatar">
              {user.name.charAt(0).toUpperCase()}
            </div>
            <div>
              <h1>Welcome back, {user.name.split(' ')[0]}! 👋</h1>
              <p>{user.email}</p>
            </div>
          </div>
          <div className="dashboard-stats">
            <div className="dash-stat">
              <div className="dash-stat-value">{postedTasks.length}</div>
              <div className="dash-stat-label">Tasks Posted</div>
            </div>
            <div className="dash-stat">
              <div className="dash-stat-value">{myApplications.length}</div>
              <div className="dash-stat-label">Applications</div>
            </div>
            <div className="dash-stat">
              <div className="dash-stat-value">
                {myApplications.filter((a) => a.status === 'accepted').length}
              </div>
              <div className="dash-stat-label">Accepted</div>
            </div>
          </div>
          <div className="dashboard-actions">
            <Link to="/post-task" className="btn btn-primary">+ Post New Task</Link>
            <Link to="/tasks" className="btn btn-outline">Browse Tasks</Link>
            <Link to="/profile" className="btn btn-outline">Edit Profile</Link>
          </div>
        </div>

        {/* Tabs */}
        <div className="dashboard-tabs">
          <button
            className={`tab-btn ${activeTab === 'posted' ? 'active' : ''}`}
            onClick={() => setActiveTab('posted')}
          >
            My Posted Tasks
            {postedTasks.length > 0 && <span className="tab-count">{postedTasks.length}</span>}
          </button>
          <button
            className={`tab-btn ${activeTab === 'applications' ? 'active' : ''}`}
            onClick={() => setActiveTab('applications')}
          >
            My Applications
            {myApplications.length > 0 && <span className="tab-count">{myApplications.length}</span>}
          </button>
        </div>

        {/* Posted Tasks */}
        {activeTab === 'posted' && (
          <div className="tab-content">
            {postedTasks.length === 0 ? (
              <div className="empty-state card">
                <div className="icon">📋</div>
                <h3>No tasks posted yet</h3>
                <p>Post your first task and find a student to help you!</p>
                <Link to="/post-task" className="btn btn-primary mt-4">Post a Task</Link>
              </div>
            ) : (
              <div className="task-table card">
                <table>
                  <thead>
                    <tr>
                      <th>Task</th>
                      <th>Category</th>
                      <th>Budget</th>
                      <th>Deadline</th>
                      <th>Status</th>
                      <th>Posted</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {postedTasks.map((task) => (
                      <tr key={task.id}>
                        <td>
                          <Link to={`/tasks/${task.id}`} className="task-link">
                            {task.title}
                          </Link>
                        </td>
                        <td className="text-muted">
                          {getCategoryIcon(task.category)} {getCategoryLabel(task.category)}
                        </td>
                        <td className="font-semibold" style={{ color: 'var(--secondary)' }}>
                          {formatCurrency(task.budget)}
                        </td>
                        <td className="text-muted">{formatDate(task.deadline)}</td>
                        <td>
                          <span
                            className={`badge badge-${task.status}`}
                          >
                            {task.status.replace('_', ' ')}
                          </span>
                        </td>
                        <td className="text-muted">{timeAgo(task.created_at)}</td>
                        <td>
                          <Link to={`/tasks/${task.id}`} className="btn btn-outline btn-sm">
                            View
                          </Link>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}

        {/* Applications */}
        {activeTab === 'applications' && (
          <div className="tab-content">
            {myApplications.length === 0 ? (
              <div className="empty-state card">
                <div className="icon">🙋</div>
                <h3>No applications yet</h3>
                <p>Browse tasks and apply to ones that match your skills!</p>
                <Link to="/tasks" className="btn btn-primary mt-4">Browse Tasks</Link>
              </div>
            ) : (
              <div className="applications-grid">
                {myApplications.map((app) => (
                  <div key={app.id} className="app-card card">
                    <div className="app-card-header">
                      <Link to={`/tasks/${app.task_id}`} className="app-task-title">
                        {app.task_title}
                      </Link>
                      <span className={`badge badge-${app.status}`}>{app.status}</span>
                    </div>
                    <div className="app-card-meta">
                      <span>{getCategoryIcon(app.category)} {getCategoryLabel(app.category)}</span>
                      <span className="font-semibold" style={{ color: 'var(--secondary)' }}>
                        {formatCurrency(app.budget)}
                      </span>
                    </div>
                    <div className="app-card-meta">
                      <span className="text-muted">Client: {app.client_name}</span>
                      <span className="text-muted">Due: {formatDate(app.deadline)}</span>
                    </div>
                    <p className="app-message-preview">{app.message}</p>
                    <div className="app-card-footer">
                      <span className="text-muted text-sm">Applied {timeAgo(app.created_at)}</span>
                      <Link to={`/tasks/${app.task_id}`} className="btn btn-outline btn-sm">
                        View Task
                      </Link>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
