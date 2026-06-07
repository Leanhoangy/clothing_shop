// Admin JS: chart init, dark mode toggle, loading & empty states
document.addEventListener('DOMContentLoaded', function() {
  // Mobile menu toggle
  const mobileMenuBtn = document.getElementById('mobile-menu-button');
  const sidebar = document.querySelector('aside');
  if (mobileMenuBtn && sidebar) {
    mobileMenuBtn.addEventListener('click', () => {
      sidebar.classList.toggle('hidden');
    });
  }

  // Dark mode toggle
  const darkToggle = document.getElementById('dark-toggle');
  if (darkToggle) {
    darkToggle.addEventListener('click', () => {
      document.documentElement.classList.toggle('dark');
      const isDark = document.documentElement.classList.contains('dark');
      localStorage.setItem('admin-dark', isDark ? '1' : '0');
    });
    const saved = localStorage.getItem('admin-dark');
    if (saved === '1') document.documentElement.classList.add('dark');
  }

  // Revenue chart (use server-provided ADMIN_MONTHLY if available)
  const ctx = document.getElementById('revenueChart');
  if (ctx) {
    const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    let monthly = (typeof ADMIN_MONTHLY !== 'undefined' && Array.isArray(ADMIN_MONTHLY)) ? ADMIN_MONTHLY : [12000,15000,14000,18000,20000,17000,22000,24000,23000,25000,27000,30000];
    // Ensure numeric
    monthly = monthly.map(v => typeof v === 'object' && v !== null && v.hasOwnProperty('intValue') ? Number(v.intValue) : Number(v));
    const data = {
      labels: months,
      datasets: [{
        label: 'Revenue',
        data: monthly,
        borderColor: '#4F46E5',
        backgroundColor: 'rgba(79,70,229,0.06)'
      }]
    };
    new Chart(ctx, { type: 'line', data: data, options: { responsive: true, maintainAspectRatio: true, aspectRatio: 2.5 } });
  }
});
