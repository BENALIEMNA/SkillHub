export const CATEGORIES = [
  { value: 'web-development', label: 'Web Development', icon: '💻' },
  { value: 'graphic-design', label: 'Graphic Design', icon: '🎨' },
  { value: 'writing', label: 'Writing & Content', icon: '✍️' },
  { value: 'data-entry', label: 'Data Entry', icon: '📊' },
  { value: 'tutoring', label: 'Tutoring & Teaching', icon: '📚' },
  { value: 'video-editing', label: 'Video Editing', icon: '🎬' },
  { value: 'social-media', label: 'Social Media', icon: '📱' },
  { value: 'translation', label: 'Translation', icon: '🌐' },
  { value: 'research', label: 'Research', icon: '🔍' },
  { value: 'other', label: 'Other', icon: '⚡' },
];

export const DIFFICULTIES = [
  { value: 'beginner', label: 'Beginner', description: 'No experience needed', color: '#dbeafe' },
  { value: 'easy', label: 'Easy', description: 'Basic skills required', color: '#d1fae5' },
  { value: 'intermediate', label: 'Intermediate', description: 'Some experience helpful', color: '#fef3c7' },
];

export const getCategoryLabel = (value) =>
  CATEGORIES.find((c) => c.value === value)?.label || value;

export const getCategoryIcon = (value) =>
  CATEGORIES.find((c) => c.value === value)?.icon || '⚡';

export const getDifficultyLabel = (value) =>
  DIFFICULTIES.find((d) => d.value === value)?.label || value;

export const formatCurrency = (amount) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', minimumFractionDigits: 0 }).format(amount);

export const formatDate = (dateStr) =>
  new Date(dateStr).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });

export const timeAgo = (dateStr) => {
  const diff = Date.now() - new Date(dateStr).getTime();
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  if (days > 0) return `${days}d ago`;
  if (hours > 0) return `${hours}h ago`;
  if (minutes > 0) return `${minutes}m ago`;
  return 'just now';
};
