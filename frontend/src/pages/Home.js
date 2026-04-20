import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { CATEGORIES } from '../utils/helpers';
import './Home.css';

const FEATURES = [
  { icon: '🎯', title: 'Easy Tasks Only', desc: 'All tasks are designed for students — no high expertise required.' },
  { icon: '💰', title: 'Earn While Learning', desc: 'Get paid for tasks that match your current skill level.' },
  { icon: '🤝', title: 'Student Community', desc: 'Connect with fellow students who need help or want to work.' },
  { icon: '⚡', title: 'Quick Start', desc: 'Sign up, browse tasks, and start working in minutes.' },
];

const STATS = [
  { value: '500+', label: 'Active Students' },
  { value: '200+', label: 'Tasks Posted' },
  { value: '95%', label: 'Success Rate' },
  { value: '$10-$100', label: 'Avg. Task Budget' },
];

export default function Home() {
  const { user } = useAuth();

  return (
    <div className="home">
      {/* Hero Section */}
      <section className="hero">
        <div className="container hero-inner">
          <div className="hero-badge">🎓 Built for Students, By Students</div>
          <h1 className="hero-title">
            Find Easy Freelance Tasks<br />
            <span className="hero-highlight">No Experience Needed</span>
          </h1>
          <p className="hero-subtitle">
            SkillHub connects students who need help with those who have the skills.
            Browse beginner-friendly tasks, earn money, and build your portfolio.
          </p>
          <div className="hero-actions">
            <Link to="/tasks" className="btn btn-primary btn-lg">
              Browse Tasks →
            </Link>
            {!user && (
              <Link to="/register" className="btn btn-outline btn-lg">
                Join for Free
              </Link>
            )}
            {user && (
              <Link to="/post-task" className="btn btn-secondary btn-lg">
                Post a Task
              </Link>
            )}
          </div>
        </div>
        <div className="hero-illustration">
          <div className="floating-card card-1">
            <span>🎨</span>
            <div>
              <div className="fc-title">Logo Design</div>
              <div className="fc-budget">$25</div>
            </div>
          </div>
          <div className="floating-card card-2">
            <span>💻</span>
            <div>
              <div className="fc-title">Landing Page</div>
              <div className="fc-budget">$50</div>
            </div>
          </div>
          <div className="floating-card card-3">
            <span>✍️</span>
            <div>
              <div className="fc-title">Blog Post</div>
              <div className="fc-budget">$15</div>
            </div>
          </div>
        </div>
      </section>

      {/* Stats */}
      <section className="stats-section">
        <div className="container">
          <div className="stats-grid">
            {STATS.map((stat) => (
              <div key={stat.label} className="stat-item">
                <div className="stat-value">{stat.value}</div>
                <div className="stat-label">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="features-section">
        <div className="container">
          <h2 className="section-title">Why SkillHub?</h2>
          <p className="section-subtitle">The platform built with student needs in mind</p>
          <div className="features-grid">
            {FEATURES.map((f) => (
              <div key={f.title} className="feature-card card">
                <div className="feature-icon">{f.icon}</div>
                <h3 className="feature-title">{f.title}</h3>
                <p className="feature-desc">{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Categories */}
      <section className="categories-section">
        <div className="container">
          <h2 className="section-title">Explore by Category</h2>
          <p className="section-subtitle">Find tasks that match your skills</p>
          <div className="categories-grid">
            {CATEGORIES.map((cat) => (
              <Link
                key={cat.value}
                to={`/tasks?category=${cat.value}`}
                className="category-card"
              >
                <span className="cat-icon">{cat.icon}</span>
                <span className="cat-label">{cat.label}</span>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      {!user && (
        <section className="cta-section">
          <div className="container">
            <div className="cta-card card">
              <h2>Ready to get started?</h2>
              <p>Join hundreds of students already earning on SkillHub</p>
              <div className="cta-actions">
                <Link to="/register" className="btn btn-primary btn-lg">
                  Create Free Account
                </Link>
                <Link to="/tasks" className="btn btn-outline btn-lg">
                  Browse Tasks First
                </Link>
              </div>
            </div>
          </div>
        </section>
      )}

      {/* Footer */}
      <footer className="footer">
        <div className="container">
          <p>© 2024 SkillHub — Mini Freelance Platform for Students</p>
        </div>
      </footer>
    </div>
  );
}
