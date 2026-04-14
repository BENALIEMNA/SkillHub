import React from 'react';
import { Link } from 'react-router-dom';
import { getCategoryIcon, getCategoryLabel, getDifficultyLabel, formatCurrency, timeAgo } from '../utils/helpers';
import './TaskCard.css';

export default function TaskCard({ task }) {
  return (
    <Link to={`/tasks/${task.id}`} className="task-card">
      <div className="task-card-header">
        <span className="task-category">
          {getCategoryIcon(task.category)} {getCategoryLabel(task.category)}
        </span>
        <span className={`badge badge-${task.difficulty}`}>
          {getDifficultyLabel(task.difficulty)}
        </span>
      </div>

      <h3 className="task-title">{task.title}</h3>
      <p className="task-description">{task.description}</p>

      <div className="task-footer">
        <div className="task-meta">
          <span className="task-budget">{formatCurrency(task.budget)}</span>
          <span className="task-deadline">📅 {task.deadline}</span>
        </div>
        <div className="task-info">
          <span className="task-client">by {task.client_name}</span>
          <span className="task-time">{timeAgo(task.created_at)}</span>
        </div>
      </div>
    </Link>
  );
}
