import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import TaskCard from '../components/TaskCard';
import api from '../utils/api';
import { CATEGORIES, DIFFICULTIES } from '../utils/helpers';
import './Tasks.css';

export default function Tasks() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [search, setSearch] = useState(searchParams.get('search') || '');
  const [category, setCategory] = useState(searchParams.get('category') || 'all');
  const [difficulty, setDifficulty] = useState(searchParams.get('difficulty') || 'all');

  useEffect(() => {
    fetchTasks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [category, difficulty]);

  const fetchTasks = async (searchVal) => {
    setLoading(true);
    setError('');
    try {
      const params = {};
      if (category !== 'all') params.category = category;
      if (difficulty !== 'all') params.difficulty = difficulty;
      if (searchVal !== undefined ? searchVal : search) {
        params.search = searchVal !== undefined ? searchVal : search;
      }
      const { data } = await api.get('/tasks', { params });
      setTasks(data);
    } catch {
      setError('Failed to load tasks. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchTasks(search);
    const params = {};
    if (category !== 'all') params.category = category;
    if (difficulty !== 'all') params.difficulty = difficulty;
    if (search) params.search = search;
    setSearchParams(params);
  };

  const handleFilterChange = (key, value) => {
    if (key === 'category') setCategory(value);
    if (key === 'difficulty') setDifficulty(value);
  };

  return (
    <div className="tasks-page">
      <div className="container">
        <div className="tasks-header">
          <div>
            <h1 className="page-title">Browse Tasks</h1>
            <p className="page-subtitle">Find beginner-friendly tasks that match your skills</p>
          </div>
        </div>

        {/* Search & Filters */}
        <div className="tasks-controls card">
          <form className="search-form" onSubmit={handleSearch}>
            <input
              type="text"
              className="form-control search-input"
              placeholder="Search tasks..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <button type="submit" className="btn btn-primary">Search</button>
          </form>

          <div className="filters">
            <div className="filter-group">
              <label>Category</label>
              <select
                className="form-control"
                value={category}
                onChange={(e) => handleFilterChange('category', e.target.value)}
              >
                <option value="all">All Categories</option>
                {CATEGORIES.map((c) => (
                  <option key={c.value} value={c.value}>{c.icon} {c.label}</option>
                ))}
              </select>
            </div>

            <div className="filter-group">
              <label>Difficulty</label>
              <select
                className="form-control"
                value={difficulty}
                onChange={(e) => handleFilterChange('difficulty', e.target.value)}
              >
                <option value="all">All Levels</option>
                {DIFFICULTIES.map((d) => (
                  <option key={d.value} value={d.value}>{d.label}</option>
                ))}
              </select>
            </div>

            {(category !== 'all' || difficulty !== 'all' || search) && (
              <button
                className="btn btn-outline btn-sm"
                onClick={() => {
                  setCategory('all');
                  setDifficulty('all');
                  setSearch('');
                  setSearchParams({});
                  fetchTasks('');
                }}
              >
                Clear Filters
              </button>
            )}
          </div>
        </div>

        {/* Results */}
        {loading ? (
          <div className="spinner"></div>
        ) : error ? (
          <div className="empty-state">
            <div className="icon">⚠️</div>
            <p>{error}</p>
          </div>
        ) : tasks.length === 0 ? (
          <div className="empty-state">
            <div className="icon">🔍</div>
            <h3>No tasks found</h3>
            <p>Try adjusting your filters or check back later for new tasks.</p>
          </div>
        ) : (
          <>
            <div className="results-count">
              Found <strong>{tasks.length}</strong> task{tasks.length !== 1 ? 's' : ''}
            </div>
            <div className="tasks-grid">
              {tasks.map((task) => (
                <TaskCard key={task.id} task={task} />
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
}
