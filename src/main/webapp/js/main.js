// DOM Elements - Move these to the top before they're used
const moviesContainer = document.getElementById('moviesContainer');
const searchInput = document.querySelector('.search-box input');
const searchButton = document.querySelector('.search-box button');

// Loading state
function setLoading(isLoading) {
    if (isLoading) {
        moviesContainer.innerHTML = '<div class="text-center w-100"><div class="spinner-border" role="status"></div></div>';
    }
}


// Fix the fetch function to handle URLs correctly
async function fetchMovies(searchQuery = '') {
    try {
        // Simplified URL construction
        const baseUrl = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)) || '';
        const url = searchQuery 
            ? `${baseUrl}/MovieServlet?action=search&query=${encodeURIComponent(searchQuery)}`
            : `${baseUrl}/MovieServlet`;
            
        console.log('Fetching from URL:', url);
        
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const text = await response.text();
        console.log('Raw response:', text);
        
        try {
            const data = JSON.parse(text);
            console.log('Parsed data:', data);
            return Array.isArray(data) ? data : [];
        } catch (parseError) {
            console.error('JSON parse error:', parseError);
            return [];
        }
    } catch (error) {
        console.error('Error fetching movies:', error);
        return [];
    }
}

// Move handleBooking function definition before it's used
async function handleBooking(event) {
    event.preventDefault();
    const movieCard = event.target.closest('.movie-card');
    if (!movieCard) return;
    
    const movieId = movieCard.dataset.movieId;
    if (!movieId) {
        console.error('No movie ID found');
        return;
    }
    
    // Check if user is logged in
    const isLoggedIn = document.querySelector('body').classList.contains('logged-in');
    
    if (!isLoggedIn) {
        window.location.href = `login.jsp?redirect=booking.jsp?movieId=${movieId}`;
        return;
    }
    
    try {
        window.location.href = `booking.jsp?movieId=${movieId}`;
    } catch (error) {
        console.error('Booking error:', error);
        alert('Error processing booking. Please try again.');
    }
}

// Update the createMovieCard function with better error handling
function createMovieCard(movie) {
    if (!movie || !movie.id) return '';
    
    try {
        const rating = Number(movie.rating || 0).toFixed(1);
        const title = movie.title || 'Untitled';
        const description = movie.description || 'No description available';
        const genre = movie.genre || 'Unspecified';
        const price = Number(movie.price || 0).toFixed(2);
        
        const imageSection = movie.imageUrl ? 
            `<img src="${movie.imageUrl}" 
                 alt="${title}" 
                 class="img-fluid" 
                 onerror="this.parentElement.innerHTML='<div class=\'no-image-placeholder\'>No Image Available</div>'">` :
            `<div class="no-image-placeholder">No Image Available</div>`;
        
        return `
            <div class="col-md-3 col-sm-6 mb-4">
                <div class="movie-card" data-movie-id="${movie.id}">
                    <div class="image-container">
                        ${imageSection}
                    </div>
                    <div class="movie-info">
                        <h5 title="${title}">${title}</h5>
                        <p class="movie-description">${description.substring(0, 100)}${description.length > 100 ? '...' : ''}</p>
                        <div class="movie-rating mb-2">
                            <span class="stars">${'★'.repeat(Math.round(Number(rating)))}${'☆'.repeat(5 - Math.round(Number(rating)))}</span>
                            <span class="rating-value">${rating}</span>
                        </div>
                        <span class="badge badge-secondary mb-2">${genre}</span>
                        <p class="movie-price">₹${price}</p>
                        <button class="btn btn-primary btn-block book-now">Book Now</button>
                    </div>
                </div>
            </div>
        `;
    } catch (error) {
        console.error('Error creating movie card:', error);
        return '';
    }
}

// Fix 3: Render movies with proper error handling
function renderMovies(moviesToRender) {
    try {
        if (!Array.isArray(moviesToRender)) {
            throw new Error('Movies data must be an array');
        }
        
        moviesContainer.innerHTML = moviesToRender
            .map(movie => createMovieCard(movie))
            .join('');

        // Add click handlers to book buttons
        document.querySelectorAll('.book-now').forEach(button => {
            button.addEventListener('click', handleBooking);
        });
    } catch (error) {
        console.error('Error rendering movies:', error);
        moviesContainer.innerHTML = '<div class="alert alert-danger">Error displaying movies</div>';
    }
}

// Fix 4: Search handler with proper closure and timeout handling
const handleSearch = (function() {
    let searchTimeout = null;
    
    return function() {
        if (searchTimeout) {
            clearTimeout(searchTimeout);
        }
        
        searchTimeout = setTimeout(async function() {
            const searchTerm = searchInput.value.trim();
            setLoading(true);
            
            try {
                const movies = await fetchMovies(searchTerm);
                if (!movies || movies.length === 0) {
                    moviesContainer.innerHTML = '<div class="alert alert-info">No movies found</div>';
                    return;
                }
                renderMovies(movies);
            } catch (error) {
                console.error('Search error:', error);
                moviesContainer.innerHTML = '<div class="alert alert-danger">Error loading movies</div>';
            } finally {
                setLoading(false);
            }
        }, 300);
    };
})();

// Event listeners
searchInput.addEventListener('input', handleSearch);
searchButton.addEventListener('click', function(e) {
    e.preventDefault();
    handleSearch();
});

// Fix 5: Export module properly
window.movieApp = {
    handleSearch: handleSearch,
    renderMovies: renderMovies,
    handleBooking: handleBooking,
    fetchMovies: fetchMovies
};

// Fix 3: Update tooltip initialization for older browsers
function initializeTooltips() {
    if (typeof bootstrap !== 'undefined' && typeof bootstrap.Tooltip === 'function') {
        const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
        Array.from(tooltipTriggerList).forEach(el => new bootstrap.Tooltip(el));
    }
}

// Fix 5: Update DOM ready handler
document.addEventListener('DOMContentLoaded', async () => {
    console.log('DOM loaded, fetching movies...'); // Add logging
    setLoading(true);
    try {
        const movies = await fetchMovies();
        console.log('Fetched movies:', movies); // Add logging
        if (!movies || movies.length === 0) {
            moviesContainer.innerHTML = '<div class="alert alert-info">No movies available</div>';
            return;
        }
        renderMovies(movies);
        initializeTooltips();
    } catch (error) {
        console.error('Initialization error:', error);
        moviesContainer.innerHTML = '<div class="alert alert-danger">Failed to load movies</div>';
    } finally {
        setLoading(false);
    }
});

// Navbar scroll behavior
window.addEventListener('scroll', () => {
    const navbar = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        navbar.classList.add('navbar-scrolled');
    } else {
        navbar.classList.remove('navbar-scrolled');
    }
});

// Movie card hover effects
document.addEventListener('mouseover', (e) => {
    const movieCard = e.target.closest('.movie-card');
    if (movieCard) {
        movieCard.classList.add('hover');
    }
});

document.addEventListener('mouseout', (e) => {
    const movieCard = e.target.closest('.movie-card');
    if (movieCard) {
        movieCard.classList.remove('hover');
    }
});

// Auto-rotate featured movies (if exists)
let currentMovieIndex = 0;
const featuredMovies = document.querySelectorAll('.featured-movie');

function rotateFeaturedMovies() {
    if (featuredMovies.length > 1) {
        featuredMovies.forEach(movie => movie.classList.remove('active'));
        currentMovieIndex = (currentMovieIndex + 1) % featuredMovies.length;
        featuredMovies[currentMovieIndex].classList.add('active');
    }
}

// Rotate featured movies every 5 seconds if they exist
if (featuredMovies.length > 0) {
    setInterval(rotateFeaturedMovies, 5000);
}

// Handle movie filtering by genre (if genre buttons exist)
const genreButtons = document.querySelectorAll('.genre-filter');
genreButtons.forEach(button => {
    button.addEventListener('click', async () => {
        const genre = button.dataset.genre;
        setLoading(true);
        try {
            const movies = await fetchMovies();
            const filteredMovies = genre === 'all' 
                ? movies 
                : movies.filter(movie => (movie.genre || '').toLowerCase() === genre.toLowerCase());
            renderMovies(filteredMovies);
        } catch (error) {
            console.error('Genre filtering error:', error);
            moviesContainer.innerHTML = '<div class="alert alert-danger">Error filtering movies</div>';
        } finally {
            setLoading(false);
        }
        
        // Update active button state
        genreButtons.forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');
    });
});
