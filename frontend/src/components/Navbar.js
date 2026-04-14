import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const isActive = (path) => location.pathname === path;

  return (
    <nav className="navbar">
      <div className="container navbar-inner">
        <Link to="/" className="navbar-brand">
          <span className="brand-icon">⚡</span>
          <span className="brand-name">SkillHub</span>
        </Link>

        <button className="menu-toggle" onClick={() => setMenuOpen(!menuOpen)} aria-label="Toggle menu">
          {menuOpen ? '✕' : '☰'}
        </button>

        <div className={`navbar-links ${menuOpen ? 'open' : ''}`}>
          <Link to="/tasks" className={`nav-link ${isActive('/tasks') ? 'active' : ''}`} onClick={() => setMenuOpen(false)}>
            Browse Tasks
          </Link>

          {user ? (
            <>
              <Link to="/post-task" className={`nav-link ${isActive('/post-task') ? 'active' : ''}`} onClick={() => setMenuOpen(false)}>
                Post a Task
              </Link>
              <Link to="/dashboard" className={`nav-link ${isActive('/dashboard') ? 'active' : ''}`} onClick={() => setMenuOpen(false)}>
                Dashboard
              </Link>
              <div className="navbar-user">
                <Link to="/profile" className="user-avatar" onClick={() => setMenuOpen(false)} title={user.name}>
                  {user.name.charAt(0).toUpperCase()}
                </Link>
                <button className="btn btn-outline btn-sm" onClick={handleLogout}>
                  Logout
                </button>
              </div>
            </>
          ) : (
            <div className="navbar-auth">
              <Link to="/login" className="btn btn-outline btn-sm" onClick={() => setMenuOpen(false)}>
                Login
              </Link>
              <Link to="/register" className="btn btn-primary btn-sm" onClick={() => setMenuOpen(false)}>
                Sign Up
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
