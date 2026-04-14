const express = require('express');
const db = require('../db');
const authMiddleware = require('../middleware/auth');

const router = express.Router();

// Get all tasks (with optional filters)
router.get('/', (req, res) => {
  const { category, status, difficulty, search } = req.query;

  let query = `
    SELECT t.*, u.name as client_name, u.email as client_email
    FROM tasks t
    JOIN users u ON t.client_id = u.id
    WHERE 1=1
  `;
  const params = [];

  if (category && category !== 'all') {
    query += ' AND t.category = ?';
    params.push(category);
  }
  if (status) {
    query += ' AND t.status = ?';
    params.push(status);
  } else {
    query += " AND t.status = 'open'";
  }
  if (difficulty && difficulty !== 'all') {
    query += ' AND t.difficulty = ?';
    params.push(difficulty);
  }
  if (search) {
    query += ' AND (t.title LIKE ? OR t.description LIKE ?)';
    params.push(`%${search}%`, `%${search}%`);
  }

  query += ' ORDER BY t.created_at DESC';

  const tasks = db.prepare(query).all(...params);
  res.json(tasks);
});

// Get single task
router.get('/:id', (req, res) => {
  const task = db.prepare(`
    SELECT t.*, u.name as client_name, u.email as client_email
    FROM tasks t
    JOIN users u ON t.client_id = u.id
    WHERE t.id = ?
  `).get(req.params.id);

  if (!task) return res.status(404).json({ error: 'Task not found' });
  res.json(task);
});

// Create task
router.post('/', authMiddleware, (req, res) => {
  const { title, description, category, budget, deadline, difficulty } = req.body;

  if (!title || !description || !category || !budget || !deadline) {
    return res.status(400).json({ error: 'All fields are required' });
  }

  const validDifficulties = ['beginner', 'easy', 'intermediate'];
  const taskDifficulty = difficulty || 'easy';
  if (!validDifficulties.includes(taskDifficulty)) {
    return res.status(400).json({ error: 'Invalid difficulty level' });
  }

  const result = db.prepare(
    'INSERT INTO tasks (title, description, category, budget, deadline, difficulty, client_id) VALUES (?, ?, ?, ?, ?, ?, ?)'
  ).run(title, description, category, budget, deadline, taskDifficulty, req.user.id);

  const task = db.prepare('SELECT t.*, u.name as client_name FROM tasks t JOIN users u ON t.client_id = u.id WHERE t.id = ?')
    .get(result.lastInsertRowid);

  res.status(201).json(task);
});

// Update task
router.put('/:id', authMiddleware, (req, res) => {
  const task = db.prepare('SELECT * FROM tasks WHERE id = ?').get(req.params.id);
  if (!task) return res.status(404).json({ error: 'Task not found' });
  if (task.client_id !== req.user.id) return res.status(403).json({ error: 'Not authorized' });

  const { title, description, category, budget, deadline, difficulty, status } = req.body;

  db.prepare(
    'UPDATE tasks SET title = ?, description = ?, category = ?, budget = ?, deadline = ?, difficulty = ?, status = ? WHERE id = ?'
  ).run(
    title || task.title,
    description || task.description,
    category || task.category,
    budget || task.budget,
    deadline || task.deadline,
    difficulty || task.difficulty,
    status || task.status,
    req.params.id
  );

  const updated = db.prepare('SELECT t.*, u.name as client_name FROM tasks t JOIN users u ON t.client_id = u.id WHERE t.id = ?')
    .get(req.params.id);
  res.json(updated);
});

// Delete task
router.delete('/:id', authMiddleware, (req, res) => {
  const task = db.prepare('SELECT * FROM tasks WHERE id = ?').get(req.params.id);
  if (!task) return res.status(404).json({ error: 'Task not found' });
  if (task.client_id !== req.user.id) return res.status(403).json({ error: 'Not authorized' });

  db.prepare('DELETE FROM applications WHERE task_id = ?').run(req.params.id);
  db.prepare('DELETE FROM tasks WHERE id = ?').run(req.params.id);
  res.json({ message: 'Task deleted' });
});

// Get my posted tasks
router.get('/my/posted', authMiddleware, (req, res) => {
  const tasks = db.prepare(
    'SELECT * FROM tasks WHERE client_id = ? ORDER BY created_at DESC'
  ).all(req.user.id);
  res.json(tasks);
});

module.exports = router;
