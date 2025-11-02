function animateCardsOnScroll() {
  const cards = document.querySelectorAll('.card');

  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting && !entry.target.classList.contains('show')) {
        const index = Array.from(cards).indexOf(entry.target);
        setTimeout(() => {
          entry.target.classList.add('appear');
        }, index * 150);
      }
    });
  }, { threshold: 0.2 });

  cards.forEach(card => observer.observe(card));
}

window.addEventListener('DOMContentLoaded', animateCardsOnScroll);
