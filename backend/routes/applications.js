const express = require('express');
const db = require('../db');
const authMiddleware = require('../middleware/auth');

const router = express.Router();

// Apply for a task
router.post('/', authMiddleware, (req, res) => {
  const { task_id, message } = req.body;

  if (!task_id || !message) {
    return res.status(400).json({ error: 'Task ID and message are required' });
  }

  const task = db.prepare('SELECT * FROM tasks WHERE id = ?').get(task_id);
  if (!task) return res.status(404).json({ error: 'Task not found' });
  if (task.status !== 'open') return res.status(400).json({ error: 'Task is not open for applications' });
  if (task.client_id === req.user.id) return res.status(400).json({ error: 'You cannot apply to your own task' });

  const existing = db.prepare(
    'SELECT * FROM applications WHERE task_id = ? AND freelancer_id = ?'
  ).get(task_id, req.user.id);
  if (existing) return res.status(409).json({ error: 'You have already applied to this task' });

  const result = db.prepare(
    'INSERT INTO applications (task_id, freelancer_id, message) VALUES (?, ?, ?)'
  ).run(task_id, req.user.id, message);

  const application = db.prepare(`
    SELECT a.*, u.name as freelancer_name, u.email as freelancer_email, u.bio as freelancer_bio, u.skills as freelancer_skills
    FROM applications a
    JOIN users u ON a.freelancer_id = u.id
    WHERE a.id = ?
  `).get(result.lastInsertRowid);

  res.status(201).json(application);
});

// Get applications for a task (task owner only)
router.get('/task/:taskId', authMiddleware, (req, res) => {
  const task = db.prepare('SELECT * FROM tasks WHERE id = ?').get(req.params.taskId);
  if (!task) return res.status(404).json({ error: 'Task not found' });
  if (task.client_id !== req.user.id) return res.status(403).json({ error: 'Not authorized' });

  const applications = db.prepare(`
    SELECT a.*, u.name as freelancer_name, u.email as freelancer_email, u.bio as freelancer_bio, u.skills as freelancer_skills
    FROM applications a
    JOIN users u ON a.freelancer_id = u.id
    WHERE a.task_id = ?
    ORDER BY a.created_at DESC
  `).all(req.params.taskId);

  res.json(applications.map(app => ({
    ...app,
    freelancer_skills: JSON.parse(app.freelancer_skills || '[]'),
  })));
});

// Get my applications
router.get('/my', authMiddleware, (req, res) => {
  const applications = db.prepare(`
    SELECT a.*, t.title as task_title, t.category, t.budget, t.deadline, t.status as task_status,
           u.name as client_name
    FROM applications a
    JOIN tasks t ON a.task_id = t.id
    JOIN users u ON t.client_id = u.id
    WHERE a.freelancer_id = ?
    ORDER BY a.created_at DESC
  `).all(req.user.id);

  res.json(applications);
});

// Accept/reject application (task owner only)
router.put('/:id', authMiddleware, (req, res) => {
  const { status } = req.body;
  if (!['accepted', 'rejected'].includes(status)) {
    return res.status(400).json({ error: 'Invalid status' });
  }

  const application = db.prepare(`
    SELECT a.*, t.client_id
    FROM applications a
    JOIN tasks t ON a.task_id = t.id
    WHERE a.id = ?
  `).get(req.params.id);

  if (!application) return res.status(404).json({ error: 'Application not found' });
  if (application.client_id !== req.user.id) return res.status(403).json({ error: 'Not authorized' });

  db.prepare('UPDATE applications SET status = ? WHERE id = ?').run(status, req.params.id);

  if (status === 'accepted') {
    db.prepare('UPDATE tasks SET status = ?, freelancer_id = ? WHERE id = ?')
      .run('in_progress', application.freelancer_id, application.task_id);
    // Reject other applications
    db.prepare('UPDATE applications SET status = ? WHERE task_id = ? AND id != ?')
      .run('rejected', application.task_id, req.params.id);
  }

  const updated = db.prepare('SELECT * FROM applications WHERE id = ?').get(req.params.id);
  res.json(updated);
});

module.exports = router;
